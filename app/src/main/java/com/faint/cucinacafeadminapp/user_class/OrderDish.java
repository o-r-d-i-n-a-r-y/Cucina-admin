package com.faint.cucinacafeadminapp.user_class;

public class OrderDish {

    int amount;
    String name;

    public OrderDish(int amount, String name) {
        this.amount = amount;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }
}
