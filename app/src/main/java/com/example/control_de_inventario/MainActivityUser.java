package com.example.control_de_inventario;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivityUser extends AppCompatActivity {

    EditText etUser, etPass;
    Button btnRegistrar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        etPass = findViewById(R.id.etPass);
        etUser = findViewById(R.id.etUser);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUser();
                volver();
            }
        });

    }

    private void registrarUser() {
        try {
            OutputStreamWriter user = new OutputStreamWriter(openFileOutput("usuarios.txt", Context.MODE_PRIVATE));
            user.write(etUser.getText().toString() + "\n" + etPass.getText().toString());//

            user.flush();
            user.close();

            Toast.makeText(MainActivityUser.this, "REGISTRO EXITOSO", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.e("usuarios.txt", "ERROR AL REGISTRAR USUARIO");
        }
    }

    private void volver() {
        Intent intent = new Intent(this, IngresoDatos.class);
        startActivity(intent);
        finish();
    }
}