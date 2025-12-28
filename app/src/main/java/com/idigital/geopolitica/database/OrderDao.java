package com.idigital.geopolitica.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.idigital.geopolitica.models.Order;

import java.util.List;

@Dao
public interface OrderDao {
    @Insert
    long insert(Order order);

    @Update
    void update(Order order);

    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY createdDate DESC")
    LiveData<List<Order>> getOrdersByUserId(int userId);

    @Query("SELECT * FROM orders WHERE orderId = :orderId LIMIT 1")
    Order getOrderById(String orderId);

    @Query("DELETE FROM orders")
    void deleteAll();
}