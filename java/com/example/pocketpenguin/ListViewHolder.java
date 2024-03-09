package com.example.pocketpenguin;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import androidx.appcompat.app.AppCompatDelegate;


public class ListViewHolder extends RecyclerView.ViewHolder  {
    private TextView listTextView;
    private ImageButton imageButton;
    private RequestQueue queue;
    private Context context;
    static String host ="http://10.0.2.2:8000";
    private CarritoActividad actividad;
    private Integer list_id;
    private Lista lista;





    public ListViewHolder(@NonNull View itemView, CarritoActividad actividad) {
        super(itemView);
        context = itemView.getContext();
        queue = Volley.newRequestQueue(itemView.getContext());
        listTextView = itemView.findViewById(R.id.nameList);
        imageButton = itemView.findViewById(R.id.basura);
        this.actividad = actividad;


        listTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, ListwithProducts.class);
                intent.putExtra("ID-LIST", list_id);
                context.startActivity(intent);

            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                AlertDialog.Builder alerta = new AlertDialog.Builder(context);
                alerta.setMessage("¿Estás seguro que quieres borrar la lista?")
                        .setCancelable(false)
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteList(list_id);

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog titulo = alerta.create();
                titulo.setTitle("Alerta");
                titulo.show();


            }
        });

    }

    public void binData(Lista lista, CarritoActividad actividad) {
        this.listTextView.setText(lista.getName_list());
         list_id = lista.getId();
    }

    public void deleteList(Integer lista_id) throws NullPointerException {
        SharedPreferences preferences = context.getSharedPreferences("SESSIONS_APP_PREFS", MODE_PRIVATE);
        String username = preferences.getString("VALID_USERNAME", null);


        JsonObjectRequestAuthenticated request = new JsonObjectRequestAuthenticated(
                Request.Method.DELETE,
                host + "/lists/" + lista_id+"/",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(context, "Se ha borrado la lista", Toast.LENGTH_LONG).show();

                        actividad.getLists();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Ha habido un error :/", Toast.LENGTH_LONG).show();

            }
        }, context
        );
        queue.add(request);
    }


    public void getSpecificList(int position) throws NullPointerException {
        SharedPreferences preferences = context.getSharedPreferences("SESSIONS_APP_PREFS", MODE_PRIVATE);
        String username = preferences.getString("VALID_USERNAME", null);

        JsonArrayRequestAuthenticated request = new JsonArrayRequestAuthenticated(
                Request.Method.GET,
                host + "/lists/"+position,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Toast.makeText(context, "Lista recibida", Toast.LENGTH_LONG).show();


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
