package com.e.recolectar.logica.vista;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.e.recolectar.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    //region Atributos
    private FirebaseAuth firebaseAuth;
    private ProgressBar mProgressBar;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //inicializacion de Firebase
        inicializarFirebase();

        //Binding de elementos visuales
        mProgressBar = findViewById(R.id.progressBar2);

    }//Fin de onCreate()

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(SplashScreen.this, "Verificando sus Datos", Toast.LENGTH_SHORT).show();
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

    //Inicializacion de objetos de Firebase
    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        //inicializamos el objeto firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();
    }

}

