package com.example.juliecreations;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.juliecreations.api.ApiClient;
import com.example.juliecreations.model.RegisterRequest;
import com.example.juliecreations.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText etEmail, etFirstname, etLastname, etPassword;
    private TextView tvResult, tvGoToLogin;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etEmail = findViewById(R.id.etRegisterEmail);
        etFirstname = findViewById(R.id.etRegisterFirstname);
        etLastname = findViewById(R.id.etRegisterLastname);
        etPassword = findViewById(R.id.etRegisterPassword);
        tvResult = findViewById(R.id.tvRegisterResult);
        tvGoToLogin = findViewById(R.id.tvGoToLogin);
        btnSubmit = findViewById(R.id.btnRegisterSubmit);

        btnSubmit.setOnClickListener(v -> register());

        tvGoToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void register() {
        String email = etEmail.getText().toString().trim();
        String firstname = etFirstname.getText().toString().trim();
        String lastname = etLastname.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            tvResult.setText("❌ Email et mot de passe requis");
            tvResult.setTextColor(0xFFC62828);
            return;
        }

        RegisterRequest request = new RegisterRequest(email, firstname, lastname, password);

        ApiClient.getApi().register(request).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "✅ Compte créé avec succès !", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    tvResult.setText("❌ " + response.message());
                    tvResult.setTextColor(0xFFC62828);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                tvResult.setText("❌ Erreur réseau");
                tvResult.setTextColor(0xFFC62828);
            }
        });
    }
}