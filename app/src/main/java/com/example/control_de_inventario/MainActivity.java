package com.example.control_de_inventario;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.control_de_inventario.db.AppDatabase;
import com.example.control_de_inventario.entidades.Articulos;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends BaseActivity {

    EditText etUsuario, etPass;
    Button btnIngresar;

    public boolean isCuentaCaducada() {
        try {
            FileInputStream fis = openFileInput("cuenta_estatus.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line = br.readLine();
            fis.close();

            if (line != null && line.equals("cuenta caducada")) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    @SuppressLint({"MissingInflatedId", "SimpleDateFormat"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int currentVersionCode = BuildConfig.VERSION_CODE; // Obtiene el versionCode de la versión actual
        int storedVersionCode = getStoredVersionCode(); // Obtiene el versionCode almacenado localmente

        if (currentVersionCode > storedVersionCode) {
            // La versión actual es más nueva que la almacenada
            // Realiza las acciones necesarias, como mostrar un mensaje al usuario
            // y actualizar el valor almacenado localmente.
            updateStoredVersionCode(currentVersionCode);
        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        etPass = findViewById(R.id.etPass);
        etUsuario = findViewById(R.id.etUsuario);
        btnIngresar = findViewById(R.id.btnIngresar);

        etPass.setText("");
        etUsuario.setText("");

        if (isCuentaCaducada()) {
            // La cuenta ha caducado, muestra un mensaje y finaliza la actividad
            Toast.makeText(this, "Caducó su cuenta. Comuníquese con el administrador", Toast.LENGTH_LONG).show();
            finish();
        }

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String v_Usu = etUsuario.getText().toString(), v_Pass = etPass.getText().toString();
                String user2 = null, pass2 = null;
                try {
                    ArrayList<String> datos = new ArrayList<>();
                    BufferedReader aux = new BufferedReader(new InputStreamReader(openFileInput("usuarios.txt")));
                    String texto;
                    while ((texto = aux.readLine()) != null) {
                        datos.add(texto);
                    }
                    user2 = datos.get(0);
                    pass2 = datos.get(1);
                } catch (Exception e) {
                    Log.e("usingresado.txt", "ERROR AL LEER ARCHIVO");
                }

                if (v_Usu.equals("admin") && v_Pass.equals("cerro5812") || v_Usu.equals(user2) && v_Pass.equals(pass2)) {
                    crearTxtUser();
                    ingresarApp();
                } else {
                    Toast.makeText(MainActivity.this, "USUARIO O CONTRASEÑA INCORRECTA", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void ingresarApp() {

        String admin = etUsuario.getText().toString();
        Intent intent = new Intent(MainActivity.this, IngresoDatos.class);
        intent.putExtra("USER", admin);
        startActivity(intent);
    }

    public void crearTxtUser() {
        try {
            OutputStreamWriter user = new OutputStreamWriter(openFileOutput("usingresado.txt", Context.MODE_PRIVATE));
            user.write(etUsuario.getText().toString());
            user.flush();
            user.close();
        } catch (Exception e) {
            Log.e("usingresado.txt", "o");
        }
    }

    private int getStoredVersionCode() {
        // Aquí debes implementar la lógica para obtener el versionCode almacenado localmente.
        // Puede ser en las preferencias compartidas (SharedPreferences), en un archivo, o en una base de datos.
        // Retorna el valor almacenado o 0 si no hay valor almacenado.
        // Ejemplo: SharedPreferences o lectura de un archivo
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("storedVersionCode", 0);
    }

    private void updateStoredVersionCode(int versionCode) {
        // Aquí debes implementar la lógica para actualizar el valor del versionCode almacenado localmente.
        // Puede ser en las preferencias compartidas (SharedPreferences), en un archivo, o en una base de datos.
        // Ejemplo: SharedPreferences o escritura en un archivo
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("storedVersionCode", versionCode);
        editor.apply();
    }

}