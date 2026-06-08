package com.example.juliecreations;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.juliecreations.api.ApiClient;
import com.example.juliecreations.model.CartItem;
import com.example.juliecreations.model.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView ivImage;
    private TextView tvTitle, tvPrice, tvCategory, tvDescription, tvPersonalizable;
    private Button btnAddToCart, btnBack;
    private EditText etInstructions;
    private int productId;
    private Product currentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        ivImage = findViewById(R.id.ivProductDetailImage);
        tvTitle = findViewById(R.id.tvProductDetailTitle);
        tvPrice = findViewById(R.id.tvProductDetailPrice);
        tvCategory = findViewById(R.id.tvProductDetailCategory);
        tvDescription = findViewById(R.id.tvProductDetailDescription);
        tvPersonalizable = findViewById(R.id.tvProductPersonalizable);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnBack = findViewById(R.id.btnBack);
        etInstructions = findViewById(R.id.etInstructions);

        productId = getIntent().getIntExtra("product_id", -1);

        btnBack.setOnClickListener(v -> finish());
        btnAddToCart.setOnClickListener(v -> addToCart());

        loadProductDetail();
    }

    private void loadProductDetail() {
        ApiClient.getApi().getProductById(productId).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Product p = response.body();
                    currentProduct = p;

                    tvTitle.setText(p.getTitle());
                    tvPrice.setText(p.getPrice() + " €");
                    tvCategory.setText("Catégorie : " + p.getCategory());
                    tvDescription.setText(p.getDescription());
                    tvPersonalizable.setText(p.isPersonalizable() ? "✅ Personnalisable" : "❌ Non personnalisable");

                    if (p.isPersonalizable()) {
                        etInstructions.setVisibility(android.view.View.VISIBLE);
                    } else {
                        etInstructions.setVisibility(android.view.View.GONE);
                    }

                    String imageUrl = "http://192.168.0.65:8000/uploads/products/" + p.getImageName();
                    Glide.with(ProductDetailActivity.this)
                            .load(imageUrl)
                            .placeholder(R.drawable.ic_launcher_background)
                            .into(ivImage);
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Erreur de chargement", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Toast.makeText(ProductDetailActivity.this, "Erreur réseau", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addToCart() {
        String instructions = etInstructions.getText().toString();

        SharedPreferences prefs = getSharedPreferences("JulieCreations", MODE_PRIVATE);
        String cartJson = prefs.getString("cart", "[]");

        Gson gson = new Gson();
        Type type = new TypeToken<List<CartItem>>(){}.getType();
        List<CartItem> cart = gson.fromJson(cartJson, type);

        if (cart == null) {
            cart = new ArrayList<>();
        }

        // Vérifier si le produit existe déjà
        boolean found = false;
        for (CartItem item : cart) {
            if (item.getProductId() == productId) {
                item.setQuantity(item.getQuantity() + 1);
                found = true;
                break;
            }
        }

        // Si pas trouvé, ajouter un nouvel item
        if (!found) {
            Product p = currentProduct;
            if (p != null) {
                cart.add(new CartItem(productId, p.getTitle(), p.getPrice(), 1, instructions));
            }
        }

        // Sauvegarder
        prefs.edit().putString("cart", gson.toJson(cart)).apply();

        Toast.makeText(this, "✅ Produit ajouté au panier !", Toast.LENGTH_SHORT).show();
    }
}