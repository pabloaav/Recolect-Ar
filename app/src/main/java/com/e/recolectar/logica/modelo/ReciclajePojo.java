package com.e.recolectar.logica.modelo;

public class ReciclajePojo {

    //region Atributos
    private String nombre;
    private String descripcion;
    private String enlace;
    //endregion


    public ReciclajePojo() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEnlace() {
        return enlace;
    }

    public void setEnlace(String enlace) {
        this.enlace = enlace;
    }
}
