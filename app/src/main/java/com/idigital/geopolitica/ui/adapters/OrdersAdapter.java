package com.idigital.geopolitica.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.idigital.geopolitica.R;
import com.idigital.geopolitica.models.Order;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    private List<Order> orders;
    private OnOrderClickListener listener;

    public interface OnOrderClickListener {
        void onOrderClick(Order order);
    }

    public OrdersAdapter(List<Order> orders, OnOrderClickListener listener) {
        this.orders = orders;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.bind(order, listener);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void updateOrders(List<Order> newOrders) {
        this.orders = newOrders;
        notifyDataSetChanged();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView orderIdText, titleText, statusText, dateText;
        View statusIndicator;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.orderCard);
            orderIdText = itemView.findViewById(R.id.orderIdText);
            titleText = itemView.findViewById(R.id.titleText);
            statusText = itemView.findViewById(R.id.statusText);
            dateText = itemView.findViewById(R.id.dateText);
            statusIndicator = itemView.findViewById(R.id.statusIndicator);
        }

        public void bind(Order order, OnOrderClickListener listener) {
            orderIdText.setText("Заказ #" + order.getOrderId());
            titleText.setText(order.getTitle());
            statusText.setText(getStatusText(order.getStatus()));

            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
            dateText.setText(sdf.format(new Date(order.getCreatedDate())));

            // Set status indicator color
            int color = getStatusColor(order.getStatus());
            statusIndicator.setBackgroundColor(color);

            cardView.setOnClickListener(v -> listener.onOrderClick(order));
        }

        private String getStatusText(String status) {
            switch (status) {
                case "pending": return "В ожидании";
                case "processing": return "В работе";
                case "completed": return "Завершён";
                case "cancelled": return "Отменён";
                default: return "Неизвестно";
            }
        }

        private int getStatusColor(String status) {
            switch (status) {
                case "pending": return 0xFFFFA726; // Orange
                case "processing": return 0xFF42A5F5; // Blue
                case "completed": return 0xFF66BB6A; // Green
                case "cancelled": return 0xFFEF5350; // Red
                default: return 0xFF9E9E9E; // Grey
            }
        }
    }
}