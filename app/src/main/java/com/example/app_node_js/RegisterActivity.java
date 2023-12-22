package com.example.app_node_js;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    TextInputLayout username,password,email,cnfrmPassword;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        SharedPreferencesManager.clearUserData(RegisterActivity.this);
        username = findViewById(R.id.userame);
        password = findViewById(R.id.password);
        cnfrmPassword = findViewById(R.id.cnfrmPaasword);
        email    = findViewById(R.id.email);

        register = findViewById(R.id.register);

        String usernameShared = SharedPreferencesManager.getUsername(RegisterActivity.this);
        if(!usernameShared.equals("")){
            startActivity(new Intent(this,HomeActivity.class));
            return;
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createUser(RegisterActivity.this,username.getEditText().getText().toString(),email.getEditText().getText().toString(),password.getEditText().getText().toString());

            }
        });



    }


    public void launchToLogin(View v){
        startActivity(new Intent(this,LoginActivity.class));
    }

    private static final String URL = "https://node-app-test-vm47.onrender.com/auth/register"; // Replace with your server URL

    public void createUser(Context context, String username, String email, String password) {

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", username);
            jsonBody.put("email", email);
            jsonBody.put("password", password);
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
                            String token = response.getString("token");

                            Toast.makeText(RegisterActivity.this, userData.getString("username"), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                            } catch (UnsupportedEncodingException | JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.e("CreateUserRequest", "Error: " + error.toString());
                            Toast.makeText(RegisterActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
        );

        requestQueue.add(jsonObjectRequest);


    }











//    private void registerUser(View view) {
//        final String registrationUrl = "https://android-app-said.onrender.com/auth/register";
//
//        // Create a HashMap to hold your request parameters.
//        final HashMap<String, String> params = new HashMap<>();
//        params.put("username", username.getEditText().getText().toString());
//        params.put("email", email.getEditText().getText().toString());
//        params.put("password", password.getEditText().getText().toString());
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                Request.Method.POST,
//                registrationUrl,
//                new JSONObject(params),
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            JSONObject userData = response.getJSONObject("userData");
//
//                            // Now you can access the individual fields within userData
//                            String username = userData.getString("username");
//                            String email = userData.getString("email");
//                            String password = userData.getString("password");
//                            String token = response.getString("token");
//                            Log.d("username", username);
//                            // Handle the response here, e.g., save the token or navigate to the next screen.
//                            Toast.makeText(RegisterActivity.this, token, Toast.LENGTH_SHORT).show();
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        NetworkResponse response = error.networkResponse;
//                        if (error instanceof ServerError && response != null) {
//                            try {
//                                String res = new String(response.data, HttpHeaderParser.parseCharset(response.headers, "utf-8"));
//                                JSONObject obj = new JSONObject(res);
//                                String errorMessage = obj.getString("message");
//                                Log.d("message", errorMessage);
//                                // Handle the error message, e.g., show it in a toast.
//                                Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
//                            } catch (JSONException | UnsupportedEncodingException je) {
//                                je.printStackTrace();
//                            }
//                        }
//                    }
//                }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<>();
//                headers.put("Content-Type", "application/json");
//                return headers;
//            }
//        };
//
////        // Set a retry policy (optional).
////        int socketTimeout = 3000;
////        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
////                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
////        jsonObjectRequest.setRetryPolicy(policy);
//
//        // Add the request to the request queue.
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(jsonObjectRequest);
//    }



}