package com.e.recolectar.adaptadores;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.e.recolectar.R;
import com.e.recolectar.logica.modelo.Incidencia;

import java.util.ArrayList;

/**
 * La clase AdaptadorRecyclerIncidencias se encarga de sincronizar, vincular o asociar un arraylist (lista de objetos), con los elementos de cada Cardview, mostrandolos a medida que se solicitan los datos
 *
 * @param : recibe una colecccion de objetos de tipo ViewHolder, que es la clase Inne declarada para manipular los objetos Incidencia
 */
public class AdaptadorRecyclerIncidencias extends RecyclerView.Adapter<AdaptadorRecyclerIncidencias.IncidenciasViewHolder> {
    //El arreglo de objetos Incidencia
    ArrayList<Incidencia> listaIncidencias;
    //El layout del cardview donde se define que mostrar en pantalla y como
    int cardViewLayout;
    //El Context, que es la Activity desde donde se llama a mostrar los datos
    Context mContext;

    //Constructor
    public AdaptadorRecyclerIncidencias(ArrayList<Incidencia> listaIncidencias, int cardViewLayout, Context mContext) {
        this.listaIncidencias = listaIncidencias;
        this.cardViewLayout = cardViewLayout;
        this.mContext = mContext;
    }

    @Override
    public void onBindViewHolder(IncidenciasViewHolder holder, int position) {
        Situacion situacion = listaIncidencias.get(position);
        holder.txtTipo.setText(situacion.gettipo());
        holder.txtFecha.setText(situacion.getfecha());
        //Cargando Imagen dsde Firebase con Glide
        Glide
                .with(mContext)
                .load(situacion.getImagenRuta())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.foto.setVisibility(View.VISIBLE);
                        holder.foto.setImageResource(R.drawable.ic_error_black_24dp);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.foto.setVisibility(View.VISIBLE);
                        return false;
                    }


                })
                .into(holder.foto);
        //holder.foto.setImageResource(listaIncidencias.get(position).getImagenId());
    }


    @Override
    public IncidenciasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_situacion, null, false);
        return new SituacionViewHolder(view);

    }

    @Override
    public int getItemCount() {
        return listaIncidencias.size();
    }


    /**
     * Esta es la Clase Inner que se encarga de ligar los elementos del layout Cardview
     * que muestran los datos traidos de la base de datos
     */
    public class IncidenciasViewHolder extends RecyclerView.ViewHolder {

        private ImageView imagen;
        private TextView tipo;
        private TextView fecha;
        private TextView descripcion;
        private TextView ubicacion;

        /**
         * Constructor: cada dato de una incidencia, se asocia con los los elementos de la vista del cardview representados por su nombre de id que figuran en el layout Cardview
         *
         * @param itemView: Es el Cardview donde estan los elementos que vamos a asociar con los datos traidos de Firebase
         */
        public IncidenciasViewHolder(View itemView) {
            super(itemView);
            //Aqui en el constructor, asociamos los valores de la incidencia tomados del cardview a sus atributos, declarados mas arriba, luego de la firma de la Clase Inner IncidenciasViewHolder
            imagen = itemView.findViewById(R.id.cv_imagen);
            tipo = itemView.findViewById(R.id.cv_tipo);
            fecha = itemView.findViewById(R.id.cv_fecha);
            descripcion = itemView.findViewById(R.id.cv_descripcion);
            ubicacion = itemView.findViewById(R.id.cv_ubicacion);

        }
    }
}
