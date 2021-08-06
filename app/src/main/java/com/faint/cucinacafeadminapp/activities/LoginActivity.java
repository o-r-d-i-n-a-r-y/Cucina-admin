package com.faint.cucinacafeadminapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.faint.cucinacafeadminapp.R;
import com.faint.cucinacafeadminapp.preferences.SharedPrefManager;
import com.faint.cucinacafeadminapp.preferences.VolleySingleton;
import com.faint.cucinacafeadminapp.user_class.Cafe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText etID, etPassword;
    final String url = "http://192.168.1.8/cucina/cafeLoginAdm.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        etID = findViewById(R.id.id_et);
        etPassword = findViewById(R.id.pass_et);

        findViewById(R.id.loginBtn).setOnClickListener(view -> userLogin());
    }

    public void userLogin() {
        final String id = etID.getText().toString();
        final String password = etPassword.getText().toString();

        if (TextUtils.isEmpty(id)) {
            etID.setError("Please enter your username");
            etID.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Please enter your password");
            etPassword.requestFocus();
            return;
        }

        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        //converting response to json object
                        JSONObject obj = new JSONObject(response);

                        //if no error in response
                        if (!obj.getBoolean("error")) {
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                            //getting the user from the response
                            JSONObject cafeJson = obj.getJSONObject("cafe");

                            JSONArray jsonArray = new JSONArray(cafeJson.getString("img_urls"));
                            ArrayList<String> urlList = new ArrayList<>();

                            for (int j = 0; j < jsonArray.length(); j++) {
                                urlList.add(jsonArray.getString(j));
                            }

                            //creating a new user object
                            Cafe cafe = new Cafe(
                                    cafeJson.getInt("id"),
                                    cafeJson.getInt("state"),
                                    cafeJson.getString("address"),
                                    urlList
                            );

                            //storing the user in shared preferences
                            SharedPrefManager.getInstance(getApplicationContext()).cafeLogin(cafe);
                            //starting the profile activity
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                        else {
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("password", password);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}