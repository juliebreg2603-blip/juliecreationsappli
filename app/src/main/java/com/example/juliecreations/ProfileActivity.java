package com.example.juliecreations;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.juliecreations.api.ApiClient;
import com.example.juliecreations.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    public TextView tvEmail, tvFirstName, tvLastName, tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvEmail = findViewById(R.id.tvProfileEmail);
        tvFirstName = findViewById(R.id.tvProfileFirstName);
        tvLastName = findViewById(R.id.tvProfileLastName);
        tvStatus = findViewById(R.id.tvProfileStatus);

        loadProfile();
    }
//
//    private void loadProfile() {
//        SharedPreferences prefs = getSharedPreferences("JulieCreations", MODE_PRIVATE);
//        int userId = prefs.getInt("user_id", -1);
//
//        if (userId == -1) {
//            Toast.makeText(this, "Non connecté", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        ApiClient.getApi().getProfile(userId).enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//
//                System.out.println("CODE = " + response.code());
//
//                if(response.errorBody() != null){
//                    try{
//                        System.out.println(response.errorBody().string());
//                    } catch(Exception e){
//                        e.printStackTrace();
//                    }
//                }
//
//                if (response.isSuccessful() && response.body() != null) {
//
//                    User user = response.body();
//
//                    tvEmail.setText(user.getEmail());
//                    tvFirstName.setText(user.getFirstname());
//                    tvLastName.setText(user.getLastname());
//
//                } else {
//
//                    Toast.makeText(ProfileActivity.this,
//                            "Erreur : " + response.code(),
//                            Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//
//                t.printStackTrace();
//
//                Toast.makeText(ProfileActivity.this,
//                        t.getMessage(),
//                        Toast.LENGTH_LONG).show();
//            }
//        });
//    }



    private void loadProfile() {
        SharedPreferences prefs = getSharedPreferences("JulieCreations", MODE_PRIVATE);
        int userId = prefs.getInt("user_id", -1);

        // 🔥 LOG IMPORTANT : ID USER
        System.out.println("🔵 USER_ID FROM PREFS = " + userId);

        if (userId == -1) {
            Toast.makeText(this, "Non connecté", Toast.LENGTH_SHORT).show();
            return;
        }

        // 🔥 LOG URL ATTENDUE
        String url = "http://10.0.2.2:8000/api/users/" + userId;
        System.out.println("🌐 REQUEST URL = " + url);

        ApiClient.getApi().getProfile(userId).enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                // 🔥 LOG CODE HTTP
                System.out.println("📡 RESPONSE CODE = " + response.code());

                // 🔥 LOG URL RÉELLE (Retrofit)
                System.out.println("📡 CALLED URL = " + call.request().url());

                if (response.errorBody() != null) {
                    try {
                        String error = response.errorBody().string();

                        // 🔥 LOG ERREUR COMPLETE
                        System.out.println("❌ ERROR BODY = " + error);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (response.isSuccessful() && response.body() != null) {

                    User user = response.body();

                    System.out.println("✅ USER EMAIL = " + user.getEmail());

                    tvEmail.setText(user.getEmail());
                    tvFirstName.setText(user.getFirstname());
                    tvLastName.setText(user.getLastname());

                } else {
                    Toast.makeText(ProfileActivity.this,
                            "Erreur : " + response.code(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                System.out.println("💥 ON FAILURE");
                t.printStackTrace();

                Toast.makeText(ProfileActivity.this,
                        t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
