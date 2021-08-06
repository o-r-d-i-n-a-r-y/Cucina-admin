package com.faint.cucinacafeadminapp.preferences;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.faint.cucinacafeadminapp.activities.LoginActivity;
import com.faint.cucinacafeadminapp.user_class.Cafe;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "cucina_admin_app_prefs";
    private static final String KEY_ADDRESS = "key_address";
    private static final String KEY_STATE = "key_state";
    private static final String KEY_URLS = "key_urls";
    private static final String KEY_ID = "key_id";

    private static SharedPrefManager mInstance;
    private static Context ctx;

    private SharedPrefManager(Context context) {
        ctx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //this method will store the user data in shared preferences
    public void cafeLogin(Cafe cafe) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_ID, cafe.getId());
        editor.putInt(KEY_STATE, cafe.getState());
        editor.putString(KEY_ADDRESS, cafe.getAddress());
        editor.putStringSet(KEY_URLS, new HashSet<>(cafe.getUrls()));

        editor.apply();
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString(KEY_ADDRESS, null) != null;
    }

    //this method will give the logged in user
    public Cafe getCafe() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        return new Cafe(
                sharedPreferences.getInt(KEY_ID, -1),
                sharedPreferences.getInt(KEY_STATE, -10),
                sharedPreferences.getString(KEY_ADDRESS, null),
                new ArrayList<>(sharedPreferences.getStringSet(KEY_URLS, null))
        );
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        ctx.startActivity(new Intent(ctx, LoginActivity.class));
    }
}
