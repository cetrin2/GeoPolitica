package com.idigital.geopolitica.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.idigital.geopolitica.models.User;

@Dao
public interface UserDao {
    @Insert
    long insert(User user);

    @Update
    void update(User user);

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    User getUserByEmail(String email);

    @Query("SELECT * FROM users LIMIT 1")
    User getCurrentUser();

    @Query("DELETE FROM users")
    void deleteAll();
}