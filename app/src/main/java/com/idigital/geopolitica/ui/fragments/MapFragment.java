package com.idigital.geopolitica.ui.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.idigital.geopolitica.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MapFragment extends Fragment {

    private static final int LOCATION_PERMISSION_REQUEST = 100;
    private FloatingActionButton fabLocation, fabLayers;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        fabLocation = view.findViewById(R.id.fabLocation);
        fabLayers = view.findViewById(R.id.fabLayers);

        fabLocation.setOnClickListener(v -> requestLocationPermission());
        fabLayers.setOnClickListener(v -> showLayersDialog());

        return view;
    }

    private void requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
        } else {
            centerMapOnLocation();
        }
    }

    private void centerMapOnLocation() {
        Toast.makeText(getContext(), "Определение местоположения...", Toast.LENGTH_SHORT).show();
    }

    private void showLayersDialog() {
        Toast.makeText(getContext(), "Выбор слоёв карты", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                centerMapOnLocation();
            }
        }
    }
}