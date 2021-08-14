package com.faint.cucinacafeadminapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.faint.cucinacafeadminapp.R;
import com.faint.cucinacafeadminapp.preferences.VolleySingleton;
import com.faint.cucinacafeadminapp.user_class.Order;
import com.faint.cucinacafeadminapp.user_class.OrderDish;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private final ArrayList<Order> orders;

    private final int WAITING_STATE = 0;
    private final int ACTIVE_STATE = 1;
    private final int READY_STATE = 2;
    private final int DECLINED_STATE = 3;
    private final int HANDED_OVER_STATE = 4;

    private final String url = "http://192.168.1.8/cucina/changeOrderState.php";

    private Context context;

    public RecyclerAdapter(ArrayList<Order> orders) {
        this.orders = orders;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView idTV, nameTV, phoneTV, clarTV, dishesTV;
        private final ViewGroup btnGroup;
        private final Button btnAccept, btnDecline, btnReady, btnHandOver;

        public MyViewHolder(final View view) {
            super(view);

            idTV = view.findViewById(R.id.id_tv);
            nameTV = view.findViewById(R.id.name_tv);
            phoneTV = view.findViewById(R.id.phone_tv);
            clarTV = view.findViewById(R.id.clar_tv);
            dishesTV = view.findViewById(R.id.dishes_tv);

            btnGroup = view.findViewById(R.id.buttons);

            btnAccept = view.findViewById(R.id.accept);
            btnDecline = view.findViewById(R.id.decline);
            btnReady = view.findViewById(R.id.ready);
            btnHandOver = view.findViewById(R.id.handed_over);
        }
    }

    @NonNull
    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.order_list_item, parent, false);

        context = parent.getContext();

        return new MyViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder holder, int position) {

        switch (orders.get(position).getState()) {
            case WAITING_STATE:
                holder.btnGroup.setVisibility(View.VISIBLE);
                holder.btnReady.setVisibility(View.GONE);
                holder.btnHandOver.setVisibility(View.GONE);
                break;
            case ACTIVE_STATE:
                holder.btnGroup.setVisibility(View.GONE);
                holder.btnReady.setVisibility(View.VISIBLE);
                holder.btnHandOver.setVisibility(View.GONE);
                break;
            case READY_STATE:
                holder.btnGroup.setVisibility(View.GONE);
                holder.btnReady.setVisibility(View.GONE);
                holder.btnHandOver.setVisibility(View.VISIBLE);
                break;
        }

        holder.idTV.setText(String.valueOf(orders.get(position).getId()));
        holder.nameTV.setText(orders.get(position).getName());
        holder.phoneTV.setText(orders.get(position).getPhone());
        holder.clarTV.setText(orders.get(position).getClarifications());

        int size = orders.get(position).getOrderDishes().size();

        StringBuilder dishesStr = new StringBuilder();
        for(int i = 0; i < size; i++) {
            OrderDish dish = orders.get(position).getOrderDishes().get(i);

            if(i != size - 1)
                dishesStr.append(dish.getName()).append(" - ").append(dish.getAmount()).append("\n");
            else
                dishesStr.append(dish.getName()).append(" - ").append(dish.getAmount());
        }

        holder.dishesTV.setText(dishesStr);

        holder.btnAccept.setOnClickListener(view -> {
            changeOrderState(ACTIVE_STATE, orders.get(position).getId());

            orders.get(position).setState(ACTIVE_STATE);
            notifyDataSetChanged();
        });

        holder.btnDecline.setOnClickListener(view -> {
            changeOrderState(DECLINED_STATE, orders.get(position).getId());

            orders.remove(position);
            notifyDataSetChanged();
        });

        holder.btnReady.setOnClickListener(view -> {
            changeOrderState(READY_STATE, orders.get(position).getId());

            orders.get(position).setState(READY_STATE);
            notifyDataSetChanged();
        });

        holder.btnHandOver.setOnClickListener(view -> {
            changeOrderState(HANDED_OVER_STATE, orders.get(position).getId());

            orders.remove(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    private void changeOrderState(int newValue, int id) {
        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        if(response.trim().equals("SUCCESS")) {
                            Toast.makeText(context, "Состояние успешно обновлено!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(context,
                                    response, Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }


                },
                error -> Toast.makeText(context,
                        "Ошибка подключения!\nПроверьте интернет-соединение", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(id));
                params.put("state", String.valueOf(newValue));

                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }
}
