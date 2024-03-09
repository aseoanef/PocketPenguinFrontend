package com.example.pocketpenguin;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ListaDialog extends AppCompatDialogFragment {
    private EditText nombreLista;
    private TextView textView;
    private String NL;
    private RequestQueue queue;
    static String host ="http://10.0.2.2:8000";
    private Context context;
    private Integer i;
    private ListAdapter listAdapter;
    private ListLista lista;
    private RecyclerView recyclerView;
    private View view;

private CarritoActividad actividad;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        context = getContext();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);
        queue = Volley.newRequestQueue(view.getContext());
        actividad = (CarritoActividad) getActivity();
        builder.setView(view)
                .setTitle("Crear lista")
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NL = nombreLista.getText().toString();
                        sendUpdateRequest(NL);

                    }
                });
        nombreLista = view.findViewById(R.id.nueva_lista);

        return builder.create();
    }


    public void sendUpdateRequest(String nombreLista) throws NullPointerException{
        SharedPreferences preferences = context.getSharedPreferences("SESSIONS_APP_PREFS", MODE_PRIVATE);
        String username = preferences.getString("VALID_USERNAME", null);

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("newListName", nombreLista);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        JsonObjectRequestAuthenticated request = new JsonObjectRequestAuthenticated(
                Request.Method.POST,
                host + "/lists/",
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(context, "Se ha enviado la lista", Toast.LENGTH_LONG).show();

                        actividad.getLists();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Ha habido un error :(", Toast.LENGTH_LONG).show();

            }
        }, context
                );
        queue.add(request);



    }





}