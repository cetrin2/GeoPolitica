package com.idigital.geopolitica;

import android.app.Application;
import com.idigital.geopolitica.network.NetworkModule;
import com.idigital.geopolitica.security.SecurePreferences;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CadastreApplication extends Application {

    private static CadastreApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        // Initialize SecurePreferences
        SecurePreferences.getInstance(this);

        // Initialize NetworkModule
        NetworkModule.getRetrofit(this);
    }

    public static CadastreApplication getInstance() {
        return instance;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        // Cleanup resources if needed
    }
}