package com.e.recolectar.logica;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.e.recolectar.R;
import com.e.recolectar.logica.modelo.Incidencia;
import com.e.recolectar.logica.modelo.IncidenciaPojo;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.makeText;

public class Main2Activity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();


        final DatabaseReference usuarioIncidenciaRef = databaseReference.child("Usuarios").child(mAuth.getCurrentUser().getUid()).child("incidencias");

        usuarioIncidenciaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<IncidenciaPojo> array = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    IncidenciaPojo incidenciaPojo = snapshot.getValue(IncidenciaPojo.class);
                    incidenciaPojo.setTipo(incidenciaPojo.getTipo());
                    incidenciaPojo.setFecha(incidenciaPojo.getFecha());
                    incidenciaPojo.setDescripcion(incidenciaPojo.getDescripcion());
                    incidenciaPojo.setUbicacion(incidenciaPojo.getUbicacion());
                    incidenciaPojo.setImagen(incidenciaPojo.getImagen());

                    array.add(incidenciaPojo);


                    Log.d("MyApp", "I am here");
                    System.out.println(incidenciaPojo);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
