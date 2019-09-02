package com.e.recolectar.logica.vista;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.e.recolectar.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {

    //region Atributos
    private FirebaseAuth firebaseAuth;
    private ProgressBar mProgressBar;
    ProgressBar pb;
    int counter = 10;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Binding de elementos visuales
        mProgressBar = findViewById(R.id.progressBar2);


        //Comportamiento progressbar

        pb = mProgressBar.findViewById(R.id.progressBar2);

        final Timer t = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run()
            {
                counter++;
                pb.setProgress(counter);

                if(counter == 100)
                    t.cancel();
            }
        };

        t.schedule(tt,0,25);

        //inicializacion de Firebase
        inicializarFirebase();

    }//Fin de onCreate()

    @Override
    protected void onStart() {
        super.onStart();
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Check if user is signed in (non-null) and update UI accordingly.
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    Intent menu = new Intent(SplashScreen.this, MenuInicio.class);
                    menu.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(menu);
                    //contamos la estadistica "usos":
                    contarUso();
                    finish();
                } else {
                    Intent login = new Intent(SplashScreen.this, MainActivity.class);
                    login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(login);
                    finish();
                }
            }

        }, 2000);
    }//Fin de onStart()

    private void contarUso() {
        final DatabaseReference ref = databaseReference.child("Estadisticas/Usuarios/usos");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snap) {
                Integer usos = snap.getValue(Integer.class);
                usos++;
                ref.setValue(usos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Inicializacion de objetos de Firebase
    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        //inicializamos el objeto firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }



}

