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
import com.e.recolectar.logica.modelo.ReciclajePojo;

import java.util.ArrayList;

public class AdaptadorRecyclerReciclaje extends RecyclerView.Adapter<AdaptadorRecyclerReciclaje.ReciclajeViewHolder> {

    //region ATRIBUTOS de AdaptadorRecyclerReciclaje

    //El arreglo de objetos Ecopuntos
    ArrayList<ReciclajePojo> listaReciclaje;

    //El layout del cardview donde se define que mostrar en pantalla y como
    int cardViewLayout;

    //El Context, que es la Activity desde donde se llama a mostrar los datos
    Context mContext;

    //endregion

    //Constructor
    public AdaptadorRecyclerReciclaje(ArrayList<ReciclajePojo> listaReciclaje, int cardViewLayout, Context mContext) {
        this.listaReciclaje = listaReciclaje;
        this.cardViewLayout = cardViewLayout;
        this.mContext = mContext;
    }

    //Este metodo toma los campos del cardview ecopuntos, y le asigna un valor determinado, que corresponde a cada objeto del arreglo de ecopuntos que viene dado como parametro de la clase AdaptadorRecyclerReciclaje
    @Override
    public void onBindViewHolder(ReciclajeViewHolder reciclajeViewHolder, int position) {
        //En cada posicion del array hay un objeto ecopuntos.
        ReciclajePojo reciclaje = listaReciclaje.get(position);

        //De cada ecopunto del modelo de datos, que se alimenta de la base de datos, tomamos los valores de los atributos de cada objeto, y se los pasamos a la vista, para que en cada atributo del ViewHolder muestre su correspondiente valor
        reciclajeViewHolder.nombre.setText(reciclaje.getNombre());
        reciclajeViewHolder.descripcion.setText(reciclaje.getDescripcion());
        reciclajeViewHolder.enlace.setText(reciclaje.getEnlace());

    }//Fin de OnBind...


    //Este metodo inicializa la vista del ViewHolder
    @Override
    public ReciclajeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Decimos que desde el Context (la Activity Menu) inflar la vista de los cardview
        View vista = LayoutInflater.from(mContext).inflate(cardViewLayout, parent, false);
        //Le pasamos esta vista, es decir, el layout del cardview, a la Clase ReciclajeViewHolder, para que haga la asociacion de datos que le corresponde
        return new ReciclajeViewHolder(vista);
    }

    @Override
    public int getItemCount() {
        if (listaReciclaje.size() > 0) {
            return listaReciclaje.size();
        } else {
            return 0;
        }
    }

    /**
     * Esta es la Clase Inner que se encarga de ligar los elementos del layout Cardview Reciclaje
     * que muestran los datos traidos de la base de datos, del nodo Ecopuntos
     */
    public class ReciclajeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView nombre;
        private TextView descripcion;
        private TextView enlace;

        /**
         * Constructor: cada dato de un ecopunto, se asocia con los los elementos de la vista del cardview ecopuntos representados por su nombre de id que figuran en el layout Cardview
         *
         * @param itemView: Es el Cardview donde estan los elementos que vamos a asociar con los datos traidos de Firebase
         */
        public ReciclajeViewHolder(@NonNull View itemView) {
            super(itemView);
            //Cada elemento del view (los cardview de ecopuntos) debe ser clicable
            itemView.setOnClickListener(this);

            //Aqui en el constructor, asociamos los valores del ecopunto tomados del cardview a sus atributos, declarados mas arriba, luego de la firma de la Clase Inner ReciclajeViewHolder
            nombre = itemView.findViewById(R.id.cv_nombre_web);
            descripcion = itemView.findViewById(R.id.cv_contenido);
            enlace = itemView.findViewById(R.id.cv_enlace);

        }

        //Al hacer clic en un cardview debe llevarnos a una vista ampliada
        @Override
        public void onClick(View view) {
            //Vista correspondiente al click en el cardview de incidencia
            llamarWeb(view);
            Toast.makeText(mContext, "Abriendo la web en el navegador, un momento por favor...", Toast.LENGTH_LONG).show();
        }

        private void llamarWeb(View v) {
//        Uri gmmIntentUri = Uri.parse("google.navigation:q=Plaza+Libertad");
            String paginaWeb = "https://ecoplas.org.ar/reciclado-de-plasticos-2/";
            Uri web = Uri.parse(paginaWeb);
            Intent webIntent = new Intent(Intent.ACTION_VIEW, web);
            v.getContext().startActivity(webIntent);

        }
    }
}

