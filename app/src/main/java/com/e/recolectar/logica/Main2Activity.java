package com.e.recolectar.logica;

import android.content.Intent;
import android.location.Location;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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


    private static final int CODIGO_SELECCIONAR_UBICACION = 30 ;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


    }


    public void onClick(View view) {
        Intent geoLocalizacion = new Intent(this, MapsActivity.class);
        startActivityForResult(geoLocalizacion, CODIGO_SELECCIONAR_UBICACION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case CODIGO_SELECCIONAR_UBICACION:
                    location = data.getParcelableExtra("locacion");
                    break;
            }

        }
    }
}
