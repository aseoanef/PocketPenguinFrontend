package com.example.pocketpenguin;

import android.app.Activity;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;

import androidx.annotation.Nullable;

public class MainActivity extends AppCompatActivity {
    private Intent intent;
    private Activity activity = this;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);



    }
}

