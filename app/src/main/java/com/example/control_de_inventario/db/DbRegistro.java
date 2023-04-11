package com.example.control_de_inventario.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.control_de_inventario.entidades.Articulos;

import java.util.ArrayList;

public class DbRegistro extends DbHelper{

    Context context;

    public DbRegistro(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    public long insertarInv(String code, String codigo, String descripcion, String talla ){

        long id = 0;
        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();

            //Aca buscar la forma de que llame a otra clase donde esta la base completa
            values.put("code", "5");
            values.put("codigo", "fr");
            values.put("descripcion", "bg");
            values.put("talla", 17);

            id = db.insert(TABLE_CODIGOS, null, values);
        } catch(Exception ex){
            ex.toString();
        }
        return id;
    }

    public ArrayList<Articulos> mostrarCodigos(){
        DbHelper dbHelper = new DbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ArrayList<Articulos> listaRegistro = new ArrayList<>();
        Articulos articulos = null;
        Cursor cursorArticulos = null;

        cursorArticulos = db.rawQuery("SELECT * FROM " + TABLE_CODIGOS, null);

        if(cursorArticulos.moveToFirst()){
            do{
                articulos = new Articulos();
                articulos.setId(cursorArticulos.getInt(0));
                articulos.setCode(cursorArticulos.getString(1));
                articulos.setCodigo(cursorArticulos.getString(2));
                articulos.setDescripcion(cursorArticulos.getString(3));
                articulos.setTalla(cursorArticulos.getString(4));
                listaRegistro.add(articulos);
            }while (cursorArticulos.moveToNext());
        }
        cursorArticulos.close();

        return listaRegistro;
    }

    public void agregarMasivo(Articulos articulos){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("code",articulos.getCode());
        values.put("codigo",articulos.getCodigo());
        values.put("descripcion",articulos.getDescripcion());
        values.put("talla",articulos.getTalla());
        database.insert(TABLE_CODIGOS,null,values);
    }


}
