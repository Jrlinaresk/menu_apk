package com.palgao.menu.modules.Bussiness.Fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.palgao.menu.modules.Bussiness.entityes.Business;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusinessViewModel extends ViewModel {
    private final BusinessRepository repository;
    private final MutableLiveData<List<Business>> businessList = new MutableLiveData<>();
    private final MutableLiveData<Business> businessDetail = new MutableLiveData<>();

    public BusinessViewModel() {
        repository = new BusinessRepository();
    }

    public LiveData<List<Business>> getAllBusinesses() {
        repository.findAll().enqueue(new Callback<List<Business>>() {
            @Override
            public void onResponse(Call<List<Business>> call, Response<List<Business>> response) {
                if (response.isSuccessful()) {
                    businessList.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Business>> call, Throwable t) {
                // Manejar errores
            }
        });
        return businessList;
    }

    public LiveData<Business> getBusinessById(String id) {
        repository.findOne(id).enqueue(new Callback<Business>() {
            @Override
            public void onResponse(Call<Business> call, Response<Business> response) {
                if (response.isSuccessful()) {
                    businessDetail.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Business> call, Throwable t) {
                // Manejar errores
            }
        });
        return businessDetail;
    }

    public void createBusiness(Business business) {
        repository.create(business).enqueue(new Callback<Business>() {
            @Override
            public void onResponse(Call<Business> call, Response<Business> response) {
                // Manejar la creación exitosa
            }

            @Override
            public void onFailure(Call<Business> call, Throwable t) {
                // Manejar errores
            }
        });
    }

    public void updateBusiness(String id, Business business) {
        repository.update(id, business).enqueue(new Callback<Business>() {
            @Override
            public void onResponse(Call<Business> call, Response<Business> response) {
                // Manejar la actualización exitosa
            }

            @Override
            public void onFailure(Call<Business> call, Throwable t) {
                // Manejar errores
            }
        });
    }

    public void removeBusiness(String id) {
        repository.remove(id).enqueue(new Callback<Business>() {
            @Override
            public void onResponse(Call<Business> call, Response<Business> response) {
                // Manejar la eliminación exitosa
            }

            @Override
            public void onFailure(Call<Business> call, Throwable t) {
                // Manejar errores
            }
        });
    }
}
