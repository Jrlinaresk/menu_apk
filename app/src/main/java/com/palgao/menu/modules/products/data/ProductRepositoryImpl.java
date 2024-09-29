package com.palgao.menu.modules.products.data;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductRepositoryImpl implements ApiService {
    private final ApiService apiService;
    private static final String BASE_URL = "https://pc3ld10h-8080.usw3.devtunnels.ms/api/v1/";

    public ProductRepositoryImpl() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }
    @Override
    public Call<List<Product>> findAll() {
        return apiService.findAll();
    }
}
