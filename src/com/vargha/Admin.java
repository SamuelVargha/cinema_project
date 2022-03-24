package com.vargha;

import java.util.ArrayList;

public class Admin {
    private ArrayList<Order> orders;

    public Admin(ArrayList<Order> orders) {
        this.orders = orders;
    }

    public int getID() {
        return 1;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }
}
