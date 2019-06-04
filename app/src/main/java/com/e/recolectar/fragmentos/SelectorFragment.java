package com.e.recolectar.fragmentos;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.e.recolectar.R;
import com.e.recolectar.logica.modelo.ModeloVistaDePagina;


/**
 * A placeholder fragment containing a simple view.
 * En un Fragment, pues extiende (hereda) de la clase Fragment.
 * Es del mismo tipo que se crea para otros Fragments desde el menu
 */
public class SelectorFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private ModeloVistaDePagina modeloVistaDePagina;

    public static Fragment newInstance(int index) {
        // Reemplazamos el codigo original por los Fragments que queremos devolver
        Fragment fragmento = null;
        //boolean esElegidoFragmento = false;
        switch (index) {
            case 1:
                fragmento = new IncidenciaFragment();

                //esElegidoFragmento = true;
                break;
            case 2:
                fragmento = new EstadoFragment();
               // esElegidoFragmento = true;
                break;
            case 3:
                fragmento = new PuntoReciclajeFragment();
                //esElegidoFragmento = true;
                break;
            default:
                fragmento = new IncidenciaFragment();
               // esElegidoFragmento = true;
                break;
        }

        //region Codigo Original
        //        SelectorFragment fragment = new SelectorFragment();
//        //La clase Bundle se utiliza para pasar informacion de una Activity a otra
//        Bundle bundle = new Bundle();
//        bundle.putInt(ARG_SECTION_NUMBER, index);
//        //El fragmento retornado por este metodo, es seteado con un argumento de tipo Bundle, al cual se le pusieron un String (ARG_SECTION_NUMBER), y un entero (index)
//        fragment.setArguments(bundle);
        //endregion
        return fragmento;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        modeloVistaDePagina = ViewModelProviders.of(this).get(ModeloVistaDePagina.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        modeloVistaDePagina.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_selector, container, false);
        final TextView textView = root.findViewById(R.id.section_label);
        modeloVistaDePagina.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}