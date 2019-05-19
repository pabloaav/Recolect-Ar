package com.e.recolectar.modelo;

/**
 * Created by Pablo Aguirre Vicentin on 18/05/2019.
 */

public class Situacion {

    private String tipo;
    private String fecha;
    private int imagenId;

    public Situacion(String fecha, String tipo, int imagenId) {
        this.fecha = fecha;
        this.tipo = tipo;
        this.imagenId = imagenId;
    }

    public String getfecha() {
        return fecha;
    }

    public void setfecha(String fecha) {
        this.fecha = fecha;
    }

    public String gettipo() {
        return tipo;
    }

    public void settipo(String tipo) {
        this.tipo = tipo;
    }

    public int getImagenId() {
        return imagenId;
    }

    public void setImagenId(int imagenId) {
        this.imagenId = imagenId;
    }
}
