package com.e.recolectar.modelo;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Pablo Aguirre Vicentin on 18/05/2019.
 */

public class Situacion {

    private String tipo;
    private String fecha;
    private int imagenId;
    ArrayList<Situacion> mSituacionesArray;

    //Constructor vacio
    public Situacion() {
    }

    //Constructor
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


    public  ArrayList<Situacion> getDatos(DatabaseReference databaseReference) {
        mSituacionesArray.clear();
        databaseReference.child("Situaciones").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String tipo = ds.child("idTipo").getValue().toString();
                        String fecha = ds.child("fecha").getValue().toString();
                        int imagenId = (int) ds.child("idImagen").getValue();
                        mSituacionesArray.add(new Situacion(fecha,tipo,imagenId));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return mSituacionesArray;
    }


}
