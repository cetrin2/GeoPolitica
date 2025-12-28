package com.idigital.geopolitica.network;

import com.idigital.geopolitica.models.CadastreObject;
import com.idigital.geopolitica.models.CadastreParcel;
import com.idigital.geopolitica.models.Order;
import com.idigital.geopolitica.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // Авторизация
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("auth/register")
    Call<AuthResponse> register(@Body RegisterRequest request);

    @POST("auth/logout")
    Call<Void> logout(@Header("Authorization") String token);

    // Кадастровый объект
    @GET("cadastre/search")
    Call<CadastreObject> searchByCadastralNumber(
            @Header("Authorization") String token,
            @Query("number") String cadastralNumber
    );

    // (новый метод)
    @GET("cadastre/parcel/search")
    Call<CadastreParcel> searchCadastreParcel(
            @Header("Authorization") String token,
            @Query("number") String cadastralNumber
    );

    @GET("cadastre/nearby")
    Call<List<CadastreObject>> getNearbyObjects(
            @Header("Authorization") String token,
            @Query("lat") double latitude,
            @Query("lng") double longitude,
            @Query("radius") int radius
    );

    // Заказы
    @GET("orders")
    Call<List<Order>> getOrders(@Header("Authorization") String token);

    @GET("orders/{id}")
    Call<Order> getOrderById(
            @Header("Authorization") String token,
            @Path("id") String orderId
    );

    @POST("orders")
    Call<Order> createOrder(
            @Header("Authorization") String token,
            @Body OrderRequest request
    );

    // User
    @GET("user/profile")
    Call<User> getUserProfile(@Header("Authorization") String token);

    @POST("user/profile")
    Call<User> updateProfile(
            @Header("Authorization") String token,
            @Body User user
    );

    // Request classes
    class LoginRequest {
        private String email;
        private String password;

        public LoginRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public String getEmail() { return email; }
        public String getPassword() { return password; }
    }

    class RegisterRequest {
        private String email;
        private String password;
        private String name;
        private String phone;

        public RegisterRequest(String email, String password, String name, String phone) {
            this.email = email;
            this.password = password;
            this.name = name;
            this.phone = phone;
        }

        public String getEmail() { return email; }
        public String getPassword() { return password; }
        public String getName() { return name; }
        public String getPhone() { return phone; }
    }

    class OrderRequest {
        private String title;
        private String cadastralNumber;
        private String type;

        public OrderRequest(String title, String cadastralNumber, String type) {
            this.title = title;
            this.cadastralNumber = cadastralNumber;
            this.type = type;
        }

        public String getTitle() { return title; }
        public String getCadastralNumber() { return cadastralNumber; }
        public String getType() { return type; }
    }

    // Response classes
    class LoginResponse {
        private String token;
        private User user;

        public String getToken() { return token; }
        public User getUser() { return user; }

        public void setToken(String token) { this.token = token; }
        public void setUser(User user) { this.user = user; }
    }

    class AuthResponse {
        private String token;
        private User user;

        public String getToken() { return token; }
        public User getUser() { return user; }

        public void setToken(String token) { this.token = token; }
        public void setUser(User user) { this.user = user; }
    }

}
