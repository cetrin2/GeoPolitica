package com.idigital.geopolitica.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cadastre_parcels")
public class CadastreParcel {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String cadastreNumber;
    private String address;
    private double area;
    private String category;
    private String status;
    private double latitude;
    private double longitude;
    private String ownerInfo;
    private long lastUpdated;
    private boolean isFavorite;

    // Constructors
    public CadastreParcel() {}

    public CadastreParcel(String cadastreNumber, String address, double area,
                          String category, double latitude, double longitude) {
        this.cadastreNumber = cadastreNumber;
        this.address = address;
        this.area = area;
        this.category = category;
        this.latitude = latitude;
        this.longitude = longitude;
        this.lastUpdated = System.currentTimeMillis();
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCadastreNumber() { return cadastreNumber; }
    public void setCadastreNumber(String cadastreNumber) { this.cadastreNumber = cadastreNumber; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public double getArea() { return area; }
    public void setArea(double area) { this.area = area; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String getOwnerInfo() { return ownerInfo; }
    public void setOwnerInfo(String ownerInfo) { this.ownerInfo = ownerInfo; }

    public long getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(long lastUpdated) { this.lastUpdated = lastUpdated; }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }
}