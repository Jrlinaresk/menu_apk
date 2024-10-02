package com.palgao.menu.modules.Bussiness;
import com.palgao.menu.modules.Bussiness.entityes.Business;

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
    Call<List<Business>> findAll();

    @GET("business/{id}")
    Call<Business> findOne(@Path("id") String id);

    @POST("business")
    Call<Business> create(@Body Business business);

    @PATCH("business/{id}")
    Call<Business> update(@Path("id") String id, @Body Business business);

    @DELETE("business/{id}")
    Call<Business> remove(@Path("id") String id);
}
