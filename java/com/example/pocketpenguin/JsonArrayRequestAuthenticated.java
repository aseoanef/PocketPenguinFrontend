package com.example.pocketpenguin;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.p2p.WifiP2pManager;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JsonArrayRequestAuthenticated extends JsonArrayRequest {
    private Context context;

    public JsonArrayRequestAuthenticated (int method, String url, @Nullable JSONArray jsonRequest,
                                          Response.Listener<JSONArray> listener,
                                          @Nullable Response.ErrorListener errorListener,
                                          Context context) {
        super(method, url, jsonRequest, listener, errorListener);
        this.context = context;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError, NullPointerException {
        SharedPreferences preferences = context.getSharedPreferences("SESSIONS_APP_PREFS", Context.MODE_PRIVATE);
        String sessionToken = preferences.getString("VALID_TOKEN", null);
        if (sessionToken == null) {
            throw new AuthFailureError();
        }
        HashMap<String, String> myHeaders = new HashMap<>();
        myHeaders.put("sessionToken", sessionToken);
        return myHeaders;

    }


}
