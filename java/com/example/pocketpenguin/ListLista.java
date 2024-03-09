package com.example.pocketpenguin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ListLista {
    private List<Lista> listas;

    public ListLista(JSONArray array) {
        listas = new ArrayList<>();
        for (int i=0;i<array.length();i++){
            try {
                JSONObject jsonElement = array.getJSONObject(i);
                Lista aLista = new Lista(jsonElement);
                listas.add(aLista);
            }catch (JSONException e){
                throw new RuntimeException(e);
            }

        }


    }

    public List<Lista> getListas() {
        return listas;
    }
}
