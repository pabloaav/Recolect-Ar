package com.e.recolectar.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.e.recolectar.R;
import com.e.recolectar.logica.modelo.EcopuntosPojo;

import java.util.ArrayList;

public class AdaptadorRecyclerEcopuntos extends RecyclerView.Adapter<AdaptadorRecyclerEcopuntos.EcopuntosViewHolder> {

    //region ATRIBUTOS de AdaptadorRecyclerEcopuntos

    //El arreglo de objetos Ecopuntos
    ArrayList<EcopuntosPojo> listaEcopuntos;

    //El layout del cardview donde se define que mostrar en pantalla y como
    int cardViewLayout;

    //El Context, que es la Activity desde donde se llama a mostrar los datos
    Context mContext;

    //endregion

    //Constructor
    public AdaptadorRecyclerEcopuntos(ArrayList<EcopuntosPojo> listaEcopuntos, int cardViewLayout, Context mContext) {
        this.listaEcopuntos = listaEcopuntos;
        this.cardViewLayout = cardViewLayout;
        this.mContext = mContext;
    }

    //Este metodo toma los campos del cardview ecopuntos, y le asigna un valor determinado, que corresponde a cada objeto del arreglo de ecopuntos que viene dado como parametro de la clase AdaptadorRecyclerEcopuntos
    @Override
    public void onBindViewHolder(EcopuntosViewHolder ecopuntosViewHolder, int position) {
        //En cada posicion del array hay un objeto ecopuntos.
        EcopuntosPojo ecopunto = listaEcopuntos.get(position);

        //De cada ecopunto del modelo de datos, que se alimenta de la base de datos, tomamos los valores de los atributos de cada objeto, y se los pasamos a la vista, para que en cada atributo del ViewHolder muestre su correspondiente valor
        ecopuntosViewHolder.nombre.setText(ecopunto.getNombre());
        ecopuntosViewHolder.ubicacion.setText(ecopunto.getUbicacion());
        ecopuntosViewHolder.informacion.setText(ecopunto.getInformacion());

    }//Fin de OnBind...


    //Este metodo inicializa la vista del ViewHolder
    @Override
    public EcopuntosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Decimos que desde el Context (la Activity Menu) inflar la vista de los cardview
        View vista = LayoutInflater.from(mContext).inflate(cardViewLayout, parent, false);
        //Le pasamos esta vista, es decir, el layout del cardview, a la Clase EcopuntosViewHolder, para que haga la asociacion de datos que le corresponde
        return new EcopuntosViewHolder(vista);
    }

    @Override
    public int getItemCount() {
        if (listaEcopuntos.size() > 0) {
            return listaEcopuntos.size();
        } else {
            return 0;
        }
    }

    /**
     * Esta es la Clase Inner que se encarga de ligar los elementos del layout Cardview Ecopuntos
     * que muestran los datos traidos de la base de datos, del nodo Ecopuntos
     */
    public class EcopuntosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView nombre;
        private TextView ubicacion;
        private TextView informacion;

        /**
         * Constructor: cada dato de un ecopunto, se asocia con los los elementos de la vista del cardview ecopuntos representados por su nombre de id que figuran en el layout Cardview
         *
         * @param itemView: Es el Cardview donde estan los elementos que vamos a asociar con los datos traidos de Firebase
         */
        public EcopuntosViewHolder(@NonNull View itemView) {
            super(itemView);
            //Cada elemento del view (los cardview de ecopuntos) debe ser clicable
            itemView.setOnClickListener(this);

            //Aqui en el constructor, asociamos los valores del ecopunto tomados del cardview a sus atributos, declarados mas arriba, luego de la firma de la Clase Inner EcopuntosViewHolder
            nombre = itemView.findViewById(R.id.cv_nombre);
            ubicacion = itemView.findViewById(R.id.cv_ubicacion);
            informacion = itemView.findViewById(R.id.cv_informacion);

        }

        //Al hacer clic en un cardview debe llevarnos a una vista ampliada
        @Override
        public void onClick(View view) {
            //accion al hacer click en el card ecopunto
            Toast.makeText(mContext, "Abriendo el Ecopunto en Mapas de Google, un momento por favor...", Toast.LENGTH_LONG).show();
            llamarMapsApp(view);
        }

        private void llamarMapsApp(View v) {
//        Uri gmmIntentUri = Uri.parse("google.navigation:q=Plaza+Libertad");
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + nombre.getText().toString() + ", " + ubicacion.getText().toString());
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(v.getContext().getPackageManager()) != null) {
                v.getContext().startActivity(mapIntent);
            }
        }
    }
}
