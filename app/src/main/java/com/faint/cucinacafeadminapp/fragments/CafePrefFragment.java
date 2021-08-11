package com.faint.cucinacafeadminapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.faint.cucinacafeadminapp.R;
import com.faint.cucinacafeadminapp.activities.URLChangeActivity;
import com.faint.cucinacafeadminapp.preferences.SharedPrefManager;
import com.faint.cucinacafeadminapp.preferences.URLs;
import com.faint.cucinacafeadminapp.preferences.VolleySingleton;

import java.util.HashMap;
import java.util.Map;


public class CafePrefFragment extends PreferenceFragmentCompat {

    private int id;
    private ListPreference statePref;
    private String[] states;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.main_settings);

        id = SharedPrefManager.getInstance(requireContext()).getCafe().getId();
        states = requireActivity().getResources().getStringArray(R.array.states_array);

        statePref = getPreferenceManager().findPreference("change_state");

        statePref.setValueIndex(SharedPrefManager.getInstance(requireActivity()).getCafe().getState());
        statePref.setSummary(states[SharedPrefManager.getInstance(requireContext()).getCafe().getState()]);

        statePref.setOnPreferenceChangeListener((preference, newValue) -> {
            if(Integer.parseInt((String) newValue) != SharedPrefManager.getInstance(requireContext()).getCafe().getState()) {
                changeStateInDB(newValue.toString());
                return true;
            }
            else
                return false;
        });

        Preference urlChange = getPreferenceManager().findPreference("change_urls");
        urlChange.setSummary("Сейчас ссылок: " + SharedPrefManager.getInstance(requireContext()).getCafe().getUrls().size());

        urlChange.setOnPreferenceClickListener(preference -> {
            Intent intent = new Intent(requireContext(), URLChangeActivity.class);
            intent.putExtra("URLS", SharedPrefManager.getInstance(requireContext()).getCafe().getUrls());
            startActivity(intent);

            return true;
        });

        Preference logoutPref = getPreferenceManager().findPreference("logout");
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

    private void changeStateInDB(String newValue) {
        StringRequest request = new StringRequest(Request.Method.POST, URLs.URL_CHANGE_STATE,
                response -> {
                    try {
                        if(response.trim().equals("SUCCESS")) {
                            Toast.makeText(requireContext(), "Состояние успешно обновлено!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(requireContext(),
                                    response, Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                    statePref.setSummary(states[Integer.parseInt(newValue)]);

                    SharedPrefManager.getInstance(requireContext()).changeState(Integer.parseInt(newValue));
                },
                error -> Toast.makeText(requireActivity(),
                        "Ошибка подключения!\nПроверьте интернет-соединение", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("cafe_id", String.valueOf(id));
                params.put("state", newValue);

                return params;
            }
        };

        VolleySingleton.getInstance(requireContext()).addToRequestQueue(request);
    }
}