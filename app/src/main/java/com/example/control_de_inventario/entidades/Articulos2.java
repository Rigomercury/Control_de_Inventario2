package com.example.control_de_inventario.entidades;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Articulos2 {
    @PrimaryKey
    @NonNull
    public String code;
    public String codigo;
    public String descripcion;
    public String talla;

    public Articulos2(@NonNull String code, String codigo, String descripcion, String talla){
        this.code = code;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.talla = talla;
    }

    public String getCode() {return code;}
    public String getCodigo() {return codigo;}
    public String getDescripcion() {return descripcion;}
    public String getTalla() {return talla;}
}