package com.example.app_node_js;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Requests {

    public static void getTodosWithAuthorization(Context context,String GET_TODOS_URL,TodosCallback callback){
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        int MY_SOCKET_TIMEOUT_MS = 20000; // 20 seconds

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                GET_TODOS_URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray todos = response.getJSONArray("todos");
                            callback.onTodosReceived(todos);
                            Toast.makeText(context, "get data successfully", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error.networkResponse != null && error.networkResponse.statusCode == 401) {

                            try {
                                String errorMessage = new String(error.networkResponse.data, "UTF-8");
                                JSONObject errorJson = new JSONObject(errorMessage);
                                String message = errorJson.getString("message");
                                callback.onError(message);
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                            } catch (UnsupportedEncodingException e) {
                                throw new RuntimeException(e);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                        }else {
                            Log.e("CreateUserRequest", "Error: " + error.toString());
                            Toast.makeText(context, "An error occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        ){
            // Override this method to add custom headers
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // Replace "YOUR_AUTH_TOKEN" with your actual authorization token
                String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjY1MjY2MGQ5MGUxMzRjOWRiODg0OWM1MiIsImlhdCI6MTY5NzU0NTY5Nn0.44OD_JqQEK7iK1Y1XHZxA18CQJsUmzBC6lNfsJ-X1h8";
                headers.put("Authorization", "Bearer "+token);
                return headers;
            }
        };

        // Set a custom RetryPolicy for the request and fixe Error: com.android.volley.TimeoutError
        // If you encounter a timeoutError from the server,it means that the server did not able to response your request for the specific period you need to increase the timeOut
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,// This is the socket timeout, specified in milliseconds. It represents the maximum time that the client is willing to wait for a response from the server.
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        requestQueue.add(jsonObjectRequest);

    }

    public static void updateTodoToFinished(Context context,String POST_TODOS_URL,TodosCallback callback){
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        int MY_SOCKET_TIMEOUT_MS = 10000; // 10 seconds
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                POST_TODOS_URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject todo = response.getJSONObject("todo");
                            callback.onTodoReceived(todo);
                            Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error.networkResponse != null && error.networkResponse.statusCode == 500) {

                            try {
                                String errorMessage = new String(error.networkResponse.data, "UTF-8");
                                JSONObject errorJson = new JSONObject(errorMessage);
                                String message = errorJson.getString("message");
                                callback.onError(message);
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                            } catch (UnsupportedEncodingException e) {
                                throw new RuntimeException(e);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                        }else {
                            Log.e("CreateUserRequest", "Error: " + error.toString());
                            Toast.makeText(context, "An error occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        ){
            // Override this method to add custom headers
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // Replace "YOUR_AUTH_TOKEN" with your actual authorization token
                String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjY1MjY2MGQ5MGUxMzRjOWRiODg0OWM1MiIsImlhdCI6MTY5NzU0NTY5Nn0.44OD_JqQEK7iK1Y1XHZxA18CQJsUmzBC6lNfsJ-X1h8";
                headers.put("Authorization", "Bearer "+token);
                return headers;
            }
        };

        // Set a custom RetryPolicy for the request and fixe Error: com.android.volley.TimeoutError
        // If you encounter a timeoutError from the server,it means that the server did not able to response your request for the specific period you need to increase the timeOut
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,// This is the socket timeout, specified in milliseconds. It represents the maximum time that the client is willing to wait for a response from the server.
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        requestQueue.add(jsonObjectRequest);

    }

    public static void updateTodo(Context context,String title,String desc,String POST_TODOS_URL,TodosCallback callback){
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JSONObject jsonBody = new JSONObject();
        try{
            jsonBody.put("title",title);
            jsonBody.put("desc",desc);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        int MY_SOCKET_TIMEOUT_MS = 10000; // 10 seconds
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                POST_TODOS_URL,
                jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject updateTodo = response.getJSONObject("updateTodo");
                            callback.onTodoReceived(updateTodo);
                            Toast.makeText(context, "update todo successfully", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error.networkResponse != null && error.networkResponse.statusCode == 500) {

                            try {
                                String errorMessage = new String(error.networkResponse.data, "UTF-8");
                                JSONObject errorJson = new JSONObject(errorMessage);
                                String message = errorJson.getString("message");
                                callback.onError(message);
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                            } catch (UnsupportedEncodingException e) {
                                throw new RuntimeException(e);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                        }else {
                            Log.e("CreateUserRequest", "Error: " + error.toString());
                            Toast.makeText(context, "An error occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        ){
            // Override this method to add custom headers
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // Replace "YOUR_AUTH_TOKEN" with your actual authorization token
                String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjY1MjY2MGQ5MGUxMzRjOWRiODg0OWM1MiIsImlhdCI6MTY5NzU0NTY5Nn0.44OD_JqQEK7iK1Y1XHZxA18CQJsUmzBC6lNfsJ-X1h8";
                headers.put("Authorization", "Bearer "+token);
                return headers;
            }
        };

        // Set a custom RetryPolicy for the request and fixe Error: com.android.volley.TimeoutError
        // If you encounter a timeoutError from the server,it means that the server did not able to response your request for the specific period you need to increase the timeOut
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,// This is the socket timeout, specified in milliseconds. It represents the maximum time that the client is willing to wait for a response from the server.
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        requestQueue.add(jsonObjectRequest);

    }


    public static void deleteTodo(Context context,String POST_TODOS_URL,TodosCallback callback){
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        int MY_SOCKET_TIMEOUT_MS = 10000; // 10 seconds
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.DELETE,
                POST_TODOS_URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray todos = response.getJSONArray("todos");
                            callback.onTodosReceived(todos);
                            String message = response.getString("message");
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error.networkResponse != null && error.networkResponse.statusCode == 500) {

                            try {
                                String errorMessage = new String(error.networkResponse.data, "UTF-8");
                                JSONObject errorJson = new JSONObject(errorMessage);
                                String message = errorJson.getString("message");
                                callback.onError(message);
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                            } catch (UnsupportedEncodingException e) {
                                throw new RuntimeException(e);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                        }else {
                            Log.e("CreateUserRequest", "Error: " + error.toString());
                            Toast.makeText(context, "An error occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        ){
            // Override this method to add custom headers
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // Replace "YOUR_AUTH_TOKEN" with your actual authorization token
                String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjY1MjY2MGQ5MGUxMzRjOWRiODg0OWM1MiIsImlhdCI6MTY5NzU0NTY5Nn0.44OD_JqQEK7iK1Y1XHZxA18CQJsUmzBC6lNfsJ-X1h8";
                headers.put("Authorization", "Bearer "+token);
                return headers;
            }
        };

        // Set a custom RetryPolicy for the request and fixe Error: com.android.volley.TimeoutError
        // If you encounter a timeoutError from the server,it means that the server did not able to response your request for the specific period you need to increase the timeOut
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,// This is the socket timeout, specified in milliseconds. It represents the maximum time that the client is willing to wait for a response from the server.
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        requestQueue.add(jsonObjectRequest);

    }
}

