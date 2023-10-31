package com.example.control_de_inventario.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.control_de_inventario.entidades.ArticuloInv;

import java.util.ArrayList;
import java.util.List;

public class InvBd extends SQLiteOpenHelper {


    public InvBd(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE invent(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "codigo TEXT, " +
                "descripcion TEXT," +
                "talla TEXT," +
                "stock INTEGER" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public long agregarDatosInv(String codigo, String descripcion, String talla, int stock) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues valores = new ContentValues();

        valores.put("codigo", codigo);
        valores.put("descripcion", descripcion);
        valores.put("talla", talla);
        valores.put("stock", stock);

        return db.insert("invent", null, valores);
    }

    public List<ArticuloInv> mostrarInven() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM invent", null);
        List<ArticuloInv> articuloInvs = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                articuloInvs.add(
                        new ArticuloInv(cursor.getInt(0),
                                cursor.getString(1),
                                cursor.getString(2),
                                cursor.getString(3),
                                cursor.getString(4)
                        ));
            } while (cursor.moveToNext());
        }
        return articuloInvs;
    }

    public ArticuloInv verInven(int id) {
        SQLiteDatabase db = getReadableDatabase();
        ArticuloInv articuloInvs = null;
        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM invent WHERE id = " + id + " LIMIT 1", null);
        if (cursor.moveToFirst()) {
            articuloInvs = new ArticuloInv();
            articuloInvs.setCodigo(cursor.getString(1));
            articuloInvs.setDescripcion(cursor.getString(2));
            articuloInvs.setTalla(cursor.getString(3));
            articuloInvs.setStock(cursor.getString(4));
        }
        cursor.close();
        return articuloInvs;
    }

    public boolean editarDatosInv(int id, String stock) {
        boolean correcto = false;

        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL("UPDATE invent SET stock = '" + stock + "' WHERE id= '" + id + "'");
            correcto = true;
        } catch (Exception e) {
            e.toString();
            correcto = false;
        } finally {
            db.close();
        }
        return correcto;
    }

    public boolean eliminarDatosInv(int id) {
        boolean correcto = false;
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.execSQL("DELETE FROM invent WHERE id = '" + id + "'");
            correcto = true;
        } catch (Exception e) {
            e.toString();
            correcto = false;
        } finally {
            db.close();
        }
        return correcto;
    }

    public boolean eliminarBAse(Context context, String base) {
        SQLiteDatabase db = getWritableDatabase();
        context.deleteDatabase(base);
        db.close();
        return true;
    }

}
