package com.palgao.menu.modules.products.ui;

import static com.palgao.menu.tools.toolsUI.calculateNumberOfColumns;
import static com.palgao.menu.tools.toolsUI.setLayoutType;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.palgao.menu.R;
import com.palgao.menu.modules.ProgressDialog.SharedLoadingViewModel;
import com.palgao.menu.modules.products.data.Product;
import com.palgao.menu.modules.products.data.ProductRepositoryImpl;
import com.palgao.menu.modules.ui.WaitingDialog;

import java.util.ArrayList;
import java.util.List;

public class ProductsFragment extends Fragment {
    private int isHorizontalLayout = 1;
    private ProductsViewModel productsViewModel;
    private CategoryAdapter categoryAdapter;
    private RecyclerView recyclerView, rv_provincias;
    private ProductAdapter productAdapter;
    private SharedLoadingViewModel sharedLoadingViewModel;
    private WaitingDialog mWaitingDialog;
    private SearchView searchView;
    private View view;
    private View i_fragment_no_result;
    private ImageView iconLarge, iconSmall, icon_multiple;
    private TextView tv_type, tv_pre_title, tv_title_screen;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_products, container, false);
        mWaitingDialog = WaitingDialog.show(requireContext(), "Cargando Productos", true);

        InitViews();

        recyclerView = view.findViewById(R.id.recyclerViewProducts);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), calculateNumberOfColumns(R.dimen.item_width, getResources()));
        recyclerView.setLayoutManager(layoutManager);

        ProductRepositoryImpl productRepository = new ProductRepositoryImpl();
        sharedLoadingViewModel = new ViewModelProvider(requireActivity()).get(SharedLoadingViewModel.class);
        sharedLoadingViewModel.getLoadingState().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null && !isLoading) {
                new Handler(Looper.getMainLooper()).postDelayed(() -> mWaitingDialog.dismiss(), 1000);
            }
        });

        ProductsViewModelFactory factory = new ProductsViewModelFactory(sharedLoadingViewModel, productRepository);
        productsViewModel = new ViewModelProvider(this, factory).get(ProductsViewModel.class);

        productAdapter = new ProductAdapter(requireContext(), new ArrayList<>(), isHorizontalLayout, productsViewModel, recyclerView, i_fragment_no_result, getChildFragmentManager(), tv_type);

        recyclerView.setAdapter(productAdapter);

        // Observar los productos y actualizar el adaptador
        productsViewModel.getProducts().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                //
                // Actualiza el adaptador con los datos de los drivers
                if(products.size() > 0 )
                {
                    sharedLoadingViewModel.setLoadingState(false);
                }
                productAdapter.setProducts(products);

                List<String> categorias = new ArrayList<>();
                categorias.add("Todos");
                for (Product product: products) {
                    if (!categorias.contains(product.getType()))
                        categorias.add(product.getType());
                }

                categoryAdapter = new CategoryAdapter(categorias, this::onProvinceSelected, requireContext());

                tv_title_screen.setText(categorias.get(0));

                tv_pre_title.setVisibility(View.VISIBLE);
                tv_title_screen.setVisibility(View.VISIBLE);

                rv_provincias.setAdapter(categoryAdapter);
            }
            private void onProvinceSelected(String category) {
                tv_title_screen.setText(category);
                productAdapter.setProductsByType(category);
            }
        });

        // Cargar los productos
        productsViewModel.loadProducts();
        InitListeners();
        return view;
    }

    private void InitViews() {
        tv_title_screen = view.findViewById(R.id.tv_title_screen);
        tv_pre_title = view.findViewById(R.id.tv_pre_title);
        rv_provincias = view.findViewById(R.id.rv_provincias);
        searchView = view.findViewById(R.id.search_view);
        i_fragment_no_result = view.findViewById(R.id.i_fragment_no_results);
        iconLarge = view.findViewById(R.id.icon_large);
        iconSmall = view.findViewById(R.id.icon_small);
        icon_multiple = view.findViewById(R.id.icon_multiple);
        tv_type = view.findViewById(R.id.tv_type);
    }

    private void InitListeners() {
        iconLarge.setOnClickListener(v -> setLayoutType(3, recyclerView, requireContext(), productAdapter, R.dimen.item_width, getResources()));
        iconSmall.setOnClickListener(v -> setLayoutType(1, recyclerView, requireContext(), productAdapter, R.dimen.item_width, getResources()));
        icon_multiple.setOnClickListener(v -> setLayoutType(2, recyclerView, requireContext(), productAdapter, R.dimen.item_width, getResources()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                productAdapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                productAdapter.filter(newText);
                return false;
            }
        });

    }
}
