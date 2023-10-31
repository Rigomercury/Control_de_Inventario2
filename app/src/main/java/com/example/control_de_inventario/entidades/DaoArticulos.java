package com.example.control_de_inventario.entidades;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;

@Dao
public interface DaoArticulos{

    @Query("SELECT * FROM Articulos")
    List<Articulos> obtenerArticulos();

    @Query("SELECT * FROM Articulos WHERE code=:code")
    List<Articulos> obtenerArticulo(String code);

    @Insert
    void insertarArticulo(Articulos...articulos);
    //@Insert
    //Completable insertarArticulo(Articulos...articulos);

    @Update
    void actualizarCargas(Articulos...articulos);

    //@Query("UPDATE Articulos SET codigo= :codigo, descripcion=:descripcion, talla = :talla WHERE code=:code")
    //void actualizarArticulos(String code, String codigo, String descripcion, String talla);

    @Query("DELETE FROM Articulos WHERE code=:code")
    void borrarArticulo(String code);

    @Query("DELETE FROM Articulos")
    void borrarArticulos();

}
