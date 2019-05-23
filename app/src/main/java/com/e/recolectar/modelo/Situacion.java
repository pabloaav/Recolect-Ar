package com.e.recolectar.modelo;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pablo Aguirre Vicentin on 18/05/2019.
 */

public class Situacion {

    private String tipo;
    private String fecha;
    private int imagenId;
//    ArrayList<Situacion> mSituacionesArray;
    private Context mContext;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;

    //Constructor vacio
    public Situacion() {
    }

    //Constructor
    public Situacion(Context mContext, DatabaseReference mDatabase, FirebaseAuth mAuth, StorageReference mStorageRef, String fecha, String tipo) {
        this.fecha = fecha;
        this.tipo = tipo;
        this.mContext = mContext;
        this.mDatabase = mDatabase;
        this.mAuth = mAuth;
        this.mStorageRef = mStorageRef;
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


//    public ArrayList<Situacion> getDatos(DatabaseReference databaseReference) {
//        mSituacionesArray.clear();
//        databaseReference.child("Situaciones").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                        String tipo = ds.child("idTipo").getValue().toString();
//                        String fecha = ds.child("fecha").getValue().toString();
//                        int imagenId = (int) ds.child("idImagen").getValue();
//                        mSituacionesArray.add(new Situacion(fecha, tipo, imagenId));
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//        return mSituacionesArray;
//    }

    public void cargarSituacion(final String tipoSituacion, final String fechaSituacion, Uri filePath) {

        if (filePath != null) {

            final StorageReference fotoRef = mStorageRef.child("Fotos").child(mAuth.getCurrentUser().getUid()).child(filePath.getLastPathSegment());
            fotoRef.putFile(filePath).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                        Map<String, Object> situacion = new HashMap<>();
                        situacion.put("tipo", tipoSituacion);
                        situacion.put("fecha", fechaSituacion);
                        situacion.put("imagen", downloadLink.toString());
//      otra opcion es: mDatabase.child("Usuarios").child(mAuth.getCurrentUser().getUid()).child("situaciones").push().updateChildren(situacion).addOnCompleteListener(new ...
                        mDatabase.child("Situaciones").child(mAuth.getCurrentUser().getUid()).push().setValue(situacion);
                        mDatabase.child("Usuarios").child(mAuth.getCurrentUser().getUid()).child("situaciones").push().setValue(situacion).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                Toast.makeText(mContext, "Se cargo la situacion correctamente.", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(mContext, "Error al cargar la situacion" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
            });


        }


    }

}
