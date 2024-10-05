package com.palgao.menu.modules.Bussiness.Fragments;
import com.palgao.menu.modules.Bussiness.ApiService;
import com.palgao.menu.modules.Bussiness.entityes.Bussiness;

import java.util.List;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BusinessRepository {
    private final ApiService apiService;
    private static final String BASE_URL = "https://pc3ld10h-8080.usw3.devtunnels.ms/api/v1/";

    public BusinessRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public Call<List<Bussiness>> findAll() {
        return apiService.findAll();
    }

    public Call<Bussiness> findOne(String id) {
        return apiService.findOne(id);
    }

    public Call<Bussiness> create(Bussiness bussiness) {
        return apiService.create(bussiness);
    }

    public Call<Bussiness> update(String id, Bussiness bussiness) {
        return apiService.update(id, bussiness);
    }

    public Call<Bussiness> remove(String id) {
        return apiService.remove(id);
    }
}

