package com.palgao.menu.modules.products.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.palgao.menu.modules.ProgressDialog.SharedLoadingViewModel;
import com.palgao.menu.modules.products.data.Product;
import com.palgao.menu.modules.products.data.ProductRepositoryImpl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsViewModel extends ViewModel {
    private final SharedLoadingViewModel sharedLoadingViewModel;
    private Socket socket;

    private final MutableLiveData<List<Product>> products = new MutableLiveData<>();
    private final ProductRepositoryImpl productRepository;

    public ProductsViewModel(SharedLoadingViewModel sharedLoadingViewModel, ProductRepositoryImpl productRepository) {
        this.sharedLoadingViewModel = sharedLoadingViewModel;
        this.productRepository = productRepository;

        try {
            socket = IO.socket("https://pc3ld10h-8080.usw3.devtunnels.ms/");
            initializeSocketListeners();
            socket.connect(); // Iniciar la conexión
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public LiveData<List<Product>> getProducts() {
        return products;
    }

    public void loadProducts() {
        productRepository.findAll().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    products.setValue(response.body());
                    sharedLoadingViewModel.setLoadingState(false);
                }
                else {
                    int a = 0;
                    sharedLoadingViewModel.setLoadingState(false);
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                // Handle failure
                sharedLoadingViewModel.setLoadingState(false);
                int a = 0;
            }
        });
    }

    private void initializeSocketListeners() {
        List<Product> productList = new ArrayList<>();
        Product product = new Product();
        // Escuchar el evento 'productsUpdated' desde el servidor
        socket.on("productsUpdated", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        if (args.length > 0) {
                            Object firstArg = args[0];
                            try {
                                if (firstArg instanceof JSONArray) {
                                    JSONArray productsArray = (JSONArray) firstArg;
                                    for (int i = 0; i < productsArray.length(); i++) {
                                        JSONObject productObject = productsArray.getJSONObject(i);
                                        product.setId(productObject.getString("_id"));
                                        product.setName(productObject.getString("name"));
                                        product.setDescription(productObject.getString("description"));
                                        product.setPrice(productObject.getDouble("price"));
                                        product.setImageUrl(productObject.getString("imageUrl"));
                                        productList.add(product);
                                    }
                                }
                                else if (args[0] instanceof JSONObject) {
                                    // Manejo de un solo producto
                                    JSONObject productObject = (JSONObject) firstArg;
                                    product.setId(productObject.getString("_id"));
                                    product.setName(productObject.getString("name"));
                                    product.setDescription(productObject.getString("description"));
                                    product.setPrice(productObject.getDouble("price"));
                                    product.setImageUrl(productObject.getString("imageUrl"));
                                    updateProduct(product.getId(), product);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                })
                .on("productCreate", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        if (args.length > 0) {
                            Object firstArg = args[0];
                            try {
                                if (firstArg instanceof JSONObject) {
                                    // Manejo de un solo producto
                                    JSONObject productObject = (JSONObject) firstArg;
                                    Product product = new Product();
                                    product.setId(productObject.getString("_id"));
                                    product.setName(productObject.getString("name"));
                                    product.setDescription(productObject.getString("description"));
                                    product.setPrice(productObject.getDouble("price"));
                                    product.setImageUrl(productObject.getString("imageUrl"));

                                    List<Product> currentProducts = products.getValue();
                                    if (currentProducts == null) {
                                        currentProducts = new ArrayList<>();
                                    }

                                    currentProducts.add(product);
                                    products.postValue(currentProducts);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                })
                .on("", args -> {

                });
    }



    @Override
    protected void onCleared() {
        super.onCleared();
        // Desconectar el socket cuando el ViewModel se destruye
        if (socket != null) {
            socket.disconnect();
            socket.off();
        }
    }

    public void updateProduct(String productId, Product updatedProduct) {
        // Obtén la lista actual de productos
        List<Product> currentProducts = products.getValue();

        if (currentProducts != null) {
            // Recorre la lista para encontrar el producto por el ID
            for (int i = 0; i < currentProducts.size(); i++) {
                Product product = currentProducts.get(i);
                // Si el ID coincide, reemplaza el producto con el actualizado
                if (product.getId().equals(productId)) {
                    currentProducts.set(i, updatedProduct);
                    break;
                }
            }

            // Usa postValue() para actualizar desde un hilo en segundo plano
            products.postValue(currentProducts);
        }
    }
}
