package com.example.app_node_js;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout email,password,cnfrmPassword;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.emailLogin);
        password = findViewById(R.id.passwordLogin);
        cnfrmPassword = findViewById(R.id.cnfrmPaaswordLogin);

        loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(v -> {
            loginUser(this,email.getEditText().getText().toString(),password.getEditText().getText().toString());
        });

    }



    public static final String URL = "https://node-app-test-vm47.onrender.com/auth/login";
    private void loginUser (Context context,String emailValue,String passwordValue){

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", emailValue);
            jsonBody.put("password", passwordValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the successful response from your server here
                        try {
                            JSONObject userData = response.getJSONObject("userData");
                            String username = userData.getString("username");
                            String email    = userData.getString("email");
                            String avatar   = userData.getString("avatar");
                            String token = response.getString("token");

                            SharedPreferencesManager.saveUserData(LoginActivity.this,username,email,avatar,token);
                            String usernameShared = SharedPreferencesManager.getUsername(LoginActivity.this);
                            Toast.makeText(LoginActivity.this, usernameShared, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                            // Do something with the user data and token
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error here
                        if (error.networkResponse != null && error.networkResponse.statusCode == 400) {
                            // User already exists
                            try {
                                String errorMessage = new String(error.networkResponse.data, "UTF-8");
                                JSONObject errorJson = new JSONObject(errorMessage);
                                String message = errorJson.getString("message");
                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                            } catch (UnsupportedEncodingException | JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.e("CreateUserRequest", "Error: " + error.toString());
                            Toast.makeText(LoginActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
        );

        requestQueue.add(jsonObjectRequest);



    }


}