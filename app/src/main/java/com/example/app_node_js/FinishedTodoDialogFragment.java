//package com.example.app_node_js;
//
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.Button;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.DialogFragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.Volley;
//import com.google.android.material.textfield.TextInputLayout;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.UnsupportedEncodingException;
//import java.util.HashMap;
//import java.util.Map;
//
//public class AddTodoDialogFragment extends DialogFragment {
//
//    TextInputLayout titleInput,descInput;
//    private OnTodoAddedListener todoAddedListener;
//    @NonNull
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        LayoutInflater inflater = requireActivity().getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.custom_form_alert, null);
//        builder.setView(dialogView);
//        titleInput = dialogView.findViewById(R.id.InputTitle);
//        descInput  = dialogView.findViewById(R.id.InputDesc);
//
//        // Set up form elements and actions (e.g., EditTexts, Buttons)
//
////        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
////            @Override
////            public void onClick(DialogInterface dialog, int which) {
////                // Handle the addition of a new todo
////                // You can retrieve data from the form elements here
////                // and add a new todo item to your data source
////            }
////        });
//
//        Button customButton = dialogView.findViewById(R.id.button_add);
//        customButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Handle the custom button click
//                Log.d("click", "you're add");
//                addTodo(requireContext(),titleInput.getEditText().getText().toString(),descInput.getEditText().getText().toString(),titleInput,descInput);
//            }
//        });
//
//
//        // Find the custom "Cancel" button in the dialog's view
//        Button cancelButton = dialogView.findViewById(R.id.button_cancel);
//        cancelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Handle the custom "Cancel" button click
//                dismiss(); // Close the dialog
//            }
//        });
//
//
//        return builder.create();
//    }
//
//
//    // HTTP's request;
//    public static final String URL = "https://android-app-said.onrender.com/todo/postTodo";
//
//    public void addTodo(Context context, String title, String desc,TextInputLayout titleInput,TextInputLayout descInput) {
//        RequestQueue requestQueue = Volley.newRequestQueue(context);
//
//        JSONObject jsonBody = new JSONObject();
//        try {
//            jsonBody.put("title", title);
//            jsonBody.put("desc", desc);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                Request.Method.POST,
//                URL,
//                jsonBody,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        // Handle the successful response from your server here
//                        try {
//                            String message = response.getString("message");
//                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
//                            Log.d("success", "onResponse: ");
////
//                            recyclerView = view.findViewById(R.id.todoRecyclerView);
//                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//                            todoAdapter = new TodoAdapter(); // Create your custom adapter
//
//                            retrieveAndDisplayTodos();
//
//                            recyclerView.setAdapter(todoAdapter);
//
//
//                            // Do something with the user data and token
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // Handle the error here
//                        if (error.networkResponse != null && error.networkResponse.statusCode == 401) {
//                            // User already exists
//                            try {
//                                String errorMessage = new String(error.networkResponse.data, "UTF-8");
//                                JSONObject errorJson = new JSONObject(errorMessage);
//                                String message = errorJson.getString("message");
//                                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
//                                dismiss();
//                            } catch (UnsupportedEncodingException | JSONException e) {
//                                e.printStackTrace();
//                            }
//                        } else {
//                            Log.e("CreateUserRequest", "Error: " + error.toString());
//                            Toast.makeText(requireContext(), "An error occurred", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
//        ) {
//            // Override this method to add custom headers
//            @Override
//            public Map<String, String> getHeaders() {
//                Map<String, String> headers = new HashMap<>();
//                // Replace "YOUR_AUTH_TOKEN" with your actual authorization token
//                String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjY1MjY2MGQ5MGUxMzRjOWRiODg0OWM1MiIsImlhdCI6MTY5NzU0NTY5Nn0.44OD_JqQEK7iK1Y1XHZxA18CQJsUmzBC6lNfsJ-X1h8";
//                headers.put("Authorization", "Bearer "+token);
//                return headers;
//            }
//        };
//
//        requestQueue.add(jsonObjectRequest);
//    }
//
//
//    void retrieveAndDisplayTodos() {
//        // Call the method to retrieve todos with authorization
//        Requests.getTodosWithAuthorization(requireContext(), new TodosCallback() {
//            @Override
//            public void onTodosReceived(JSONArray todos) {
//                // Parse and display the todos
//                todoAdapter.setTodos(todos);
//                Log.d("todos", String.valueOf(todos));
//            }
//
//            @Override
//            public void onError(String message) {
//                // Handle the error
//            }
//        });
//    }
//
//}









package com.example.app_node_js;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FinishedTodoDialogFragment extends DialogFragment {

    TextInputLayout titleInput, descInput;
    private OnTodoAddedListener todoAddedListener;
    static String todoId;

    public FinishedTodoDialogFragment(String todoId){
        this.todoId = todoId;
    }

    public interface OnTodoAddedListener {
        void onTodoAdded();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.finished_alert, null);
        builder.setView(dialogView);

         String URL = "https://node-app-test-vm47.onrender.com/todo/updateTodoTofinished/"+todoId;
        Log.d("log", URL);
        Button customButton = dialogView.findViewById(R.id.button_finished);
        customButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Requests.updateTodoToFinished(requireContext(), URL, new TodosCallback() {

                    @Override
                    public void onTodoReceived(JSONObject todo) {
                        if (todoAddedListener != null) {
                            todoAddedListener.onTodoAdded();
                        }

                    }

                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onTodosReceived(JSONArray todos) {
                        return;
                    }
                });
            }
        });

        Button cancelButton = dialogView.findViewById(R.id.button_cancel_finished);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return builder.create();
    }

    public static final String URL = "https://node-app-test-vm47.onrender.com/todo/postTodo";

    public void addTodo(Context context, String title, String desc) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("title", title);
            jsonBody.put("desc", desc);
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
                        try {
                            String message = response.getString("message");
                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();

                            // Notify the listener that a new todo has been added
                            if (todoAddedListener != null) {
                                todoAddedListener.onTodoAdded();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null && error.networkResponse.statusCode == 401) {
                            try {
                                String errorMessage = new String(error.networkResponse.data, "UTF-8");
                                JSONObject errorJson = new JSONObject(errorMessage);
                                String message = errorJson.getString("message");
                                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                                dismiss();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.e("AddTodoRequest", "Error: " + error.toString());
                            Toast.makeText(requireContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        )  {
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

        requestQueue.add(jsonObjectRequest);
    }

    public void setOnTodoAddedListener(HomeFragment listener) {
        this.todoAddedListener = listener;
    }

}
