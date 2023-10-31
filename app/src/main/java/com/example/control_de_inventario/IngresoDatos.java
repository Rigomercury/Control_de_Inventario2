package com.example.control_de_inventario;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.control_de_inventario.databinding.ActivityIngresodatosBinding;
import com.example.control_de_inventario.db.AppDatabase;
import com.example.control_de_inventario.db.InvBd;
import com.example.control_de_inventario.entidades.Articulos;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IngresoDatos extends BaseActivity {
    @NonNull ActivityIngresodatosBinding binding;

    EditText txtCode, txtStock;
    TextView txtCodigo, txtDescripcion, txtTalla;
    Button btnGuardar;
    ImageButton fabScanear;
    List<Articulos> listaArticulos;
    String user = "";


    RequestQueue requestQueue;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresodatos);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        binding = ActivityIngresodatosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        txtCode = findViewById(R.id.txtCode);
        txtCodigo = findViewById(R.id.txtCodigo);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        txtTalla = findViewById(R.id.txtTalla);
        txtStock = findViewById(R.id.txtStock);
        btnGuardar = findViewById(R.id.btnGuardar);
        fabScanear = findViewById(R.id.fabScanear);

        leerTxtUser();

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                leerTxtUser();
            } else {
                user = extras.getString("USER");
            }
        } else {
            user = (String) savedInstanceState.getSerializable("USER");
        }

        // Verificar si la cuenta ha caducado
        if (isCuentaCaducada()) {
            fabScanear.setEnabled(false);
        }

        binding.fabScanear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrador = new IntentIntegrator(IngresoDatos.this);
                integrador.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrador.setPrompt("Lector de barras RodrigoApp");
                integrador.setCameraId(0);
                integrador.setOrientationLocked(false);
                integrador.setBeepEnabled(false);
                integrador.setCaptureActivity(CaptureActivityPortraint.class);
                integrador.setBarcodeImageEnabled(true);
                integrador.initiateScan();
            }
        });

        pedirPermisos();

        InvBd invBd = new InvBd(this, "Inventariado.db", null, 1);
        invBd.eliminarBAse(IngresoDatos.this, "inventario.db");
        invBd.eliminarBAse(IngresoDatos.this, "toma_de_inventario.db");

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //VER MAS ADELANTE DE PODER MIGRAR A MYSQL

                //ejecutaGuardar("http://192.168.100.29/AndroidCajas/insertarInventario.php");

                if (txtCode.getText().toString().isEmpty()||
                        txtCodigo.getText().toString().isEmpty()||
                        txtDescripcion.getText().toString().isEmpty()||
                        txtTalla.getText().toString().isEmpty()||
                        txtStock.getText().toString().isEmpty()){
                    Toast.makeText(IngresoDatos.this, "DEBE LLENAR TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show();
                }else{
                    invBd.agregarDatosInv(txtCodigo.getText().toString(), txtDescripcion.getText().toString(), txtTalla.getText().toString(), Integer.parseInt(txtStock.getText().toString()));
                    limpiar();
                    txtCode.requestFocus();
                }

            }
        });

        txtCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (txtCode.getText().toString().isEmpty()) {

                } else {
                    buscar();
                    //txtStock.requestFocus();
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultcode, Intent data) {
        super.onActivityResult(requestCode, resultcode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultcode, data);
        if (intentResult.getContents() == null) {
            Toast.makeText(this, "CANCELADO", Toast.LENGTH_LONG).show();
        } else {
            txtCode.setText(intentResult.getContents());
        }
    }

    ;

    @SuppressLint("ResourceType")
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (user.equals("admin")) {
            menu.findItem(R.id.menuUsuarios).setEnabled(true);
            menu.findItem(R.id.menuActivity2).setEnabled(true);
        } else if (user.equals("")) {
            menu.findItem(R.id.menuUsuarios).setEnabled(false);
            menu.findItem(R.id.menuActivity2).setEnabled(false);
        } else {
            menu.findItem(R.id.menuUsuarios).setEnabled(false);
            menu.findItem(R.id.menuActivity2).setEnabled(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuActivity2:
                verListado();
                return true;
            case R.id.menuMostrarPistoleado:
                InvBd invBd = new InvBd(this, "Inventariado.db", null, 1);
                invBd.mostrarInven();
                verInv();
                return true;
            case R.id.menuUsuarios:
                loginUser();
                return true;
            case R.id.menuVersion:
                String versionName = BuildConfig.VERSION_NAME;
                @SuppressLint({"StringFormatInvalid", "LocalSuppress"}) String versionText = getString(R.string.app_version, versionName);
                new AlertDialog.Builder(this)
                        .setTitle(R.string.app_name)
                        .setMessage(versionText)
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
                return true;
            case R.id.menuSalir:
                salir();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void verListado() {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }

    private void loginUser() {
        Intent intent = new Intent(this, MainActivityUser.class);
        startActivity(intent);
    }

    private void verInv() {
        Intent intent = new Intent(this, MainActivityInv.class);
        intent.putExtra("USER", user);
        startActivity(intent);
    }

    private void limpiar() {
        txtCode.setText("");
        txtCodigo.setText("");
        txtDescripcion.setText("");
        txtTalla.setText("");
        txtStock.setText("");
    }

    private void buscar() {
        AppDatabase appDatabase = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                "dbClaroscuro"
        ).allowMainThreadQueries().build();

        listaArticulos = appDatabase.daoArticulos().obtenerArticulo(txtCode.getText().toString());
        String textoCodigo = "";
        String textoDescripcion = "";
        String textoTalla = "";
        for (int i = 0; i < listaArticulos.size(); i++) {
            textoCodigo = listaArticulos.get(i).codigo;
            textoDescripcion = listaArticulos.get(i).descripcion;
            textoTalla = listaArticulos.get(i).talla;
        }
        txtCodigo.setText(textoCodigo);
        txtDescripcion.setText(textoDescripcion);
        txtTalla.setText(textoTalla);
    }

    private void pedirPermisos() {
        if (ContextCompat.checkSelfPermission(IngresoDatos.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(IngresoDatos.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
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
    @SuppressLint("")
    public void salir(){
        finishAffinity();
    }
    /*private void ejecutaGuardar(String URL){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "bien", Toast.LENGTH_LONG).show();
                Toast.makeText(IngresoDatos.this, txtCode.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<>();
                parametros.put("codigo",txtCodigo.getText().toString());
                parametros.put("descripcion",txtDescripcion.getText().toString());
                parametros.put("talla",txtTalla.getText().toString());
                parametros.put("stock", txtStock.getText().toString());
                return parametros;
            }
        };
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }*/
}
