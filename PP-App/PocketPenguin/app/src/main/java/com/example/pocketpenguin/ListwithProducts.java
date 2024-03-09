package com.example.pocketpenguin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ListwithProducts extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private android.widget.SearchView buscador;
    private CharSequence busqueda;
    private Integer list_id;
    private RequestQueue queue;
    static String host ="http://10.0.2.2:8000";
    private Context context;
    private String nombreLista;
    private ScrollView scrollView;
    private ListViewAdapter adapter;
    private TextView textView;
    private String nombre_lista;
    private ListView list;
    private ArrayList<Products> productsList;
    private ArrayList<Products> arrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_with_products);
        context= this;
        this.queue = Volley.newRequestQueue(this);
        arrayList = new ArrayList<>();
        list = (ListView) findViewById(R.id.listview);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();


        if (bundle!=null){
            list_id = (Integer) bundle.getInt("ID-LIST");
        }
        adapter = new ListViewAdapter(this, arrayList, list_id);
        getSpecificList(list_id);



        getAllProducts();
        buscador = (SearchView) findViewById(R.id.searchView);
        buscador.setOnQueryTextListener(this);

        if (arrayList.isEmpty()) {
            list.setVisibility(View.GONE);
        } else {
            list.setVisibility(View.VISIBLE);
            list.setAdapter(adapter);
        }

    }
    public void getSpecificList(int list_id) throws NullPointerException {
        SharedPreferences preferences = context.getSharedPreferences("SESSIONS_APP_PREFS", MODE_PRIVATE);
        String username = preferences.getString("VALID_USERNAME", null);

        JsonObjectRequestAuthenticated request = new JsonObjectRequestAuthenticated(
                Request.Method.GET,
                host + "/lists/"+list_id,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(context, "Lista recibida", Toast.LENGTH_LONG).show();
                        try {
                             nombre_lista = response.getString("name");
                        }catch (JSONException e){}
                        textView=findViewById(R.id.nombreLista);
                        textView.setText(nombre_lista);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Problema recibiendo la lista", Toast.LENGTH_LONG).show();
            }
        }, context
        );

        queue.add(request);
    }


    public void getAllProducts() {
        SharedPreferences preferences = context.getSharedPreferences("SESSIONS_APP_PREFS", MODE_PRIVATE);
        String username = preferences.getString("VALID_USERNAME", null);


        JsonArrayRequestAuthenticated request = new JsonArrayRequestAuthenticated(
                Request.Method.GET,
                host + "/product",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Toast.makeText(context, "Productos recibidos", Toast.LENGTH_LONG).show();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject productJson = response.getJSONObject(i);
                                Products product = new Products(
                                        productJson.getInt("id"),
                                        productJson.getString("name"),
                                        (float) productJson.getDouble("price")
                                );
                                System.out.println("producto:"+ productJson.getString("name"));
                                arrayList.add(product);


                            } catch (JSONException e) {
                                throw new RuntimeException();
                            }


                            adapter = new ListViewAdapter(context, arrayList, list_id);
                            list.setAdapter(adapter);

                        }
                        System.out.println("lista-->"+arrayList);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Problema recibiendo los productos", Toast.LENGTH_LONG).show();
            }
        }, context
        );

        queue.add(request);

    }



    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }


    @Override
    public boolean onQueryTextChange(String newText) {
         String text = newText;
        adapter.filter(text);

        if (text.isEmpty() && adapter.isEmptyOriginalList()) {
            list.setVisibility(View.GONE);
        } else {
            list.setVisibility(View.VISIBLE);
        }

        return false;
    }

}
