package com.palgao.menu.modules.Bussiness;
import com.palgao.menu.modules.Bussiness.entityes.Bussiness;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @GET("business")
    Call<List<Bussiness>> findAll();

    @GET("business/{id}")
    Call<Bussiness> findOne(@Path("id") String id);

    @POST("business")
    Call<Bussiness> create(@Body Bussiness bussiness);

    @PATCH("business/{id}")
    Call<Bussiness> update(@Path("id") String id, @Body Bussiness bussiness);

    @DELETE("business/{id}")
    Call<Bussiness> remove(@Path("id") String id);
}
