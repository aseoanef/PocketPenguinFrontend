package com.example.pocketpenguin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BillsList {
    private List<Bill> bills;
    public List<Bill> getBills() {
        return bills;
    }
    public BillsList(JSONArray array) {
        bills = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject jsonElement = array.getJSONObject(i);
                Bill aBill = new Bill(jsonElement);
                bills.add(aBill);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
