package com.example.pocketpenguin;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.*;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


public class ChatActivity extends AppCompatActivity {
    private Button sendbutton;
    private ProgressBar progressBar;
    private EditText textinputChat;
    private MessageList messages;
    private Context context=this;

    private RecyclerView recyclerView;
    private RequestQueue queue;
    private ConstraintLayout mainLayout;
    public ChatActivity() throws JSONException {
    }
    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }
    public void setMessages(MessageList messages) {
            this.messages = messages;
            MessageAdapter myAdapter = new MessageAdapter(this.messages);
            recyclerView.setAdapter(myAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            progressBar.setVisibility(View.GONE);
    }
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        this.queue = Volley.newRequestQueue(this);
        this.textinputChat=findViewById(R.id.textinput_chat);
        this.mainLayout = findViewById(R.id.chat_layout); // Lo asignamos con findViewById
        this.recyclerView = findViewById(R.id.recycler_view_chat);
        this.sendbutton=findViewById(R.id.sendbutton);
        progressBar=findViewById(R.id.progressBar);
        // Solicitamos una lista de mensajes
        requestMessageList();

        Button homeButton = findViewById(R.id.homebutton);
        homeButton.setOnClickListener(view -> {
            Toast.makeText(this, "Iniciando otra actividad...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, ActivityMain.class);
            context.startActivity(intent);
        });
        Button chatButton = findViewById(R.id.chatbutton);
        chatButton.setOnClickListener(view -> {
            Toast.makeText(this, "Iniciando otra actividad...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context,ChatActivity.class);
            context.startActivity(intent);
        });
        Button cartButton = findViewById(R.id.cartbutton);
        cartButton.setOnClickListener(view -> {
            Toast.makeText(this, "Iniciando otra actividad...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context,CarritoActividad.class);
            context.startActivity(intent);
        });
        Button profileButton = findViewById(R.id.profilebutton);
        profileButton.setOnClickListener(view -> {
            Toast.makeText(this, "Iniciando otra actividad...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context,ProfileActivity.class);
            context.startActivity(intent);
        });

        this.sendbutton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEmpty(textinputChat)==false) {
                postMesssage();}
            }
        });
    }
    public void serverHealth(){
        VolleyLog.DEBUG=true;
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                Server.name+"/product",
                null,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response){
                        // Aquí mostramos un Toast
                        // que indique "Hit OK"
                            // Parseamos la respuesta y la asignamos a nuestro atributo
                            //Toast.makeText(MainActivity.this, "Hit OK: " + responseBody, Toast.LENGTH_SHORT).show();
                            System.out.println(response);

                    }
                },new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error){
                    if (error.networkResponse == null) {
                        // Error: No se ha establecido la conexión
                        Toast.makeText(ChatActivity.this,"Connection could not be established",Toast.LENGTH_SHORT).show();
                    } else {
                        int serverCode = error.networkResponse.statusCode;
                    // Error: El servidor ha dado una respuesta de error
                        Toast.makeText(ChatActivity.this,"Server response " +serverCode,Toast.LENGTH_SHORT).show();
                    // La siguiente variable contendrá el código HTTP,
                    // por ejemplo 405, 500,...
                    }
                }
            }
        );
        // Aquí lanzaremos una petición HTTP
        this.queue.add(request);
    }
    public void postMesssage(){
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("content", textinputChat.getText().toString());
        }catch (JSONException e){
            throw new RuntimeException(e);
        }
        SharedPreferences preferences = context.getSharedPreferences("SESSIONS_APP_PREFS", MODE_PRIVATE);
        String username = preferences.getString("VALID_USERNAME", null);
        JsonObjectRequestAuthenticated request = new JsonObjectRequestAuthenticated(
                Request.Method.POST,
                Server.name + "/chat/",
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Aquí mostramos un Toast
                        // que indique "Hit OK"
                        Toast.makeText(ChatActivity.this, "okey mackey", Toast.LENGTH_SHORT).show();
                        requestMessageList();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse == null) {
                    // Error: No se ha establecido la conexión
                    Toast.makeText(ChatActivity.this, "Connection could not be established", Toast.LENGTH_SHORT).show();
                } else {
                    int serverCode = error.networkResponse.statusCode;
                    // Error: El servidor ha dado una respuesta de error
                    Toast.makeText(ChatActivity.this, "Server response " + serverCode, Toast.LENGTH_SHORT).show();

                    // La siguiente variable contendrá el código HTTP,
                    // por ejemplo 405, 500,...
                }
            }
        },context);
        // Aquí lanzaremos una petición HTTP
        this.queue.add(request);
        textinputChat.setText("");
        textinputChat.setHint("Enter message");
    }
    public void requestMessageList(){
        SharedPreferences preferences = context.getSharedPreferences("SESSIONS_APP_PREFS", MODE_PRIVATE);
        String username = preferences.getString("VALID_USERNAME", null);
        JsonArrayRequestAuthenticated request = new JsonArrayRequestAuthenticated(
                Request.Method.GET,
                Server.name + "/chat",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // que indique "Hit OK"
                        // Parseamos la respuesta y la asignamos a nuestro atributo
                        Toast.makeText(ChatActivity.this, "Hit OK: " , Toast.LENGTH_SHORT).show();
                        setMessages(new MessageList(response));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse == null) {
                    // Error: No se ha establecido la conexión
                    Toast.makeText(ChatActivity.this, "Connection could not be established", Toast.LENGTH_SHORT).show();
                } else {
                    int serverCode = error.networkResponse.statusCode;
                    // Error: El servidor ha dado una respuesta de error
                    Toast.makeText(ChatActivity.this, "Server response " + serverCode, Toast.LENGTH_SHORT).show();

                    // La siguiente variable contendrá el código HTTP,
                    // por ejemplo 405, 500,...
                }
            }
        },context);queue.add(request);}

}