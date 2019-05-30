package com.e.recolectar.adaptadores;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.e.recolectar.logica.modelo.Situacion;
import com.e.recolectar.R;

import java.util.ArrayList;

/**
 * Created by Pablo Aguirre Vicentin on 18/05/2019.
 */

public class SituacionesAdapter extends RecyclerView.Adapter<SituacionesAdapter.SituacionViewHolder> {

    ArrayList<Situacion> listaSituaciones;

    public SituacionesAdapter(ArrayList<Situacion> listaSituaciones) {
        this.listaSituaciones = listaSituaciones;
    }

    @Override
    public SituacionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_situacion, null, false);
        return new SituacionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SituacionViewHolder holder, int position) {
        holder.txtTipo.setText(listaSituaciones.get(position).gettipo());
        holder.txtFecha.setText(listaSituaciones.get(position).getfecha());
        //holder.foto.setImageResource(listaSituaciones.get(position).getImagenId());
    }

    @Override
    public int getItemCount() {
        return listaSituaciones.size();
    }

    public class SituacionViewHolder extends RecyclerView.ViewHolder {
        TextView txtTipo, txtFecha;
        ImageView foto;

        public SituacionViewHolder(View itemView) {
            super(itemView);
            txtTipo = itemView.findViewById(R.id.idTipo);
            txtFecha = itemView.findViewById(R.id.idFecha);
            foto = itemView.findViewById(R.id.idImagen);
        }
    }
}
