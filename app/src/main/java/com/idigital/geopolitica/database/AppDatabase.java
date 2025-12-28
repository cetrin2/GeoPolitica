package com.idigital.geopolitica.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.idigital.geopolitica.models.CadastreParcel;
import com.idigital.geopolitica.models.Order;
import com.idigital.geopolitica.models.User;

@Database(entities = {User.class, CadastreParcel.class, Order.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract UserDao userDao();
    public abstract CadastreParcelDao cadastreParcelDao();
    public abstract OrderDao orderDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "cadastre_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}