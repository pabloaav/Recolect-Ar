package com.e.recolectar.logica.vista;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.e.recolectar.R;
import com.e.recolectar.adaptadores.AdaptadorPaginas;
import com.e.recolectar.fragmentos.EstadoFragment;
import com.e.recolectar.fragmentos.IncidenciaFragment;
import com.e.recolectar.fragmentos.PuntoReciclajeFragment;

public class MenuInicio extends AppCompatActivity implements EstadoFragment.OnFragmentInteractionListener, IncidenciaFragment.OnFragmentInteractionListener, PuntoReciclajeFragment.OnFragmentInteractionListener {

    //region ATRIBUTOS
    private int[] tabIcons = {
            R.drawable.incidencia,
            R.drawable.ic_estado,
            R.drawable.puntos};
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
        } catch (Exception e) {
            e.printStackTrace();
        }
       /* FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}