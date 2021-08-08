package com.faint.cucinacafeadminapp.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.faint.cucinacafeadminapp.R;
import com.faint.cucinacafeadminapp.preferences.SharedPrefManager;


public class CafePrefFragment extends PreferenceFragmentCompat {

    Preference logoutPref;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.main_settings);

        logoutPref = getPreferenceManager().findPreference("logout");
        logoutPref.setOnPreferenceClickListener(preference -> {
            final AlertDialog.Builder builder = new AlertDialog.Builder( requireActivity() );

            builder.setMessage( "Вы действительно хотите выйти из учётной записи кафе?" )
                    .setCancelable(true)
                    .setNegativeButton("Нет", null)
                    .setPositiveButton("Да",
                            (dialog, id) -> SharedPrefManager.getInstance(requireContext()).logout());

            final AlertDialog alert = builder.create();
            alert.show();

            return true;
        });
    }
}