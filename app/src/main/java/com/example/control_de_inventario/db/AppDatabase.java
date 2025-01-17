package com.example.control_de_inventario.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.control_de_inventario.entidades.Articulos;
import com.example.control_de_inventario.entidades.DaoArticulos;

@Database(
        entities = {Articulos.class},
        version = 1
)

public abstract class AppDatabase extends RoomDatabase {
    public abstract DaoArticulos daoArticulos();
}
