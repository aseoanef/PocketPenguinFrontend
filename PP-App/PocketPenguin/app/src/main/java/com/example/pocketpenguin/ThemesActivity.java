package com.example.pocketpenguin;

import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class ThemesActivity extends AppCompatActivity {
    private Button color1;
    private Button color2;
    private Button color3;
    private Button color4;
    private Button modoOscuro;
    private Button modoClaro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.themes_activity);
        color1=findViewById(R.id.color1);
        color2=findViewById(R.id.color2);
        color3=findViewById(R.id.color3);
        color4=findViewById(R.id.color4);
        modoOscuro=findViewById(R.id.modoOscuro);
        modoClaro=findViewById(R.id.modoClaro);

        modoOscuro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                Tema.colorTema = ColorTema.BLUELIGHT;
                recreate();
            }
        });

        modoClaro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                Tema.colorTema = ColorTema.BLUEDARK;
                recreate();
            }
        });

        color1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tema.colorTema = ColorTema.YELLOW;
                recreate();

            }
        });
        color2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tema.colorTema = ColorTema.RED;
                recreate();

            }
        });
        color3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tema.colorTema = ColorTema.BLUE;
                recreate();


            }
        });
        color4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tema.colorTema = ColorTema.GREEN;
                recreate();

            }
        });

    }
    private void changeButtonColors(Button button, int color) {

        button.setBackgroundColor(color);


    }

}
