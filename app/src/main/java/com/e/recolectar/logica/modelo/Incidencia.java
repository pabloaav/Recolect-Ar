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
 * Tarea: tomar los datos, guardar en base de datos, recuperar datos segun el modelo, mostrar datos
 */

public class Incidencia {

    //region ATRIBUTOS
    private String tipo;
    private String fecha;
    private Uri imagen;
    private String descripcion;
    private String ubicacion;
    private Map<String, Object> geo_ubicacion;
    //    ArrayList<Incidencia> mSituacionesArray;
    private Context mContext;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private Bitmap rezisePhoto;
    //endregion

    //region CONSTRUCTOR, SETS Y GETS
    //Constructor vacio
    public Incidencia() {
    }

    //Constructor
    public Incidencia(String tipo, String fecha, Uri imagen, String descripcion, Map<String, Object> geo_ubicacion, String ubicacion, Context mContext, DatabaseReference mDatabase, FirebaseAuth mAuth, StorageReference mStorageRef) {
        this.tipo = tipo;
        this.fecha = fecha;
        this.imagen = imagen;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.geo_ubicacion = geo_ubicacion;
        this.mContext = mContext;
        this.mDatabase = mDatabase;
        this.mAuth = mAuth;
        this.mStorageRef = mStorageRef;
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

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public DatabaseReference getmDatabase() {
        return mDatabase;
    }

    public void setmDatabase(DatabaseReference mDatabase) {
        this.mDatabase = mDatabase;
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public void setmAuth(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }

    public StorageReference getmStorageRef() {
        return mStorageRef;
    }

    public void setmStorageRef(StorageReference mStorageRef) {
        this.mStorageRef = mStorageRef;
    }

    public Map<String, Object> getGeo_ubicacion() {
        return geo_ubicacion;
    }

    public void setGeo_ubicacion(Map<String, Object> geo_ubicacion) {
        this.geo_ubicacion = geo_ubicacion;
    }


    public void cargarIncidencia() {

        if (imagen != null) {
            String segmento = imagen.getLastPathSegment();
            final StorageReference fotoRef = mStorageRef.child("Fotos").child(mAuth.getCurrentUser().getUid()).child(segmento);

            fotoRef.putFile(imagen).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw new Exception();
                    }

                    return fotoRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadLink = task.getResult();
                        Map<String, Object> incidencia = new HashMap<>();
                        //En el mismo orden de Firebase
                        incidencia.put("descripcion", descripcion);
                        incidencia.put("fecha", fecha);
                        incidencia.put("imagen", downloadLink.toString());
                        incidencia.put("tipo", tipo);
                        incidencia.put("direccion", ubicacion);
                        incidencia.put("ubicacion", geo_ubicacion);
//      otra opcion es: mDatabase.child("Usuarios").child(mAuth.getCurrentUser().getUid()).child("situaciones").push().updateChildren(incidencia).addOnCompleteListener(new ...
                        mDatabase.child("Incidencias").child(mAuth.getCurrentUser().getUid()).push().setValue(incidencia);
//                        mDatabase.child("Incidencias").child(mAuth.getCurrentUser().getUid()).push().setValue(location);
                        mDatabase.child("Usuarios").child(mAuth.getCurrentUser().getUid()).child("incidencias").push().setValue(incidencia).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                Toast.makeText(mContext, "Se cargo la incidencia correctamente.", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(mContext, "Error al cargar la incidencia" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
            });


        }


    }

}
