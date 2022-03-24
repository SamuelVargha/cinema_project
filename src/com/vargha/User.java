package com.vargha;

import java.util.ArrayList;

public class User {
    private int ID;
    private ArrayList<Order> orders;

    public User(int ID, ArrayList<Order> orders) {
        this.ID = ID;
        this.orders = orders;
    }

    public int getID() {
        return ID;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }
}
