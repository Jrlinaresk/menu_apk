package com.palgao.menu.modules.Bussiness.Fragments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.palgao.menu.modules.Bussiness.BussineRepositoryImpl;
import com.palgao.menu.modules.Bussiness.entityes.Bussiness;
import com.palgao.menu.modules.ProgressDialog.SharedLoadingViewModel;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BussinessViewModel extends ViewModel {
    private final MutableLiveData<List<Bussiness>> businessList = new MutableLiveData<>();
    private final MutableLiveData<Bussiness> businessDetail = new MutableLiveData<>();
    private final SharedLoadingViewModel sharedLoadingViewModel;
    private final BussineRepositoryImpl bussineRepository;


    public BussinessViewModel(SharedLoadingViewModel sharedLoadingViewModel, BussineRepositoryImpl bussineRepository) {
        this.sharedLoadingViewModel = sharedLoadingViewModel;
        this.bussineRepository = bussineRepository;
    }

    public LiveData<List<Bussiness>> getAllBusinesses() {
        bussineRepository.findAll().enqueue(new Callback<List<Bussiness>>() {
            @Override
            public void onResponse(Call<List<Bussiness>> call, Response<List<Bussiness>> response) {
                if (response.isSuccessful()) {
                    businessList.setValue(response.body());
                    sharedLoadingViewModel.setLoadingState(false);
                }
                else {
                    sharedLoadingViewModel.setLoadingState(false);
                }
            }

            @Override
            public void onFailure(Call<List<Bussiness>> call, Throwable t) {
                // Manejar errores
                sharedLoadingViewModel.setLoadingState(false);
            }
        });
        return businessList;
    }

    public LiveData<Bussiness> getBusinessById(String id) {
        bussineRepository.findOne(id).enqueue(new Callback<Bussiness>() {
            @Override
            public void onResponse(Call<Bussiness> call, Response<Bussiness> response) {
                if (response.isSuccessful()) {
                    businessDetail.setValue(response.body());
                    sharedLoadingViewModel.setLoadingState(false);
                }
                else {
                    sharedLoadingViewModel.setLoadingState(false);
                }
            }

            @Override
            public void onFailure(Call<Bussiness> call, Throwable t) {
                // Manejar errores
                sharedLoadingViewModel.setLoadingState(false);
            }
        });
        return businessDetail;
    }

    public void createBusiness(Bussiness bussiness) {
        bussineRepository.create(bussiness).enqueue(new Callback<Bussiness>() {
            @Override
            public void onResponse(Call<Bussiness> call, Response<Bussiness> response) {
                // Manejar la creación exitosa
                sharedLoadingViewModel.setLoadingState(false);
            }

            @Override
            public void onFailure(Call<Bussiness> call, Throwable t) {
                // Manejar errores
                sharedLoadingViewModel.setLoadingState(false);
            }
        });
    }

    public void updateBusiness(String id, Bussiness bussiness) {
        bussineRepository.update(id, bussiness).enqueue(new Callback<Bussiness>() {
            @Override
            public void onResponse(Call<Bussiness> call, Response<Bussiness> response) {
                // Manejar la actualización exitosa
                sharedLoadingViewModel.setLoadingState(false);
            }

            @Override
            public void onFailure(Call<Bussiness> call, Throwable t) {
                // Manejar errores
                sharedLoadingViewModel.setLoadingState(false);
            }
        });
    }

    public void removeBusiness(String id) {
        bussineRepository.remove(id).enqueue(new Callback<Bussiness>() {
            @Override
            public void onResponse(Call<Bussiness> call, Response<Bussiness> response) {
                // Manejar la eliminación exitosa
                sharedLoadingViewModel.setLoadingState(false);
            }

            @Override
            public void onFailure(Call<Bussiness> call, Throwable t) {
                // Manejar errores
                sharedLoadingViewModel.setLoadingState(false);
            }
        });
    }

    public void loadBussiness() {
        bussineRepository.findAll().enqueue(new Callback<List<Bussiness>>() {
            @Override
            public void onResponse(Call<List<Bussiness>> call, Response<List<Bussiness>> response) {
                if (response.isSuccessful()) {
                    businessList.setValue(response.body());

                    List<String> categorias = new ArrayList<>();

                    for (Bussiness product: businessList.getValue()) {
                        categorias.add(product.getType());
                    }
                    
                    sharedLoadingViewModel.setLoadingState(false);
                }
                else {
                    int a = 0;
                    sharedLoadingViewModel.setLoadingState(false);
                }
            }

            @Override
            public void onFailure(Call<List<Bussiness>> call, Throwable t) {
                // Handle failure
                sharedLoadingViewModel.setLoadingState(false);
                int a = 0;
            }
        });
    }
}
