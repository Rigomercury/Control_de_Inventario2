package com.example.control_de_inventario.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


public class DbHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NOMBRE = "inventario.db";
    public static final String TABLE_CODIGOS = "codigos";

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NOMBRE, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_CODIGOS + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "code INTEGER NOT NULL," +
                "codigo TEXT NOT NULL," +
                "descripcion TEXT NOT NULL," +
                "talla TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_CODIGOS);
        onCreate(db);
    }

}

