package com.cermati.cermatiassignment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseSearch<Model> {
    @SerializedName("total_count")
    @Expose
    private int total_count;

    @SerializedName("incomplete_results")
    @Expose
    private boolean incomplete_results;

    @SerializedName("items")
    @Expose
    private ArrayList<Model> items;

    @SerializedName("message")
    @Expose
    private String message;

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public boolean isIncomplete_results() {
        return incomplete_results;
    }

    public void setIncomplete_results(boolean incomplete_results) {
        this.incomplete_results = incomplete_results;
    }

    public ArrayList<Model> getItems() {
        return items;
    }

    public void setItems(ArrayList<Model> items) {
        this.items = items;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
