package com.example.juliecreations;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.juliecreations.model.CartItem;
import com.example.juliecreations.ui.CartAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private CartAdapter adapter;
    private TextView tvStatus, tvTotal;
    private SharedPreferences prefs;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        prefs = getSharedPreferences("JulieCreations", MODE_PRIVATE);
        gson = new Gson();

        adapter = new CartAdapter();
        RecyclerView rv = findViewById(R.id.rvCart);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        tvStatus = findViewById(R.id.tvCartStatus);
        tvTotal = findViewById(R.id.tvCartTotal);

        Button btnValidate = findViewById(R.id.btnValidateOrder);
        Button btnBack = findViewById(R.id.btnBackFromCart);

        btnBack.setOnClickListener(v -> finish());

        btnValidate.setOnClickListener(v -> {

            boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);

            if (!isLoggedIn) {

                Toast.makeText(this,
                        "Veuillez vous connecter pour valider la commande",
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(CartActivity.this, LoginActivity.class);
                startActivity(intent);

            } else {

                Toast.makeText(this,
                        "Commande validée !",
                        Toast.LENGTH_SHORT).show();

                // TODO : envoyer la commande à l’API
            }
        });

        adapter.setListener(() -> {
            saveCart();
            updateTotal();
        });

        loadCart();
    }

    private void loadCart() {
        String cartJson = prefs.getString("cart", "[]");
        Type type = new TypeToken<List<CartItem>>(){}.getType();
        List<CartItem> cart = gson.fromJson(cartJson, type);

        if (cart == null || cart.isEmpty()) {
            tvStatus.setText("Votre panier est vide");
            tvTotal.setText("Total : 0,00 €");
        } else {
            tvStatus.setText("");
            adapter.setItems(cart);
            updateTotal();
        }
    }

    private void saveCart() {
        String cartJson = gson.toJson(adapter.getItems());
        prefs.edit().putString("cart", cartJson).apply();
    }

    private void updateTotal() {
        float total = 0;
        for (CartItem item : adapter.getItems()) {
            total += item.getTotal();
        }
        tvTotal.setText(String.format("Total : %.2f €", total));
    }
}