package com.idigital.geopolitica.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.idigital.geopolitica.R;
import com.idigital.geopolitica.database.AppDatabase;
import com.idigital.geopolitica.models.User;
import com.idigital.geopolitica.network.ApiService;
import com.idigital.geopolitica.network.RetrofitClient;
import com.idigital.geopolitica.security.SecurePreferences;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout emailLayout, passwordLayout;
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView registerTextView;
    private ProgressBar progressBar;
    private SecurePreferences securePrefs;
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        securePrefs = SecurePreferences.getInstance(this);
        database = AppDatabase.getInstance(this);

        loginButton.setOnClickListener(v -> attemptLogin());
        registerTextView.setOnClickListener(v -> {
            // Navigate to registration
            Toast.makeText(this, "Регистрация в разработке", Toast.LENGTH_SHORT).show();
        });
    }

    private void initViews() {
        emailLayout = findViewById(R.id.emailInputLayout);
        passwordLayout = findViewById(R.id.passwordInputLayout);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerTextView = findViewById(R.id.registerTextView);
        progressBar = findViewById(R.id.progressBar);
    }

    private void attemptLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        emailLayout.setError(null);
        passwordLayout.setError(null);

        if (TextUtils.isEmpty(email)) {
            emailLayout.setError("Введите email");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordLayout.setError("Введите пароль");
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("Неверный формат email");
            return;
        }

        performLogin(email, password);
    }

    private void performLogin(String email, String password) {
        showLoading(true);

        ApiService apiService = RetrofitClient.getApiService();
        ApiService.LoginRequest request = new ApiService.LoginRequest(email, password);

        apiService.login(request).enqueue(new Callback<ApiService.LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiService.LoginResponse> call,
                                   @NonNull Response<ApiService.LoginResponse> response) {
                showLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    ApiService.LoginResponse loginResponse = response.body();

                    // Save user data
                    new Thread(() -> {
                        User user = loginResponse.getUser();
                        user.setToken(loginResponse.getToken());
                        user.setLoggedIn(true);

                        long userId = database.userDao().insert(user);

                        // Save token to secure preferences
                        securePrefs.saveAuthToken(loginResponse.getToken());
                        securePrefs.saveUserId(userId);

                        runOnUiThread(() -> {
                            Toast.makeText(LoginActivity.this,
                                    "Вход выполнен успешно",
                                    Toast.LENGTH_SHORT).show();

                            // Navigate to main activity
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        });
                    }).start();
                } else {
                    Toast.makeText(LoginActivity.this,
                            "Ошибка входа. Проверьте данные",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiService.LoginResponse> call,
                                  @NonNull Throwable t) {
                showLoading(false);
                Toast.makeText(LoginActivity.this,
                        "Ошибка соединения: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        loginButton.setEnabled(!show);
        emailEditText.setEnabled(!show);
        passwordEditText.setEnabled(!show);
    }
}