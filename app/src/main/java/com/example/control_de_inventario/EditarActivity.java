package com.example.control_de_inventario;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.control_de_inventario.db.InvBd;
import com.example.control_de_inventario.entidades.ArticuloInv;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EditarActivity extends AppCompatActivity {

    EditText txtStock;
    TextView txtCodigo, txtDescripcion, txtTalla;
    Button btnGuardar;
    FloatingActionButton fabEditar, fabEliminar;

    boolean correcto = false;

    ArticuloInv articuloInv;
    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        txtCodigo = findViewById(R.id.txtCodigo);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        txtTalla = findViewById(R.id.txtTalla);
        txtStock = findViewById(R.id.txtStock);
        btnGuardar = findViewById(R.id.btnGuardar);
        fabEditar = findViewById(R.id.fabEditar);
        fabEditar.setVisibility(View.INVISIBLE);
        fabEliminar = findViewById(R.id.fabEliminar);
        fabEliminar.setVisibility(View.INVISIBLE);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                id = Integer.parseInt(null);
            } else {
                id = extras.getInt("ID");
            }
        } else {
            id = (int) savedInstanceState.getSerializable("ID");
        }
        InvBd invBd = new InvBd(EditarActivity.this, "Inventariado.db", null, 1);
        articuloInv = invBd.verInven(id);

        if (articuloInv != null) {
            txtCodigo.setText(articuloInv.getCodigo());
            txtDescripcion.setText(articuloInv.getDescripcion());
            txtTalla.setText(articuloInv.getTalla());
            txtStock.setText(articuloInv.getStock());
        }
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!txtCodigo.getText().toString().equals("") && !txtStock.getText().toString().equals("")) {
                    correcto = invBd.editarDatosInv(id, txtStock.getText().toString());

                    if (correcto) {
                        Toast.makeText(EditarActivity.this, "REGISTRO MODIFICADO", Toast.LENGTH_SHORT).show();
                        verInicio();
                    } else {
                        Toast.makeText(EditarActivity.this, "ERROR AL MODIFICAR REGISTRO", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditarActivity.this, "DEBE LLENAR LOS CAMPOS", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void verInicio() {
        Intent intent = new Intent(EditarActivity.this, MainActivityInv.class);
        intent.putExtra("ID", id);
        startActivity(intent);
    }
}
