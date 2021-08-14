package com.faint.cucinacafeadminapp.user_class;

import java.util.ArrayList;

public class Order {

    private final int id;
    private final String name;
    private final String phone;
    private final String clarifications;
    private final ArrayList<OrderDish> orderDishes;
    private int state;

    public Order(int id, String name, String phone,
                 String clarifications, ArrayList<OrderDish> orderDishes, int state) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.clarifications = clarifications;
        this.orderDishes = orderDishes;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getClarifications() {
        return clarifications;
    }

    public ArrayList<OrderDish> getOrderDishes() {
        return orderDishes;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
