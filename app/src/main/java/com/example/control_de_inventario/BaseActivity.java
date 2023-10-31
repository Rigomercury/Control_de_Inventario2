package com.example.control_de_inventario;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BaseActivity extends AppCompatActivity {

    private static final String DATE_TO_DISABLE_APP = "2024-04-30";
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public boolean cuentaCaducada = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkExpirationDate();
    }

    private void checkExpirationDate() {
        try {
            Date currentDate = getCurrentDateTimeFromServer();
            Date expirationDate = DATE_FORMAT.parse(DATE_TO_DISABLE_APP);

            if (currentDate.after(expirationDate)) {
                // La fecha actual es posterior a la fecha de vencimiento
                // Muestra un mensaje de advertencia y cierra la actividad
                Toast.makeText(this, "Caducó su cuenta. Comuníquese con el administrador", Toast.LENGTH_LONG).show();
                cuentaCaducada = true;

                // Crear y escribir el archivo de estado
                try {
                    FileOutputStream fos = openFileOutput("cuenta_estatus.txt", MODE_PRIVATE);
                    fos.write("cuenta caducada".getBytes());
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                finish();
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private Date getCurrentDateTimeFromServer() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://worldtimeapi.org/api/timezone/America/Santiago") // Cambia a la zona horaria de Chile
                .build();

        try {
            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            return dateFormat.parse(responseBody);
        } catch (Exception e) {
            e.printStackTrace();
            return new Date(); // Si no se pudo obtener la hora del servidor, se usa la hora actual del dispositivo.
        }
    }
    public boolean isCuentaCaducada() {
        return cuentaCaducada;
    }
}
