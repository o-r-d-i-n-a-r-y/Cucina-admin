package com.faint.cucinacafeadminapp.user_class;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Cafe implements Parcelable {

    private final int id;
    private int state;
    private final String address;
    private ArrayList<String> urls;

    public Cafe(int id, int state, String address, ArrayList<String> urls) {
        this.id = id;
        this.state = state;
        this.address = address;
        this.urls = urls;
    }

    @SuppressWarnings("unchecked")
    protected Cafe(Parcel in) {
        id = in.readInt();
        state = in.readInt();
        address = in.readString();
        urls = in.readArrayList(String.class.getClassLoader());
    }

    public static final Creator<Cafe> CREATOR = new Creator<Cafe>() {
        @Override
        public Cafe createFromParcel(Parcel in) {
            return new Cafe(in);
        }

        @Override
        public Cafe[] newArray(int size) {
            return new Cafe[size];
        }
    };

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

    public void setUrls(ArrayList<String> urls) {
        this.urls = urls;
    }

    public ArrayList<String> getUrls() {
        return urls;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(state);
        parcel.writeString(address);
        parcel.writeList(urls);
    }
}
