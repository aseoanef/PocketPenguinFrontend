package com.example.pocketpenguin;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import android.content.Context;


import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Inherited;

public class RegisterActivity extends AppCompatActivity {
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextEmail;
    private Button registerButton;
    private RequestQueue queue;
    private TextView bienvenida;
    private TextView datos;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.queue = Volley.newRequestQueue(this);
        setContentView(R.layout.activity_register);
        bienvenida = findViewById(R.id.Bienvenido);
        datos = findViewById(R.id.Datos);
        editTextUsername = findViewById(R.id.newUser);
        editTextPassword = findViewById(R.id.newPassword);
        editTextEmail = findViewById(R.id.correo);
        registerButton = findViewById(R.id.createUser);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(RegisterActivity.this, "Nombre: " + editTextUsername.getText().toString(), Toast.LENGTH_SHORT).show();
                sendPostRegister();
            }
        });

    }

    private void sendPostRegister()  {

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("username", editTextUsername.getText().toString());
            requestBody.put("password", editTextPassword.getText().toString());
            requestBody.put("email", editTextEmail.getText().toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                Server.name + "/user/",
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(RegisterActivity.this, "Usuario creado", Toast.LENGTH_LONG).show();
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse == null) {
                            Toast.makeText(RegisterActivity.this, "No se ha establecido la conexión", Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                int serverCode = error.networkResponse.statusCode;
                                //Toast.makeText(RegisterActivity.this, "Estado de respuesta: " + serverCode, Toast.LENGTH_LONG).show();
                                if (serverCode == 400) {
                                    Toast.makeText(RegisterActivity.this, "Alguno de los campos es incorrecto", Toast.LENGTH_SHORT).show();
                                }

                                if (serverCode == 409) {
                                    Toast.makeText(RegisterActivity.this, "Ya existe una cuenta con este correo ", Toast.LENGTH_SHORT).show();
                                }
                            }catch (NullPointerException e){}
                        }
                    }
                }
        );
        this.queue.add(request);
    }
}
