package com.example.pocketpenguin;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private TextView Titulo;
    private EditText email;
    private EditText password;
    private TextView noCuenta;
    private Button registrarse;
    private Button iniciarSesion;
    private Context context = this;
    private RequestQueue queue;
    private Intent intent;
    private Activity activity = this;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_inicio);
        Titulo = findViewById(R.id.NombreAplicacion);
        email = findViewById(R.id.email);
        password = findViewById(R.id.Password);
        noCuenta = findViewById(R.id.NoTienesCuenta);
        iniciarSesion = findViewById(R.id.IniciarSesion);
        registrarse = findViewById(R.id.CrearCuenta);
        queue = Volley.newRequestQueue(this);



        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, RegisterActivity.class);
                activity.startActivity(intent);
            }
        });
       // queue = Volley.newRequestQueue(this);
        iniciarSesion.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){loginUser();}
        });
    }

    private void loginUser(){
        JSONObject requestBody = new JSONObject();
        try{
            requestBody.put("password",password.getText().toString());
            requestBody.put("email",email.getText().toString());

        }catch (JSONException e){
            throw new RuntimeException(e);
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                Server.name + "/session/",
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String receivedToken;
                        try{
                            receivedToken = response.getString("sessionToken");
                        }catch (JSONException e){
                            throw new RuntimeException(e);
                        }
                        Toast.makeText(context,"Token de sesión:" + receivedToken, Toast.LENGTH_SHORT).show();

                        Toast.makeText(context,"Iniciando sesión...", Toast.LENGTH_SHORT).show();
                        SharedPreferences preferences = context.getSharedPreferences("SESSIONS_APP_PREFS",MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("VALID_EMAIL",email.getText().toString());
                        editor.putString("VALID_TOKEN",receivedToken);
                        editor.commit();
                        Intent intent = new Intent(activity, ActivityMain.class);
                        activity.startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse == null) {
                            // Error: No se ha establecido la conexión
                            Toast.makeText(LoginActivity.this, "error", Toast.LENGTH_SHORT).show();
                        } else {
                            int serverCode = error.networkResponse.statusCode;
                           if (serverCode==404){
                               Toast.makeText(context, "Correo no válido", Toast.LENGTH_SHORT).show();
                           }

                            if (serverCode==401){
                                Toast.makeText(context, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                            }

                            if (serverCode==500){
                                Toast.makeText(context, "Problemas con el servidor", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }


                }

        );
        this.queue.add(request);
    }


}
