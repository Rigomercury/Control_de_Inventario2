package com.example.control_de_inventario;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.control_de_inventario.db.InvBd;
import com.example.control_de_inventario.entidades.ArticuloInv;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class VerActivity extends AppCompatActivity {

    EditText txtStock;
    TextView txtCodigo, txtDescripcion, txtTalla;
    Button btnGuardar;
    FloatingActionButton fabEditar, fabEliminar;
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
        fabEditar = findViewById(R.id.fabEditar);
        fabEliminar = findViewById(R.id.fabEliminar);
        btnGuardar = findViewById(R.id.btnGuardar);

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
        InvBd invBd = new InvBd(VerActivity.this, "Inventariado.db", null, 1);
        articuloInv = invBd.verInven(id);

        if (articuloInv != null) {
            txtCodigo.setText(articuloInv.getCodigo());
            txtDescripcion.setText(articuloInv.getDescripcion());
            txtTalla.setText(articuloInv.getTalla());
            txtStock.setText(articuloInv.getStock());
            btnGuardar.setVisibility(View.INVISIBLE);
            txtCodigo.setInputType(InputType.TYPE_NULL);
            txtDescripcion.setInputType(InputType.TYPE_NULL);
            txtTalla.setInputType(InputType.TYPE_NULL);
            txtStock.setInputType(InputType.TYPE_NULL);

        }
        fabEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VerActivity.this, EditarActivity.class);
                intent.putExtra("ID", id);
                startActivity(intent);
            }
        });
        fabEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(VerActivity.this);
                builder.setMessage("DESEA ELIMINAR REGISTRO?")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (invBd.eliminarDatosInv(id)) {
                                    lista();
                                }
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });
    }

    private void lista() {
        Intent intent = new Intent(this, MainActivityInv.class);
        startActivity(intent);
    }
}