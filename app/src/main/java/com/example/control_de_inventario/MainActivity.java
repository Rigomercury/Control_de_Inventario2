package com.example.control_de_inventario;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.control_de_inventario.adaptadores.ListaRegistrosAdapter;
import com.example.control_de_inventario.db.DbHelper;
import com.example.control_de_inventario.db.DbRegistro;
import com.example.control_de_inventario.entidades.Articulos;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView listaRegistro;
    ArrayList<Articulos> listaArrayArticulos;
    //Button btnCrear;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listaRegistro = findViewById(R.id.listaRegistro);
        listaRegistro.setLayoutManager(new LinearLayoutManager(this));

        /*btnCrear = findViewById(R.id.btnCrear);
        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbHelper dbHelper = new DbHelper(MainActivity.this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                if(db!=null){
                    Toast.makeText(MainActivity.this, "Base de datos Creada", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        DbRegistro dbRegistro = new DbRegistro(MainActivity.this);

        //listaArrayArticulos = new ArrayList<>();

        ListaRegistrosAdapter adapter = new ListaRegistrosAdapter(dbRegistro.mostrarCodigos());
        listaRegistro.setAdapter(adapter);

    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menuNuevo:
                nuevoRegistro();
                return true;
            case R.id.menuActivity2:
                nuevoActivity2();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void nuevoRegistro(){
        Intent intent = new Intent(this, NuevoActivity.class);
        startActivity(intent);
    }

    private void nuevoActivity2(){
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }

}