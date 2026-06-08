package com.example.juliecreations;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.juliecreations.api.ApiClient;
import com.example.juliecreations.model.HydraCollection;
import com.example.juliecreations.model.Product;
import com.example.juliecreations.ui.ProductAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.content.SharedPreferences;

public class MainActivity extends AppCompatActivity {

    private ProductAdapter adapter;
    private TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvStatus = findViewById(R.id.tvStatus);

        Button btnCart = findViewById(R.id.btnCart);
        btnCart.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        });


        Button btnLogout = findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("JulieCreations", MODE_PRIVATE);
            prefs.edit().clear().apply();

            Toast.makeText(this, "Déconnecté", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
        Button btnAccount = findViewById(R.id.btnAccount);
        btnAccount.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("JulieCreations", MODE_PRIVATE);
            boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);

            if (isLoggedIn) {
                // TODO: Ouvrir le profil
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        adapter = new ProductAdapter();
        RecyclerView rv = findViewById(R.id.rvProducts);
        rv.setLayoutManager(new GridLayoutManager(this, 2));
        rv.setAdapter(adapter);

        adapter.setListener(product -> {
            Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
            intent.putExtra("product_id", product.getId());
            startActivity(intent);
        });

        loadProducts();
    }

    private void loadProducts() {
        ApiClient.getApi().getProducts().enqueue(new Callback<HydraCollection<Product>>() {
            @Override
            public void onResponse(Call<HydraCollection<Product>> call, Response<HydraCollection<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setItems(response.body().getMember());
                    tvStatus.setText("Produits chargés avec succès !");
                } else {
                    tvStatus.setText("Erreur : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<HydraCollection<Product>> call, Throwable t) {
                tvStatus.setText("Erreur réseau : " + t.getMessage());
            }
        });
    }
}