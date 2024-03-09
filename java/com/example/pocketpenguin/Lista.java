package com.example.pocketpenguin;

import org.json.JSONException;
import org.json.JSONObject;

public class Lista {
    private Integer id;
    private String name_list;
    private String family;

    public Lista(JSONObject json) throws JSONException {
        this.id = json.getInt("id");
        System.out.println(this.id);
        this.name_list = json.getString("name");
        this.family = json.getString("family");
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName_list() {
        return name_list;
    }

    public void setName_list(String name_list) {
        this.name_list = name_list;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }
}
