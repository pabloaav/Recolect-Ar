package com.e.recolectar.logica.modelo;

public class EcopuntosPojo {

    //region Atributos
    private String nombre;
     private String ubicacion;
     private String informacion;
    //endregion

    //Constructor vacio necesario para objetos Pojo que captan info de SnapShot
    public EcopuntosPojo() {
    }

    //Sets y Gets para atributos

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getInformacion() {
        return informacion;
    }

    public void setInformacion(String informacion) {
        this.informacion = informacion;
    }
}
