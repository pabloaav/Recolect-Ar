package com.e.recolectar.logica.modelo;

public class UbicacionPojo {

    private String cadenaUbicacion;
    private double latitud;
    private double longitud;

    public UbicacionPojo() {
    }

    public UbicacionPojo(String cadenaUbicacion, double latitud, double longitud) {
        this.cadenaUbicacion = cadenaUbicacion;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public String getCadenaUbicaion() {
        return cadenaUbicacion;
    }

    public void setCadenaUbicaion(String cadenaUbicaion) {
        this.cadenaUbicacion = cadenaUbicaion;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

}
