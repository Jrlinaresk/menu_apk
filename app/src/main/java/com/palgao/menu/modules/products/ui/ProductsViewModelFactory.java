package com.palgao.menu.modules.products.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.palgao.menu.modules.ProgressDialog.SharedLoadingViewModel;
import com.palgao.menu.modules.products.data.ProductRepositoryImpl;

public class ProductsViewModelFactory implements ViewModelProvider.Factory {
    private final SharedLoadingViewModel sharedLoadingViewModel;
    private final ProductRepositoryImpl productRepository;

    public ProductsViewModelFactory(SharedLoadingViewModel sharedLoadingViewModel, ProductRepositoryImpl productRepository) {
        this.sharedLoadingViewModel = sharedLoadingViewModel;
        this.productRepository = productRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ProductsViewModel.class)) {
            return (T) new ProductsViewModel(sharedLoadingViewModel, productRepository); // Necesitas instanciar productUseCase
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
