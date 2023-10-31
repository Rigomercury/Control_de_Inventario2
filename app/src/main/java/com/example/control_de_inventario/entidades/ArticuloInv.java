package com.example.control_de_inventario.entidades;

public class ArticuloInv {
    private int id;
    private String codigo, descripcion ,talla, stock;

    public ArticuloInv(String codigo, String descripcion, String talla, String stock) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.talla = talla;
        this.stock = stock;
    }
    public ArticuloInv() {
    }

    public ArticuloInv(int id, String codigo, String descripcion, String talla, String stock) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.talla = talla;
        this.stock = stock;
    }


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTalla() {
        return talla;
    }
    public void setTalla(String talla) {
        this.talla = talla;
    }

    public String getStock() {
        return stock;
    }
    public void setStock(String stock) {
        this.stock = stock;
    }
}
