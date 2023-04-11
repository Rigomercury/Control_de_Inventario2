package com.example.control_de_inventario.entidades;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface DaoArticulos{

    @Query("SELECT * FROM articulos2")
    List<Articulos2> obtenerArticulos();

    @Query("SELECT * FROM articulos2 WHERE code=:code")
    List<Articulos2> obtenerArticulo(String code);

    @Insert
    void insertarArticulo(Articulos2...articulos);

    @Query("UPDATE articulos2 SET codigo= :codigo, descripcion=:descripcion, talla = :talla WHERE code=:code")
    void actualizarArticulos(String code, String codigo, String descripcion, String talla);

    @Query("DELETE FROM articulos2 WHERE code=:code")
    void borrarArticulo(String code);

    @Query("DELETE FROM articulos2")
    void borrarArticulos();

}
