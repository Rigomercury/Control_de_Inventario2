package com.example.control_de_inventario;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.control_de_inventario.adaptadores.RecyclerViewAdapterInv;
import com.example.control_de_inventario.db.InvBd;
import com.example.control_de_inventario.entidades.ArticuloInv;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivityInv extends BaseActivity implements SearchView.OnQueryTextListener{
    //private static final int SHARE_REQUEST_CODE = 123;
    RecyclerView rvItemInv;
    Button btnExportar;
    EditText etRuta;
    SearchView svConsulta;
    String user = "";
    String archivo2 = "";
    RecyclerViewAdapterInv adapterInv;
    List<ArticuloInv> articuloInvList = new ArrayList<>();


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_inv);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        rvItemInv = findViewById(R.id.rvItemInv);
        etRuta = findViewById(R.id.etRuta);
        svConsulta =findViewById(R.id.svConsulta);
        btnExportar = findViewById(R.id.btnExportar);


        leerRuta();
        leerTxtUser();

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                leerTxtUser();
            }

        } else {
            user = (String) savedInstanceState.getSerializable("USER");
        }

        rvItemInv.setLayoutManager(new GridLayoutManager(this, 1));

        ocultarBtnExportar();
        obtenerInventario();

        svConsulta.setOnQueryTextListener(this);

        etRuta.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                grabarRuta();
            }
        });

        btnExportar.setOnClickListener(v -> {
            leerRuta();
            if (etRuta.getText().toString().isEmpty()) {
                Toast.makeText(MainActivityInv.this, "INGRESAR INFORMACION DE RUTA", Toast.LENGTH_SHORT).show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivityInv.this);
                builder.setMessage("SEGURO ENVIAR ARCHIVO A CSV?")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                pedirPermisos();
                                exportarCSV();

                                //PARA EL CASO DE TABLETS COMENTAR LA FUNCION SIGUIENTE
                                //PORQUE NO TIENEN WASAP.
                                enviarWs(archivo2);
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivityInv.this, "SE ANULO EXPORTACION DE CSV", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }

        });
    }

    public void pedirPermisos() {
        if (ContextCompat.checkSelfPermission(MainActivityInv.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivityInv.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
    }

    private void checkExternalStoragePermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.i("Mensaje", "NO TIENE PERMISO PARA LEER.");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 225);
        } else {
            Log.i("Mensaje", "AUTORIZADO PARA LEER!");
        }
    }

    public void obtenerInventario() {
        articuloInvList.clear();
        InvBd invBd = new InvBd(MainActivityInv.this, "Inventariado.db", null, 1);
        SQLiteDatabase db = invBd.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM invent", null);


        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                articuloInvList.add(
                        new ArticuloInv(
                                cursor.getInt(0),
                                cursor.getString(1),
                                cursor.getString(2),
                                cursor.getString(3),
                                cursor.getString(4)
                        )
                );
            } while (cursor.moveToNext());
        } else {
            Toast.makeText(this, "NO HAY REGISTROS", Toast.LENGTH_SHORT).show();
            salir();
            cambiarActivity();
        }
        db.close();

        adapterInv = new RecyclerViewAdapterInv(MainActivityInv.this, articuloInvList);
        rvItemInv.setAdapter(adapterInv);
    }

    public void exportarCSV() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String nombreConFechaYHora = sdf.format(new Date());

        File file = new File(Environment.getExternalStorageDirectory(), "/" + etRuta.getText().toString() + "/ArchivoExportadoCSV");
        String nomFile = nombreConFechaYHora + ".csv";
        String archivo = file + "/" + nomFile;  //file.toString()
        archivo2 = nomFile;

        boolean isCreateCarpeta = false;
        if (!file.exists()) {
            isCreateCarpeta = file.mkdir();
        }
        checkExternalStoragePermission();
        try {
            FileWriter fileWriter = new FileWriter(archivo);

            InvBd invBd = new InvBd(MainActivityInv.this, "Inventariado.db", null, 1);
            SQLiteDatabase db = invBd.getWritableDatabase();

            Cursor cursor = db.rawQuery("SELECT * FROM INVENT", null);
            if (cursor != null && cursor.getCount() != 0) {
                cursor.moveToFirst();
                do {
                    fileWriter.append(cursor.getString(1));
                    fileWriter.append(",");
                    fileWriter.append(cursor.getString(2));
                    fileWriter.append(",");
                    fileWriter.append(cursor.getString(3));
                    fileWriter.append(",");
                    fileWriter.append(cursor.getString(4));
                    fileWriter.append("\n");
                } while (cursor.moveToNext());
            } else {
                Toast.makeText(this, "NO HAY REGISTROS", Toast.LENGTH_SHORT).show();
            }
            db.close();
            fileWriter.close();
            Toast.makeText(this, "SE CREO ARCHIVO CSV", Toast.LENGTH_SHORT).show();

            //PARA EL CASO DE TABLETS DEJAR LAS SIGUIENTES LINEAS, PORQUE DEBO ELIMINAR EL ARCHIVO AL EJECUTAR EL CSV
            //Y NO HABILITAR LA WEXPORTACIONE DE WASAP
            //invBd.eliminarBAse(MainActivityInv.this, "Inventariado.db");
            //cambiarActivity();


        } catch (Exception e) {
            Toast.makeText(MainActivityInv.this, "NO SE PUDO ENVIAR CSV, NO SE ENCONTRO LA RUTA", Toast.LENGTH_SHORT).show();
        }
    }

    private void cambiarActivity() {
        Intent intent = new Intent(MainActivityInv.this, IngresoDatos.class);
        intent.putExtra("USER", user);
        startActivity(intent);
    }

    private void ocultarBtnExportar() {
        if (user.equals("admin")) {
            btnExportar.setVisibility(View.VISIBLE);
        } else {
            btnExportar.setVisibility(View.INVISIBLE);
        }

    }

    private void grabarRuta() {
        try {
            OutputStreamWriter ruta = new OutputStreamWriter(openFileOutput("rutaCVS.txt", Context.MODE_PRIVATE));
            ruta.write(etRuta.getText().toString());
            ruta.flush();
            ruta.close();
        } catch (Exception e) {
        }
    }

    public void leerRuta() {
        try {
            BufferedReader aux = new BufferedReader(new InputStreamReader(openFileInput("rutaCVS.txt")));
            String ruta_txt = aux.readLine();
            etRuta.setText(ruta_txt);
        } catch (Exception e) {
        }
    }

    public void leerTxtUser() {
        try {
            BufferedReader aux = new BufferedReader(new InputStreamReader(openFileInput("usingresado.txt")));
            String user_txt = aux.readLine();
            user = user_txt;
        } catch (Exception e) {
            Log.e("usingresado.txt", "E");
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        adapterInv.filtrado(s);
        return false;
    }

    public void enviarWs(String archi){
        //=====campos a eliminar si no resulta para que siga funcionando el sistema

        try {
            File filenew;
            filenew = new File(Environment.getExternalStorageDirectory(), "Download/ArchivoExportadoCSV/"+ archi);
            Uri uri = FileProvider.getUriForFile(this, "com.example.Control_de_Inventario.fileprovider", filenew);

            // Crear un Intent para compartir el archivo CSV a través de WhatsApp
            Intent compartirIntent = new Intent(Intent.ACTION_SEND);
            compartirIntent.setType("text/csv");
            compartirIntent.putExtra(Intent.EXTRA_STREAM, uri);
            compartirIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            compartirIntent.setPackage("com.whatsapp"); // Establecer el paquete de WhatsApp como destinatario
            startActivityForResult(compartirIntent, 1);
            //startActivity(Intent.createChooser(compartirIntent, "Compartir archivo CSV"));


        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
        }
        //=====campos a eliminar si no resulta para que siga funcionando el sistema
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        InvBd invBd = new InvBd(MainActivityInv.this, "Inventariado.db", null, 1);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // El archivo fue compartido con éxito
                // Hacer algo aquí, como mostrar un mensaje al usuario
                Toast.makeText(this, "El Archivo se compartio con exito", Toast.LENGTH_SHORT).show();

                invBd.eliminarBAse(MainActivityInv.this, "Inventariado.db");
                cambiarActivity();

            } else if (resultCode == RESULT_CANCELED) {
                // El usuario canceló la acción de compartir
                // Hacer algo aquí, como mostrar un mensaje al usuario
                Toast.makeText(this, "El Archivo se cancelo", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @SuppressLint("")
    public void salir(){
        finishAffinity();
    }
}