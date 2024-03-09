package com.example.pocketpenguin;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {

    private ImageView profileImage;
    private String imageProfile;
    private TextView usernameTextView;
    private TextView txtMostrarCodigoFamilia;

    private RequestQueue queue;

    private Context context=this;
    static String host ="http://10.0.2.2:8000/";

    private String Username;
    private String Family;
    private Activity activity = this;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getUsuario();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);
        Button logoutButton = findViewById(R.id.logoutButton);
        TextView txtMostrarCodigoFamilia = findViewById(R.id.txtMostrarCodigoFamilia);
        Button changeThemeButton = findViewById(R.id.editThemeButton);
        Button editProfileButton = findViewById(R.id.editProfileButton);
        Button invoicesButton = findViewById(R.id.invoicesButton);
        EditText familyCodeEditText = findViewById(R.id.familyCodeEditText);
        ImageButton btnEnviar = findViewById(R.id.sendFamilyCodeButton);
        Button deleteAccount = findViewById(R.id.deleteAccount);
        ImageButton sendFamilyCodeButton = findViewById(R.id.sendFamilyCodeButton);
        profileImage = findViewById(R.id.profileImage);
        usernameTextView = findViewById(R.id.usernameTextView);
        this.queue= Volley.newRequestQueue(ProfileActivity.this);


        SharedPreferences preferences = getSharedPreferences("user_preferences", MODE_PRIVATE);
        String username = preferences.getString("username", "Nombre de Usuario");
        String profileImageUrl = preferences.getString("profileImageUrl", "https://raw.githubusercontent.com/GaelRGuerreiro/fotosPingu/main/pinguino10.jpg"); // Aquí asumimos que guardas la URL de la imagen del perfil en SharedPreferences

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


        getUsuario();
        usernameTextView.setText(Username);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        invoicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, GraficActivity.class);
                startActivity(intent);
            }
        });
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutConfirmationDialog();
            }
        });
        sendFamilyCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para obtener y mostrar el código de familia
                String familyCode = familyCodeEditText.getText().toString();
                mostrarCodigoFamilia(familyCode);
            }
        });


        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newFamilyCode = familyCodeEditText.getText().toString();
                txtMostrarCodigoFamilia.setText(newFamilyCode);
                updateUsuario(username,newFamilyCode,profileImageUrl);
            }
        });

        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog();
            }
        });
        changeThemeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ThemesActivity.class); // Reemplaza ChangeThemeActivity con el nombre real de tu actividad de cambio de tema
                startActivity(intent);
            }
        });
    }






    //  public void getPrueba(){
    //
    //
    //      JsonObjectRequest request = new JsonObjectRequest(
    //              Request.Method.GET,
    //              com.example.pocketpenguin.Server.name + "/user",
    //              null,
    //              new Response.Listener<JSONObject>() {
    //                  @Override
    //                  public void onResponse(JSONObject response) {
    //                      Toast.makeText(ProfileActivity.this, "GET OK: Server is healthy!", Toast.LENGTH_LONG).show();
    //                  }
    //
    //
    //              }, new Response.ErrorListener() {
    //
    //          @Override
    //          public void onErrorResponse(VolleyError error) {
    //              if (error.networkResponse != null) {
    //                  int serverCode = error.networkResponse.statusCode;
    //                  Toast.makeText(ProfileActivity.this, "Server replied with " + serverCode, Toast.LENGTH_SHORT).show();
    //              } else {
    //                  Toast.makeText(ProfileActivity.this, "Unable to reach server", Toast.LENGTH_SHORT).show();
    //              }
    //          }
    //      }
    //
    //
    //      );
    //
    //      this.queue.add(request);
    //
    //  }


    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Estás seguro de que quieres cerrar sesión?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id){
                        // Eliminar el sessionToken de las preferencias compartidas
                        SharedPreferences preferences = getSharedPreferences("SESSIONS_APP_PREFS", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.remove("SESSION_TOKEN");
                        editor.apply();

                        // Iniciar la actividad de la pantalla de inicio de sesión
                        Intent intent = new Intent(activity, LoginActivity.class);
                        activity.startActivity(intent);
                        finish(); // Para cerrar la actividad actual y evitar que el usuario vuelva a ella con el botón de retroceso
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Cancela el cierre de sesión
                        dialog.dismiss();
                    }
                });
        // Crea y muestra el diálogo
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Estás seguro de que quieres Borrar tu cuenta ?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id){
                    deleteUsuario();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Cancela el cierre de sesión
                        dialog.dismiss();
                    }
                });
        // Crea y muestra el diálogo
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void mostrarCodigoFamilia(String codigo) {
        String mensaje = "Código de familia ingresado: " + codigo;
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }



    public void getUsuario() throws NullPointerException {
        SharedPreferences preferences = context.getSharedPreferences("SESSIONS_APP_PREFS", MODE_PRIVATE);
        String username = preferences.getString("VALID_USERNAME", null);

        JsonObjectRequestAuthenticated request = new JsonObjectRequestAuthenticated(
                Request.Method.GET,
                host + "user/",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(context, "Usuario recibido", Toast.LENGTH_LONG).show();
                        try {
                            Username= response.getString("username");
                            Family= response.getString("family");
                            imageProfile= response.getString("imageUrl");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        usernameTextView=findViewById(R.id.usernameTextView);
                        usernameTextView.setText(Username);
                        txtMostrarCodigoFamilia=findViewById(R.id.txtMostrarCodigoFamilia);
                        txtMostrarCodigoFamilia.setText(Family);
                        profileImage=findViewById(R.id.profileImage);
                        try {
                            Util.downloadBitmapToImageView(response.getString("imageUrl"),profileImage);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Problema recibiendo usuario", Toast.LENGTH_LONG).show();
            }
        }, context
        );

        queue.add(request);
    }



    public void deleteUsuario() throws NullPointerException {
        SharedPreferences preferences = context.getSharedPreferences("SESSIONS_APP_PREFS", MODE_PRIVATE);
        String username = preferences.getString("VALID_USERNAME", null);

        if (username != null) {

            String url = host + "user/" + username;

            JsonObjectRequestAuthenticated request = new JsonObjectRequestAuthenticated(
                    Request.Method.DELETE,
                    url,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(context, "Usuario borrado", Toast.LENGTH_LONG).show();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Problema borrando usuario", Toast.LENGTH_LONG).show();
                }
            }, context);

            queue.add(request);
        } else {
            Toast.makeText(context, "Nombre de usuario no válido", Toast.LENGTH_LONG).show();
        }
    }



    public void updateUsuario(final String newName, final String newFamilyCode, final String newProfilePicture) {
        SharedPreferences preferences = context.getSharedPreferences("SESSIONS_APP_PREFS", MODE_PRIVATE);
        String sessionToken = preferences.getString("SESSION_TOKEN", null);

        if (sessionToken == null) {
            String url = host + "user/";

            JSONObject requestBody = new JSONObject();
            try {
                requestBody.put("sessionToken", sessionToken);
                if (newName != null) {
                    requestBody.put("newName", newName);
                }
                if (newFamilyCode != null) {
                    requestBody.put("newFamilyCode", newFamilyCode);
                }
                if (newProfilePicture != null) {
                    requestBody.put("newProfilePicture", newProfilePicture);
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            JsonObjectRequestAuthenticated request = new JsonObjectRequestAuthenticated(
                    Request.Method.PUT,
                    url,
                    requestBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(context, "Usuario actualizado exitosamente", Toast.LENGTH_LONG).show();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Error al actualizar el usuario", Toast.LENGTH_LONG).show();
                }
            }, context);

            queue.add(request);
        } else {
            Toast.makeText(context, "Token de sesión no válido", Toast.LENGTH_LONG).show();
        }
    }






}
