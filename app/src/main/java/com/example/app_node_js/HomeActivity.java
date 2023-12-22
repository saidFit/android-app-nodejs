package com.example.app_node_js;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.app_node_js.FinishedTasksFragment;
import com.example.app_node_js.HomeFragment;
import com.example.app_node_js.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

//    BottomNavigationView bottomNavigationView;
    DrawerLayout drawerLayout;
    RelativeLayout toolbar;
    NavigationView navigationView;
    FloatingActionButton fab;
    CircleImageView circleImage; // Declare the CircleImageView variable
    ImageView imageView;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        bottomNavigationView = findViewById(R.id.bottomNavigationView);
//        bottomNavigationView.setBackground(null);

        replaceFragment(new HomeFragment());

        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbarMenu);

        toolbar.findViewById(R.id.menuToolbar).setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });



        navigationView = findViewById(R.id.navigationView);

        View hdView = navigationView.getHeaderView(0);
       TextView user_name = (TextView) hdView.findViewById(R.id.usernameDrawal);
//        user_email = (TextView) hdView.findViewById(R.id.user_email);
        circleImage = (CircleImageView) hdView.findViewById(R.id.circleImageView);
        user_name.setText("said");
        circleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                setDrawerClick(item.getItemId());
                item.setChecked(true);
                drawerLayout.closeDrawers();
                return true;
            }
        });

    }


    public void openGallery (){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                // Get the selected image's input stream
                InputStream inputStream = getContentResolver().openInputStream(data.getData());

                if (inputStream != null) {
                    // Decode the input stream into a bitmap
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    if (bitmap != null) {
                        // Display the selected image using Glide
//                        Glide.with(this)
//                                .load(data.getData())
//                                .into(circleImage);

                        // Encode the selected image to base64
                        String imageBase64 = encodeImageToBase64(bitmap);
                        uploadImageToServer(HomeActivity.this,data.getData());
                        Log.d("imageF", String.valueOf(data.getData()));
                        Log.d("img", "--------"+imageBase64+"------------");
                        // Upload the base64 image to your server
                        // uploadImageToServer(imageBase64);
                    } else {
                        Log.e("Bitmap Error", "Failed to decode the selected image");
                    }

                    // Close the input stream
                    ((InputStream) inputStream).close();
                } else {
                    Log.e("InputStream Error", "Failed to open input stream for the selected image");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }




    private String encodeImageToBase64(Bitmap imageBitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
    public static final String URL = "https://node-app-test-vm47.onrender.com/auth/updateUser/652660d90e134c9db8849c52";
//    public static final String URL = "https://node-app-test-vm47.onrender.com/todo/postTodo";

    public void uploadImageToServer(Context context, Uri avatar) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("avatar", avatar);
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
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            Log.e("Volley Error", "Status Code: " + error.networkResponse.statusCode);
                            if (error.networkResponse.data != null) {
                                try {
                                    String errorMessage = new String(error.networkResponse.data, "UTF-8");
                                    Log.e("Volley Error", "Error Message: " + errorMessage);
                                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }
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

    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit();
    }

    private void setDrawerClick(int itemId) {

         if (itemId == R.id.action_home) {
            replaceFragment(new HomeFragment());
        } else if (itemId == R.id.action_finished_task) {
            replaceFragment(new FinishedTasksFragment());
        }
    }

}
