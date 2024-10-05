package com.palgao.menu.modules.Bussiness;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.palgao.menu.modules.Bussiness.Fragments.BussinessViewModel;
import com.palgao.menu.modules.ProgressDialog.SharedLoadingViewModel;
import com.palgao.menu.modules.products.ui.ProductsViewModel;

public class BussinessViewModelFactory implements ViewModelProvider.Factory {
    private final SharedLoadingViewModel sharedLoadingViewModel;
    private final BussineRepositoryImpl repository;

    public BussinessViewModelFactory(SharedLoadingViewModel sharedLoadingViewModel, BussineRepositoryImpl repository) {
        this.sharedLoadingViewModel = sharedLoadingViewModel;
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(BussinessViewModel.class)) {
            return (T) new BussinessViewModel(sharedLoadingViewModel, repository); // Necesitas instanciar productUseCase
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
