package com.faint.cucinacafeadminapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.faint.cucinacafeadminapp.R;
import com.faint.cucinacafeadminapp.preferences.SharedPrefManager;
import com.faint.cucinacafeadminapp.preferences.URLs;
import com.faint.cucinacafeadminapp.preferences.VolleySingleton;
import com.faint.cucinacafeadminapp.user_class.Cafe;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class URLChangeActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<String> urls;

    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_url_change);

        urls = getIntent().getStringArrayListExtra("URLS");
        id = SharedPrefManager.getInstance(this).getCafe().getId();

        ArrayList<EditText> fields = new ArrayList<>();
        fields.add(findViewById(R.id.ed1));
        fields.add(findViewById(R.id.ed2));
        fields.add(findViewById(R.id.ed3));
        fields.add(findViewById(R.id.ed4));
        fields.add(findViewById(R.id.ed5));

        findViewById(R.id.fab).setOnClickListener(this);

        for(int i = 0; i < urls.size(); i++) {
            fields.get(i).setText(urls.get(i));

            int finalI = i;
            TextWatcher mTextEditorWatcher = new TextWatcher() {
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    urls.set(finalI, s.toString());
                }

                public void afterTextChanged(Editable s) { }
            };

            fields.get(i).addTextChangedListener(mTextEditorWatcher);
        }
    }

    @Override
    public void onClick(View view) {
        SharedPrefManager.getInstance(this).getCafe().setUrls(urls);
        changeUrlsInDB(new JSONArray(urls).toString());
    }

    private void changeUrlsInDB(String newValue) {
        StringRequest request = new StringRequest(Request.Method.POST, URLs.URL_CHANGE_IMGs,
                response -> {
                    try {
                        if(response.trim().equals("SUCCESS")) {
                            Toast.makeText(getApplicationContext(), "Изображения успешно обновлены!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),
                                    response, Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                    SharedPrefManager.getInstance(getApplicationContext()).changeUrls(urls);
                    finish();
                },
                error -> Toast.makeText(getApplicationContext(),
                        "Ошибка подключения!\nПроверьте интернет-соединение", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("cafe_id", String.valueOf(id));
                params.put("urls", newValue);

                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }
}