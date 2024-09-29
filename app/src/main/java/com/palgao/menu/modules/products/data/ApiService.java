package com.palgao.menu.modules.products.data;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("products") // Asegúrate de poner el endpoint correcto
    Call<List<Product>> findAll();
}
