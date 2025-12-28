package com.idigital.geopolitica.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.idigital.geopolitica.models.CadastreParcel;

import java.util.List;

@Dao
public interface CadastreParcelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(CadastreParcel parcel);

    @Update
    void update(CadastreParcel parcel);

    @Query("SELECT * FROM cadastre_parcels")
    LiveData<List<CadastreParcel>> getAllParcels();

    @Query("SELECT * FROM cadastre_parcels WHERE cadastreNumber = :number LIMIT 1")
    CadastreParcel getParcelByNumber(String number);

    @Query("SELECT * FROM cadastre_parcels WHERE isFavorite = 1")
    LiveData<List<CadastreParcel>> getFavoriteParcels();

    @Query("DELETE FROM cadastre_parcels")
    void deleteAll();
}