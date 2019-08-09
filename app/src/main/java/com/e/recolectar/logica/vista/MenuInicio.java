package com.e.recolectar.logica.vista;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.e.recolectar.R;
import com.e.recolectar.adaptadores.AdaptadorPaginas;
import com.e.recolectar.fragmentos.EstadoFragment;
import com.e.recolectar.fragmentos.IncidenciaFragment;
import com.e.recolectar.fragmentos.PuntoReciclajeFragment;
import com.google.firebase.auth.FirebaseAuth;


public class MenuInicio extends AppCompatActivity implements EstadoFragment.OnFragmentInteractionListener, IncidenciaFragment.OnFragmentInteractionListener, PuntoReciclajeFragment.OnFragmentInteractionListener {

    //region ATRIBUTOS
    private int[] tabIcons = {
            R.drawable.incidencia,
            R.drawable.ic_estado,
            R.drawable.puntos};
    private FirebaseAuth mAuth;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_inicio);
        try {
            AdaptadorPaginas sectionsPagerAdapter = new AdaptadorPaginas(this, getSupportFragmentManager());
            ViewPager viewPager = findViewById(R.id.view_pager);
            viewPager.setAdapter(sectionsPagerAdapter);
            TabLayout tabs = findViewById(R.id.tabs);
            tabs.setupWithViewPager(viewPager);

            // Para poner iconos
            tabs.getTabAt(0).setIcon(tabIcons[0]);
            tabs.getTabAt(1).setIcon(tabIcons[1]);
            tabs.getTabAt(2).setIcon(tabIcons[2]);

            //Si hay Menu, existe un usuario, luego:
            mAuth = FirebaseAuth.getInstance();

            //Mostramos el usuario en el SnackBAr
            Snackbar.make(viewPager, "Bienvenido: " + mAuth.getCurrentUser().getEmail(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }//Fin de metodo onCreate()

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void logOut(View v) {
        if (v.getId() == R.id.btn_cerrar_sesion) {
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle("Importante, atención por favor...");
            dialogo1.setMessage("¿ Esta usted seguro de cerrar sesión ?");
            dialogo1.setCancelable(false);
            dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    aceptar();
                }
            });
            dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogo1, int id) {
                    dialogo1.dismiss();
                }
            });
            dialogo1.show();
        }
    }

    private void aceptar() {
        FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    //Do anything here which needs to be done after signout is complete
                    Intent login = new Intent(MenuInicio.this, MainActivity.class);
                    login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(login);
                    finish();
                } else {
                }
            }
        };

        //Init and attach
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(authStateListener);

        //Call signOut()
        mAuth.signOut();
    }

}