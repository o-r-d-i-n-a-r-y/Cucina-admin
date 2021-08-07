package com.faint.cucinacafeadminapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.faint.cucinacafeadminapp.R;
import com.faint.cucinacafeadminapp.user_class.Order;
import com.faint.cucinacafeadminapp.user_class.OrderDish;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private final ArrayList<Order> orders;

    public RecyclerAdapter(ArrayList<Order> orders) {
        this.orders = orders;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView idTV, nameTV, phoneTV, clarTV, dishesTV;

        public MyViewHolder(final View view) {
            super(view);

            idTV = view.findViewById(R.id.id_tv);
            nameTV = view.findViewById(R.id.name_tv);
            phoneTV = view.findViewById(R.id.phone_tv);
            clarTV = view.findViewById(R.id.clar_tv);
            dishesTV = view.findViewById(R.id.dishes_tv);
        }
    }

    @NonNull
    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.order_list_item, parent, false);

        return new MyViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder holder, int position) {
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
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }
}
