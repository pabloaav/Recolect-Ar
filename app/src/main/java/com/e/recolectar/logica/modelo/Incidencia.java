package com.e.recolectar.logica.modelo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pablo Aguirre Vicentin on 18/05/2019.
 * La clase Incidencia modela los objetos de una incidencia hecha por el usuario.
 */

public class Incidencia {

    //region ATRIBUTOS

    private String tipo;
    private String fecha;
    private Uri imagen;
    private String descripcion;
    private String ubicacion;
    private Map<String, Object> geo_ubicacion;
    private Map<String, Boolean> estado;

    //endregion

    //region CONSTRUCTOR, SETS Y GETS

    //Constructor vacio
    public Incidencia() {
    }

    //Constructor
    public Incidencia(String tipo, String fecha, Uri imagen, String descripcion, Map<String, Object> geo_ubicacion, String ubicacion, Map<String, Boolean> estado) {
        this.tipo = tipo;
        this.fecha = fecha;
        this.imagen = imagen;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.geo_ubicacion = geo_ubicacion;
        this.estado = estado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Uri getImagen() {
        return imagen;
    }

    public void setImagen(Uri imagen) {
        this.imagen = imagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Map<String, Object> getGeo_ubicacion() {
        return geo_ubicacion;
    }

    public void setGeo_ubicacion(Map<String, Object> geo_ubicacion) {
        this.geo_ubicacion = geo_ubicacion;
    }

    public Map<String, Boolean> getEstado() {
        return estado;
    }

    public void setEstado(Map<String, Boolean> estado) {
        this.estado = estado;
    }
}
