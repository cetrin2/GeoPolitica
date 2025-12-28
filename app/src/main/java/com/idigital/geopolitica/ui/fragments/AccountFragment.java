package com.idigital.geopolitica.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.idigital.geopolitica.R;
import com.idigital.geopolitica.database.AppDatabase;
import com.idigital.geopolitica.models.User;
import com.idigital.geopolitica.security.SecurePreferences;
import com.idigital.geopolitica.ui.activities.LoginActivity;
import com.google.android.material.button.MaterialButton;

public class AccountFragment extends Fragment {

    private TextView nameText, emailText, phoneText;
    private CardView profileCard, settingsCard, helpCard, aboutCard;
    private MaterialButton logoutButton;
    private AppDatabase database;
    private SecurePreferences securePrefs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        initViews(view);
        database = AppDatabase.getInstance(requireContext());
        securePrefs = SecurePreferences.getInstance(requireContext());

        loadUserData();
        setupClickListeners();

        return view;
    }

    private void initViews(View view) {
        nameText = view.findViewById(R.id.nameText);
        emailText = view.findViewById(R.id.emailText);
        phoneText = view.findViewById(R.id.phoneText);
        profileCard = view.findViewById(R.id.profileCard);
        settingsCard = view.findViewById(R.id.settingsCard);
        helpCard = view.findViewById(R.id.helpCard);
        aboutCard = view.findViewById(R.id.aboutCard);
        logoutButton = view.findViewById(R.id.logoutButton);
    }

    private void loadUserData() {
        new Thread(() -> {
            User user = database.userDao().getCurrentUser();

            if (user != null) {
                requireActivity().runOnUiThread(() -> {
                    nameText.setText(user.getName() != null ? user.getName() : "Пользователь");
                    emailText.setText(user.getEmail());
                    phoneText.setText(user.getPhone() != null ? user.getPhone() : "Не указан");
                });
            }
        }).start();
    }

    private void setupClickListeners() {
        profileCard.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Редактирование профиля", Toast.LENGTH_SHORT).show();
        });

        settingsCard.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Настройки", Toast.LENGTH_SHORT).show();
        });

        helpCard.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Помощь и поддержка", Toast.LENGTH_SHORT).show();
        });

        aboutCard.setOnClickListener(v -> {
            showAboutDialog();
        });

        logoutButton.setOnClickListener(v -> {
            showLogoutDialog();
        });
    }

    private void showAboutDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("О приложении")
                .setMessage("Кадастровое приложение\nВерсия 1.0\n\n" +
                        "Приложение для работы с кадастровой информацией, " +
                        "просмотра карт и управления заказами.")
                .setPositiveButton("OK", null)
                .show();
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Выход")
                .setMessage("Вы уверены, что хотите выйти из аккаунта?")
                .setPositiveButton("Выйти", (dialog, which) -> performLogout())
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void performLogout() {
        new Thread(() -> {
            database.userDao().deleteAll();
            securePrefs.clearAll();

            requireActivity().runOnUiThread(() -> {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                requireActivity().finish();
            });
        }).start();
    }
}