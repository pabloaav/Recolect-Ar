package com.e.recolectar.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.e.recolectar.R;

import com.e.recolectar.logica.modelo.IncidenciaPojo;

import java.util.ArrayList;

/**
 * La clase AdaptadorRecyclerIncidencias se encarga de sincronizar, vincular o asociar un arraylist (lista de objetos), con los elementos de cada Cardview, mostrandolos a medida que se solicitan los datos
 * <p>
 * Recibe una colecccion de objetos de tipo ViewHolder, que es la clase Inne declarada para manipular los objetos Incidencia
 */
public class AdaptadorRecyclerIncidencias extends RecyclerView.Adapter<AdaptadorRecyclerIncidencias.IncidenciasViewHolder> {

    //region ATRIBUTOS de AdaptadorRecyclerIncidencias

    //El arreglo de objetos Incidencia
    ArrayList<IncidenciaPojo> listaIncidencias;

    //El layout del cardview donde se define que mostrar en pantalla y como
    int cardViewLayout;

    //El Context, que es la Activity desde donde se llama a mostrar los datos
    Context mContext;

    //endregion

    //Constructor
    public AdaptadorRecyclerIncidencias(ArrayList<IncidenciaPojo> listaIncidencias, int cardViewLayout, Context mContext) {
        this.listaIncidencias = listaIncidencias;
        this.cardViewLayout = cardViewLayout;
        this.mContext = mContext;
    }

    //Este metodo toma los campos del cardview incidencias, y le asigna un valor determinado, que corresponde a cada objeto del arreglo de incidencias que viene dado com parametro de la clase AdaptadorRecyclerIncidencias
    @Override
    public void onBindViewHolder(final IncidenciasViewHolder incidenciasViewHolder, int position) {
        //En cada posicion del array hay un objeto incidencias.
        IncidenciaPojo incidencia = listaIncidencias.get(position);

        //De cada incidencia del modelo de datos, que se alimenta de la base de datos, tomamos os valores de los atributos de cada objeto, y se los pasamos a la vista, para que en cada atributo del ViewHolder muestre su correspondiente valor
        incidenciasViewHolder.tipo.setText(incidencia.getTipo());
        incidenciasViewHolder.fecha.setText(incidencia.getFecha());
        incidenciasViewHolder.descripcion.setText(incidencia.getDescripcion());
        incidenciasViewHolder.direccion.setText(incidencia.getDireccion());
        incidenciasViewHolder.ubicacion.setText(incidencia.getCadenaUbicacion());
//        incidenciasViewHolder.imagen.setImageResource(R.drawable.basura);

        String imagen = incidencia.getImagen();
//cargar foto Firebase
        Glide
                .with(mContext)
                .load(imagen)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        incidenciasViewHolder.progressBar.setVisibility(View.GONE);
                        incidenciasViewHolder.imagen.setVisibility(View.VISIBLE);
                        incidenciasViewHolder.imagen.setImageResource(R.drawable.ic_error_black_24dp);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        incidenciasViewHolder.progressBar.setVisibility(View.GONE);
                        incidenciasViewHolder.imagen.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .placeholder(R.drawable.icono_sin_foto256x)
                .error(R.drawable.icono_sin_foto512x)
                .into(incidenciasViewHolder.imagen);

    }//Fin de OnBind...

    //Este metodo inicializa la vista del ViewHolder
    @Override
    public IncidenciasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Decimos que desde el Context (la Activity Menu) inflar la vista de los cardview
        View vista = LayoutInflater.from(mContext).inflate(cardViewLayout, parent, false);
        //Le pasamos esta vista, es decir, el layout del cardview, a la Clase IncidenciasViewHolder, para que haga la asociacion de datos que le corresponde
        return new IncidenciasViewHolder(vista);
    }

    @Override
    public int getItemCount() {
        if (listaIncidencias.size() > 0) {
            return listaIncidencias.size();
        } else {
            return 0;
        }
    }

    /**
     * Esta es la Clase Inner que se encarga de ligar los elementos del layout Cardview
     * que muestran los datos traidos de la base de datos
     */
    public class IncidenciasViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imagen;
        private TextView tipo;
        private TextView fecha;
        private TextView descripcion;
        private TextView direccion;
        private TextView ubicacion;
        private ProgressBar progressBar;

        /**
         * Constructor: cada dato de una incidencia, se asocia con los los elementos de la vista del cardview representados por su nombre de id que figuran en el layout Cardview
         *
         * @param itemView: Es el Cardview donde estan los elementos que vamos a asociar con los datos traidos de Firebase
         */
        public IncidenciasViewHolder(@NonNull View itemView) {
            super(itemView);
            //Cada elemento del view (los cardview de incidencias) debe ser clicable
            itemView.setOnClickListener(this);

            //Aqui en el constructor, asociamos los valores de la incidencia tomados del cardview a sus atributos, declarados mas arriba, luego de la firma de la Clase Inner IncidenciasViewHolder
            imagen = itemView.findViewById(R.id.cv_imagen);
            tipo = itemView.findViewById(R.id.cv_tipo);
            fecha = itemView.findViewById(R.id.cv_fecha);
            descripcion = itemView.findViewById(R.id.cv_descripcion);
            direccion = itemView.findViewById(R.id.cv_direccion);
            ubicacion = itemView.findViewById(R.id.cv_ubicacion);
            progressBar = itemView.findViewById(R.id.progressBar);
        }

        //Al hacer clic en un cardview debe llevarnos a una vista ampliada
        @Override
        public void onClick(View view) {
            //Vista correspondiente al click en el cardview de incidencia
        }
    }
}
