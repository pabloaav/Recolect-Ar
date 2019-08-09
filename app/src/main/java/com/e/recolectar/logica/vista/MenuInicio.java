package com.e.recolectar.logica.vista;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.e.recolectar.R;
import com.e.recolectar.adaptadores.AdaptadorPaginas;
import com.e.recolectar.fragmentos.EstadoFragment;
import com.e.recolectar.fragmentos.IncidenciaFragment;
import com.e.recolectar.fragmentos.PuntoReciclajeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
            //Declaration and defination
            FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if (firebaseAuth.getCurrentUser() == null) {
                        //Do anything here which needs to be done after signout is complete
                        startActivity(new Intent(MenuInicio.this, MainActivity.class));
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
}