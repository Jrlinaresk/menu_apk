package com.palgao.menu.modules.products.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.palgao.menu.R;
import com.palgao.menu.modules.products.data.Product;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> productList;
    private List<Product> filteredProductList;
    private ProductsViewModel productViewModel;
    private int isHorizontalLayout;
    private RecyclerView recyclerView;

    private View i_fragment_no_result;

    private Context context;
    private FragmentManager fragmentManager;

    public ProductAdapter(Context context, List<Product> productList, int isHorizontalLayout, ProductsViewModel productViewModel, RecyclerView recyclerView, View i_fragment_no_result, FragmentManager fragmentManager) {
        this.productList = productList != null ? productList : new ArrayList<>();
        this.recyclerView = recyclerView;
        this.filteredProductList = new ArrayList<>(this.productList);
        this.isHorizontalLayout = isHorizontalLayout;
        this.productViewModel = productViewModel;
        this.i_fragment_no_result = i_fragment_no_result;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }


    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == R.layout.item_producto_plano) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto_plano, parent, false);
        } else if (viewType == R.layout.item_product) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_expand, parent, false);
        }
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = filteredProductList.get(position);
        String productId = product.getId();

        holder.bind(product);
    }

    @Override
    public int getItemViewType(int position) {
        switch (isHorizontalLayout) {
            case 1:
                return R.layout.item_producto_plano;
            case 2:
                return R.layout.item_product;
            case 3:
                return R.layout.item_product_expand;
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return filteredProductList != null ? filteredProductList.size() : 0;
    }

    public void setProducts(List<Product> productList) {
        this.productList = productList != null ? productList : new ArrayList<>();
        this.filteredProductList = new ArrayList<>(this.productList);
        updateUI(productList);
        notifyDataSetChanged();
    }

    private void updateUI(List<Product> products) {
        if (products.isEmpty()) {
            showNoResultsFragment();
        } else {
            showProductListFragment();
        }
    }

    public void setLayoutType(int isHorizontalLayout) {
        this.isHorizontalLayout = isHorizontalLayout;
        notifyDataSetChanged();
    }

    public void filter(String query) {
        filteredProductList.clear();
        if (query.isEmpty()) {
            filteredProductList.addAll(productList);
        } else {
            String queryLower = query.toLowerCase();
            for (Product product : productList) {
                if (product.getName().toLowerCase().contains(queryLower)) {
                    filteredProductList.add(product);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        private final TextView productName;
        private final TextView productPrice;
        private final ImageView productImage;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productImage = itemView.findViewById(R.id.productImage);
        }

        public void bind(Product product) {
            productName.setText(product.getName());
            productPrice.setText(String.valueOf(product.getPrice()));

            String profilePictureUrl;
            profilePictureUrl = "https://pc3ld10h-8080.usw3.devtunnels.ms/uploads/" + product.getImageUrl(); //

            if (profilePictureUrl != null && !profilePictureUrl.isEmpty())
                Picasso.get()
                        .load(profilePictureUrl)
                        .placeholder(R.drawable.ic_closet) // Imagen de placeholder si la original no está disponible
                        .error(R.drawable.ic_usuario) // Imagen de error si ocurre algún problema
                        .into(productImage, new Callback() {
                            @Override
                            public void onSuccess() {
                                // Imagen cargada con éxito
                                int a = 0;
                            }

                            @Override
                            public void onError(Exception e) {
                                // Error al cargar la imagen
                                e.printStackTrace();
                            }
                        });
        }
    }

    private void showNoResultsFragment() {
        //ocultar el rv y mostrar que no se encontro elementos
        recyclerView.setVisibility(View.GONE);
        i_fragment_no_result.setVisibility(View.VISIBLE);
    }

    private void showProductListFragment() {
        recyclerView.setVisibility(View.VISIBLE);
        i_fragment_no_result.setVisibility(View.GONE);
    }
}
