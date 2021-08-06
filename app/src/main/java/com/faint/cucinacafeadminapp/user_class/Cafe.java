package com.faint.cucinacafeadminapp.user_class;

import java.util.ArrayList;

public class Cafe {

    private final int id;
    private int state;
    private final String address;
    private final ArrayList<String> urls;

    public Cafe(int id, int state, String address, ArrayList<String> urls) {
        this.id = id;
        this.state = state;
        this.address = address;
        this.urls = urls;
    }

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setUrl(String url, int pos) {
        urls.set(pos, url);
    }

    public ArrayList<String> getUrls() {
        return urls;
    }
}
