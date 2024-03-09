package com.example.pocketpenguin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CarritoActividad extends AppCompatActivity {

    private Button boton_crearlista;
    private Context context = this;
    private ProgressBar progressBar;
    private ConstraintLayout mainLayout;
    private RequestQueue queue;
    static String host ="http://10.0.2.2:8000";
    private RecyclerView recyclerView;
    private ListLista lista;



    public void setLista(ListLista lista) {
        this.lista = lista;
        ListAdapter myadapter = new ListAdapter(this.lista, this);
        recyclerView.setAdapter(myadapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                ((LinearLayoutManager) recyclerView.getLayoutManager()).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        progressBar.setVisibility(View.GONE);


        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    public ListLista getLista() {
        return lista;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carrito_actividad);
        this.mainLayout = findViewById(R.id.carrito_layout);
        this.recyclerView = findViewById(R.id.recycled_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.queue = Volley.newRequestQueue(this);
        progressBar=findViewById(R.id.progressBar);
        getLists();
        boton_crearlista=findViewById(R.id.BCrearLista);
        boton_crearlista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

    }


    public void openDialog(){
        ListaDialog listaDialog = new ListaDialog();

        listaDialog.show(getSupportFragmentManager(), "dialog list");

    }
    public void getLists() throws NullPointerException {
        SharedPreferences preferences = context.getSharedPreferences("SESSIONS_APP_PREFS", MODE_PRIVATE);
        String username = preferences.getString("VALID_USERNAME", null);


        JsonArrayRequestAuthenticated request = new JsonArrayRequestAuthenticated(
                Request.Method.GET,
                host + "/lists/",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Toast.makeText(context, "Lista recibida", Toast.LENGTH_LONG).show();
                        setLista(new ListLista(response));


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

}

