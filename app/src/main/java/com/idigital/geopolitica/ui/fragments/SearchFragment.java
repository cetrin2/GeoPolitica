package com.idigital.geopolitica.ui.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.idigital.geopolitica.R;
import com.idigital.geopolitica.database.AppDatabase;
import com.idigital.geopolitica.models.CadastreParcel;
import com.idigital.geopolitica.network.ApiService;
import com.idigital.geopolitica.network.RetrofitClient;
import com.idigital.geopolitica.security.SecurePreferences;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";

    private EditText searchEditText;
    private MaterialButton searchButton;
    private ProgressBar progressBar;
    private CardView resultCard;
    private TextView cadastreNumberText, addressText, areaText, categoryText, statusText;
    private AppDatabase database;
    private SecurePreferences securePrefs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        initViews(view);
        database = AppDatabase.getInstance(requireContext());
        securePrefs = SecurePreferences.getInstance(requireContext());

        searchButton.setOnClickListener(v -> performSearch());

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchButton.setEnabled(s.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    private void initViews(View view) {
        searchEditText = view.findViewById(R.id.searchEditText);
        searchButton = view.findViewById(R.id.searchButton);
        progressBar = view.findViewById(R.id.progressBar);
        resultCard = view.findViewById(R.id.resultCard);
        cadastreNumberText = view.findViewById(R.id.cadastreNumberText);
        addressText = view.findViewById(R.id.addressText);
        areaText = view.findViewById(R.id.areaText);
        categoryText = view.findViewById(R.id.categoryText);
        statusText = view.findViewById(R.id.statusText);
    }

    private void performSearch() {
        String cadastreNumber = searchEditText.getText().toString().trim();

        if (cadastreNumber.isEmpty()) {
            Toast.makeText(getContext(), "Введите кадастровый номер", Toast.LENGTH_SHORT).show();
            return;
        }

        showLoading(true);
        resultCard.setVisibility(View.GONE);

        // First check offline database
        new Thread(() -> {
            try {
                CadastreParcel offlineParcel = database.cadastreParcelDao().getParcelByNumber(cadastreNumber);

                if (offlineParcel != null) {
                    requireActivity().runOnUiThread(() -> {
                        displayResult(offlineParcel);
                        showLoading(false);
                        Toast.makeText(getContext(), "Данные из локальной базы", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    // Search online
                    searchOnline(cadastreNumber);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error searching in local database", e);
                requireActivity().runOnUiThread(() -> {
                    searchOnline(cadastreNumber);
                });
            }
        }).start();
    }

    private void searchOnline(String cadastreNumber) {
        String token = securePrefs.getAuthToken();

        if (token == null || token.isEmpty()) {
            requireActivity().runOnUiThread(() -> {
                showLoading(false);
                Toast.makeText(getContext(), "Необходима авторизация", Toast.LENGTH_SHORT).show();
            });
            return;
        }

        ApiService apiService = RetrofitClient.getApiService();
        Call<CadastreParcel> call = apiService.searchCadastreParcel("Bearer " + token, cadastreNumber);

        call.enqueue(new Callback<CadastreParcel>() {
            @Override
            public void onResponse(@NonNull Call<CadastreParcel> call, @NonNull Response<CadastreParcel> response) {
                if (!isAdded()) return;

                showLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    CadastreParcel parcel = response.body();
                    displayResult(parcel);

                    // Save to local database
                    new Thread(() -> {
                        try {
                            database.cadastreParcelDao().insert(parcel);
                            Log.d(TAG, "Parcel saved to local database");
                        } catch (Exception e) {
                            Log.e(TAG, "Error saving to database", e);
                        }
                    }).start();

                    Toast.makeText(getContext(), "Данные получены с сервера", Toast.LENGTH_SHORT).show();
                } else {
                    handleErrorResponse(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CadastreParcel> call, @NonNull Throwable t) {
                if (!isAdded()) return;

                showLoading(false);
                Log.e(TAG, "Network error", t);

                String errorMessage;
                if (t instanceof IOException) {
                    errorMessage = "Ошибка сети. Проверьте подключение к интернету";
                } else {
                    errorMessage = "Ошибка: " + t.getMessage();
                }

                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                resultCard.setVisibility(View.GONE);
            }
        });
    }

    private void handleErrorResponse(Response<CadastreParcel> response) {
        String errorMessage;

        switch (response.code()) {
            case 401:
                errorMessage = "Ошибка авторизации. Войдите снова";
                break;
            case 404:
                errorMessage = "Кадастровый номер не найден";
                break;
            case 500:
                errorMessage = "Ошибка сервера. Попробуйте позже";
                break;
            default:
                errorMessage = "Ошибка: " + response.code();
                try {
                    if (response.errorBody() != null) {
                        String error = response.errorBody().string();
                        Log.e(TAG, "Error response: " + error);
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Error reading error body", e);
                }
        }

        Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
        resultCard.setVisibility(View.GONE);
    }

    private void displayResult(CadastreParcel parcel) {
        if (!isAdded()) return;

        resultCard.setVisibility(View.VISIBLE);

        cadastreNumberText.setText(parcel.getCadastreNumber() != null ?
                parcel.getCadastreNumber() : "Не указан");

        addressText.setText(parcel.getAddress() != null ?
                parcel.getAddress() : "Адрес не указан");

        areaText.setText(parcel.getArea() > 0 ?
                String.format("%.2f м²", parcel.getArea()) : "Не указана");

        categoryText.setText(parcel.getCategory() != null ?
                parcel.getCategory() : "Не указана");

        statusText.setText(parcel.getStatus() != null ?
                parcel.getStatus() : "Не указан");
    }

    private void showLoading(boolean show) {
        if (!isAdded()) return;

        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        searchButton.setEnabled(!show);
        searchEditText.setEnabled(!show);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Clean up references
        searchEditText = null;
        searchButton = null;
        progressBar = null;
        resultCard = null;
        cadastreNumberText = null;
        addressText = null;
        areaText = null;
        categoryText = null;
        statusText = null;
    }
}
