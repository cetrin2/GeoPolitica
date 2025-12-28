package com.idigital.geopolitica.ui.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.idigital.geopolitica.R;
import com.idigital.geopolitica.ui.fragments.AccountFragment;
import com.idigital.geopolitica.ui.fragments.MapFragment;
import com.idigital.geopolitica.ui.fragments.OrdersFragment;
import com.idigital.geopolitica.ui.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(navListener);

        // Load default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new MapFragment())
                    .commit();
        }
    }

    private BottomNavigationView.OnItemSelectedListener navListener =
            new BottomNavigationView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    int itemId = item.getItemId();
                    if (itemId == R.id.nav_map) {
                        selectedFragment = new MapFragment();
                    } else if (itemId == R.id.nav_search) {
                        selectedFragment = new SearchFragment();
                    } else if (itemId == R.id.nav_orders) {
                        selectedFragment = new OrdersFragment();
                    } else if (itemId == R.id.nav_account) {
                        selectedFragment = new AccountFragment();
                    }

                    if (selectedFragment != null) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragmentContainer, selectedFragment)
                                .commit();
                    }

                    return true;
                }
            };
}