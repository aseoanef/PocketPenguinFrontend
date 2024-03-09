package com.example.pocketpenguin;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EditProfileActivity extends AppCompatActivity {

    private List<String> imageUrls = new ArrayList<>();
    private TextView userName;
    private RequestQueue queue;

    private Context context=this;
    static String host ="http://10.0.2.2:8000/";
    private String Username;
    private String Family;
    private ImageView profileImage;
    private String imageProfile;

    private ImageView newProfileImage;

    private String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        userName=findViewById(R.id.userName); // Inicializa userName
        context = this; // Inicializa context
        queue = Volley.newRequestQueue(this);
        getUsuario();



        SharedPreferences preferences = getSharedPreferences("user_preferences", MODE_PRIVATE);
        String username = preferences.getString("username", "Grecox");
        String profileImageUrl = preferences.getString("profileImageUrl", "https://raw.githubusercontent.com/GaelRGuerreiro/fotosPingu/main/pinguino10.jpg"); // Aquí asumimos que guardas la URL de la imagen del perfil en SharedPreferences

        userName.setText(username);

        Button submitUsernameButton = findViewById(R.id.submitUsernameButton);
        submitUsernameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText newUsernameEditText = findViewById(R.id.newUsernameEditText);
                String newUsername = newUsernameEditText.getText().toString();

                // Verifica si se ingresó un nuevo nombre de usuario
                if (!TextUtils.isEmpty(newUsername)) {
                    userName.setText(newUsername);

                    updateUsuario(newUsername, null, null); // Envía null para newFamilyCode y newProfilePicture
                } else {
                    // Muestra un mensaje de error si no se ingresó un nuevo nombre de usuario
                    Toast.makeText(EditProfileActivity.this, "Por favor, ingrese un nuevo nombre de usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button acceptButton = findViewById(R.id.acceptButton);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtiene la imagen que está en newProfileImage
                ImageView newProfileImageView = findViewById(R.id.newProfileImage);
                Drawable drawable = newProfileImageView.getDrawable();
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();


                 String imageUrl = url;

                // Realiza la actualización del perfil de usuario con la nueva imagen
                updateUsuario(null, null, imageUrl); // Envía null para newName y newFamilyCode
                loadNewImageFromUrl(url);


            }
        });

    }

    public void setImageUrl(String url){

        this.url=url;

    }
    public void showImageSelectionDialog(View v) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.image_selection_dialog);

        LinearLayout linearLayoutImages = dialog.findViewById(R.id.linearLayoutImages);

        // Limpiar el LinearLayout antes de agregar nuevas imágenes
        linearLayoutImages.removeAllViews();

        // Llamar a fetchImageUrls para obtener las URL de las imágenes
        fetchImageUrls(linearLayoutImages, dialog);

        dialog.show();
    }

    public void fetchImageUrls(final LinearLayout linearLayoutImages, final Dialog dialog) {
        InputStream inputStream = getResources().openRawResource(R.raw.imagenes);
        Scanner scanner = new Scanner(inputStream);
        StringBuilder stringBuilder = new StringBuilder();
        while (scanner.hasNextLine()) {
            stringBuilder.append(scanner.nextLine());
        }
        String jsonString = stringBuilder.toString();
        scanner.close();

        try {
            JSONArray jsonArray = new JSONArray(jsonString);


            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                final String url = jsonObject.getString("url");
                ImageView imageView = new ImageView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                imageView.setLayoutParams(layoutParams);
                // Utiliza Picasso para cargar la imagen desde la URL
                Picasso.get().load(url).into(imageView);
                // Agrega el ImageView al LinearLayout
                linearLayoutImages.addView(imageView);

                // Configura un clic en la imagen
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Cerrar el diálogo al hacer clic en la imagen
                        dialog.dismiss();
                        // Cambiar la imagen en el ImageView principal
                        loadImageFromUrl(url);
                        setImageUrl(url);
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadImageFromUrl(String url) {
        ImageView profileImageView = findViewById(R.id.newProfileImage);
        Picasso.get().load(url).into(profileImageView);
    }

    private void loadNewImageFromUrl(String url){
        ImageView profileImageView = findViewById(R.id.currentProfileImage);
        Picasso.get().load(url).into(profileImageView);

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
                            setResult(0);
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
                        userName=findViewById(R.id.userName);
                        userName.setText(Username);
                        profileImage=findViewById(R.id.currentProfileImage);
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
}