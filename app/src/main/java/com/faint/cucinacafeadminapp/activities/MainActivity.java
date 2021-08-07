package com.faint.cucinacafeadminapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;

import com.faint.cucinacafeadminapp.R;
import com.faint.cucinacafeadminapp.preferences.SharedPrefManager;
import com.faint.cucinacafeadminapp.user_class.Cafe;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    public static Cafe cafe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(SharedPrefManager.getInstance(this).isLoggedIn()) {
            cafe = SharedPrefManager.getInstance(this).getCafe();

            BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
            NavController navController = Navigation.findNavController(this, R.id.fragment_container);
            NavigationUI.setupWithNavController(bottomNavigationView, navController);
        }
        else {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}