package com.example.control_de_inventario;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.control_de_inventario.db.DbHelper;
import com.example.control_de_inventario.db.DbRegistro;

public class NuevoActivity extends AppCompatActivity {

    EditText txtCode,txtCodigo,txtDescripcion, txtTalla;
    Button btnGuardar, btnBuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo);

        txtCode = findViewById(R.id.txtCode);
        txtCodigo = findViewById(R.id.txtCodigo);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        txtTalla = findViewById(R.id.txtTalla);
        btnGuardar = findViewById(R.id.btnGuardar);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbRegistro dbRegistro = new DbRegistro(NuevoActivity.this);
                long id = dbRegistro.insertarInv(txtCode.getText().toString(),txtCodigo.getText().toString(),txtDescripcion.getText().toString(),txtTalla.getText().toString());

                if(id > 0){
                    Toast.makeText(NuevoActivity.this,"REGISTRO GUARDADO", Toast.LENGTH_LONG).show();
                    limpiar();
                }else{
                    Toast.makeText(NuevoActivity.this,"ERROR AL GUARDAR REGISTRO", Toast.LENGTH_LONG).show();
                }
            }
        });

        txtCode.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (txtCode.getText().toString().isEmpty()){
                    Toast.makeText(NuevoActivity.this,"Nada de Nada",Toast.LENGTH_LONG).show();
                }else{
                    buscar();
                }
            }
        });
    }
    public void buscar() {
        DbHelper dbHelper = new DbHelper(NuevoActivity.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM codigos WHERE code="+ "'" +txtCode.getText().toString()+"'" , null);

        if(cursor.moveToFirst()){

            txtCodigo.setText(cursor.getString(2));
            txtDescripcion.setText(cursor.getString(3));
            txtTalla.setText(cursor.getString(4));
        }
        cursor.close();
        db.close();
    }

    private void limpiar(){
        txtCode.setText("");
        txtCodigo.setText("");
        txtDescripcion.setText("");
        txtTalla.setText("");
    }
}