package com.idigital.geopolitica.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.idigital.geopolitica.R;
import com.idigital.geopolitica.database.AppDatabase;
import com.idigital.geopolitica.models.Order;
import com.idigital.geopolitica.security.SecurePreferences;
import com.idigital.geopolitica.ui.adapters.OrdersAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class OrdersFragment extends Fragment {

    private RecyclerView ordersRecyclerView;
    private OrdersAdapter adapter;
    private ProgressBar progressBar;
    private TextView emptyView;
    private FloatingActionButton fabNewOrder;
    private AppDatabase database;
    private SecurePreferences securePrefs;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        initViews(view);
        setupRecyclerView();

        database = AppDatabase.getInstance(requireContext());
        securePrefs = SecurePreferences.getInstance(requireContext());

        loadOrders();

        fabNewOrder.setOnClickListener(v -> createNewOrder());

        return view;
    }

    private void initViews(View view) {
        ordersRecyclerView = view.findViewById(R.id.ordersRecyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        emptyView = view.findViewById(R.id.emptyView);
        fabNewOrder = view.findViewById(R.id.fabNewOrder);
    }

    private void setupRecyclerView() {
        adapter = new OrdersAdapter(new ArrayList<>(), order -> {
            // Handle order click
        });

        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ordersRecyclerView.setAdapter(adapter);
    }

    private void loadOrders() {
        int userId = securePrefs.getUserId();

        database.orderDao().getOrdersByUserId(userId).observe(getViewLifecycleOwner(), new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orders) {
                progressBar.setVisibility(View.GONE);

                if (orders == null || orders.isEmpty()) {
                    emptyView.setVisibility(View.VISIBLE);
                    ordersRecyclerView.setVisibility(View.GONE);
                } else {
                    emptyView.setVisibility(View.GONE);
                    ordersRecyclerView.setVisibility(View.VISIBLE);
                    adapter.updateOrders(orders);
                }
            }
        });
    }

    private void createNewOrder() {
        // Show dialog to create new order
    }
}