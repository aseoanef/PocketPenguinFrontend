package com.example.pocketpenguin;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class Bill {
    private String title;
    private float moneyAmount;
    public float getMoneyAmount(){
        return moneyAmount;
    }
    public String getTitle() {
        return title;
    }
    public Bill(JSONObject json) throws JSONException {
        this.title = json.getString("name");
        this.moneyAmount = BigDecimal.valueOf(json.getDouble("moneyamount")).floatValue();
    }
}
