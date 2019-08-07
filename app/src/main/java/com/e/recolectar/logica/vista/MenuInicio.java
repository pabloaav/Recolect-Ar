package com.e.recolectar.logica.vista;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

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


}