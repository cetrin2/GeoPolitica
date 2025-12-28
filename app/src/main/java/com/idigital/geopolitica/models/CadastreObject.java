package com.idigital.geopolitica.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.Date;

public class CadastreObject implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("cadastral_number")
    private String cadastralNumber;

    @SerializedName("address")
    private String address;

    @SerializedName("area")
    private double area; // площадь в кв.м

    @SerializedName("category")
    private String category; // категория земель

    @SerializedName("cadastral_cost")
    private double cadastralCost; // кадастровая стоимость

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("owner")
    private String owner; // владелец

    @SerializedName("purpose")
    private String purpose; // назначение

    @SerializedName("registration_date")
    private Date registrationDate; // дата постановки на учет

    @SerializedName("status")
    private String status; // статус объекта

    @SerializedName("district")
    private String district; // район

    @SerializedName("region")
    private String region; // регион

    // Конструкторы
    public CadastreObject() {
    }

    public CadastreObject(String cadastralNumber, String address, double latitude, double longitude) {
        this.cadastralNumber = cadastralNumber;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getCadastralNumber() {
        return cadastralNumber;
    }

    public String getAddress() {
        return address;
    }

    public double getArea() {
        return area;
    }

    public String getCategory() {
        return category;
    }

    public double getCadastralCost() {
        return cadastralCost;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getOwner() {
        return owner;
    }

    public String getPurpose() {
        return purpose;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public String getStatus() {
        return status;
    }

    public String getDistrict() {
        return district;
    }

    public String getRegion() {
        return region;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setCadastralNumber(String cadastralNumber) {
        this.cadastralNumber = cadastralNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCadastralCost(double cadastralCost) {
        this.cadastralCost = cadastralCost;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    // Дополнительные методы
    public String getFormattedArea() {
        return String.format("%.2f кв.м", area);
    }

    public String getFormattedCost() {
        return String.format("%.2f руб.", cadastralCost);
    }

    @Override
    public String toString() {
        return "CadastreObject{" +
                "cadastralNumber='" + cadastralNumber + '\'' +
                ", address='" + address + '\'' +
                ", area=" + area +
                '}';
    }
}