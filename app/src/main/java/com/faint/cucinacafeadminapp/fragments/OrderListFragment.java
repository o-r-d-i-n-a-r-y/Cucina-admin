package com.faint.cucinacafeadminapp.fragments;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.faint.cucinacafeadminapp.R;
import com.faint.cucinacafeadminapp.adapters.RecyclerAdapter;
import com.faint.cucinacafeadminapp.preferences.SharedPrefManager;
import com.faint.cucinacafeadminapp.preferences.VolleySingleton;
import com.faint.cucinacafeadminapp.user_class.Order;
import com.faint.cucinacafeadminapp.user_class.OrderDish;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderListFragment extends Fragment {

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;

    private ArrayList<Order> orderList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_order_list, container, false);

        recyclerView = root.findViewById(R.id.recyclerView);
        progressBar = root.findViewById(R.id.progressBar);

        orderList = new ArrayList<>();
        getOrders();

        refreshLayout = root.findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(() -> {
            getOrders();
            refreshLayout.setRefreshing(false);
        });

        return root;
    }

    private void getOrders() {
        WifiManager wifi = (WifiManager)
                requireActivity().getSystemService(Context.WIFI_SERVICE);

        if (wifi.isWifiEnabled()) {
            if(!orderList.isEmpty()) {
                orderList.clear();
            }
            else {
                progressBar.setVisibility(View.VISIBLE);
            }
        }

        String url = "http://192.168.1.8/cucina/getCafeOrders.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONArray array = new JSONArray(response);

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);

                            String jsonDishes = object.getString("order_list");
                            Gson gson = new Gson();
                            Type listType = new TypeToken< ArrayList<OrderDish> >(){}.getType();

                            ArrayList<OrderDish> dishes = gson.fromJson(jsonDishes, listType);
                            String clar = object.getString("order_clar");
                            String name = object.getString("order_name");
                            String phone = object.getString("order_phone");
                            int state = object.getInt("state");
                            int id = object.getInt("id");

                            Order order = new Order(id, name, phone, clar, dishes, state);
                            orderList.add(order);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                    progressBar.setVisibility(View.GONE);

                    RecyclerAdapter adapter = new RecyclerAdapter(orderList);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapter);

                    // here i should set adapter
                },
                error -> {
                    Toast.makeText(requireActivity(),
                            "Ошибка подключения!\nПроверьте интернет-соединение", Toast.LENGTH_SHORT).show();

                    if(orderList.isEmpty()) {
                        progressBar.setVisibility(View.GONE);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("cafe_id",
                        String.valueOf(
                                SharedPrefManager
                                    .getInstance(requireContext())
                                    .getCafe()
                                    .getId()
                        )
                );

                return params;
            }
        };

        VolleySingleton.getInstance(requireContext()).addToRequestQueue(request);
    }
}