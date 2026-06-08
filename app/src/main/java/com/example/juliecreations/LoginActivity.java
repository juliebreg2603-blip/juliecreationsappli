//package com.example.juliecreations;
//
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.juliecreations.api.ApiClient;
//import com.example.juliecreations.model.LoginRequest;
//import com.example.juliecreations.model.User;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class LoginActivity extends AppCompatActivity {
//
//    private EditText etEmail, etPassword;
//    private TextView tvResult;
//    private Button btnSubmit;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setTitle("Connexion");
//        }
//
//        etEmail = findViewById(R.id.etLoginEmail);
//        etPassword = findViewById(R.id.etLoginPassword);
//        tvResult = findViewById(R.id.tvLoginResult);
//        btnSubmit = findViewById(R.id.btnLoginSubmit);
//
//        btnSubmit.setOnClickListener(v -> login());
//
//        TextView backButton = findViewById(R.id.tvBack);
//        backButton.setOnClickListener(v -> {
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        });
//    }
//
//    private void login() {
//        String email = etEmail.getText().toString().trim();
//        String password = etPassword.getText().toString().trim();
//
//        if (email.isEmpty() || password.isEmpty()) {
//            tvResult.setText("❌ Email et mot de passe requis");
//            tvResult.setTextColor(0xFFC62828);
//            return;
//        }
//
//        LoginRequest request = new LoginRequest(email, password);
//
//        ApiClient.getApi().login(request).enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    User user = response.body();
//
//                    SharedPreferences prefs = getSharedPreferences("JulieCreations", MODE_PRIVATE);
//                    prefs.edit()
//                            .putInt("user_id", user.getId())
//                            .putString("user_email", user.getEmail())
//                            .putBoolean("is_logged_in", true)
//                            .apply();
//
//                    Toast.makeText(LoginActivity.this, "✅ Connexion réussie !", Toast.LENGTH_SHORT).show();
//
//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                } else {
//                    tvResult.setText("❌ Email ou mot de passe incorrect");
//                    tvResult.setTextColor(0xFFC62828);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                tvResult.setText("❌ Erreur réseau");
//                tvResult.setTextColor(0xFFC62828);
//            }
//        });
//    }
//
//    @Override
//    public boolean onSupportNavigateUp() {
//        finish();
//        return true;
//    }
//}



package com.example.juliecreations;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.juliecreations.api.ApiClient;
import com.example.juliecreations.model.LoginRequest;
import com.example.juliecreations.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private TextView tvResult;
    private Button btnSubmit;

    private static final String TAG = "LOGIN_DEBUG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etLoginEmail);
        etPassword = findViewById(R.id.etLoginPassword);
        tvResult = findViewById(R.id.tvLoginResult);
        btnSubmit = findViewById(R.id.btnLoginSubmit);

        btnSubmit.setOnClickListener(v -> login());
    }

    private void login() {

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            tvResult.setText("❌ Email et mot de passe requis");
            return;
        }

        LoginRequest request = new LoginRequest(email, password);

        Log.d(TAG, "➡️ LOGIN REQUEST email=" + email);

        ApiClient.getApi().login(request).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                Log.d(TAG, "📡 RESPONSE CODE = " + response.code());

                if (!response.isSuccessful() || response.body() == null) {
                    tvResult.setText("❌ Email ou mot de passe incorrect");
                    return;
                }

                User user = response.body();

                Log.d(TAG, "🟢 USER RECEIVED ID = " + user.getId());

                // 🔥 IMPORTANT : validation du user id
                if (user.getId() <= 0) {
                    tvResult.setText("❌ Erreur serveur : ID utilisateur invalide");
                    return;
                }

                // 🔥 reset propre des prefs avant save
                SharedPreferences prefs = getSharedPreferences("JulieCreations", MODE_PRIVATE);
                prefs.edit().clear().apply();

                prefs.edit()
                        .putInt("user_id", user.getId())
                        .putString("user_email", user.getEmail())
                        .putBoolean("is_logged_in", true)
                        .apply();

                Log.d(TAG, "💾 SAVED USER ID = " + user.getId());

                Toast.makeText(LoginActivity.this,
                        "✅ Connexion réussie",
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                Log.e(TAG, "❌ NETWORK ERROR", t);

                tvResult.setText("❌ Erreur réseau : " + t.getMessage());
            }
        });
    }
}