package com.e.recolectar.fragmentos;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.e.recolectar.R;
import com.e.recolectar.adaptadores.AdaptadorRecyclerIncidencias;
import com.e.recolectar.logica.modelo.Incidencia;
import com.e.recolectar.logica.modelo.IncidenciaPojo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EstadoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EstadoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EstadoFragment extends Fragment {
    //region DEFAUL ATRIBUTOS
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    //endregion
    private DatabaseReference mDataBase;
    private FirebaseAuth mAuth;
    private ArrayList<IncidenciaPojo> array;
    //endregion

    //region DEFAULT METODOS
    public EstadoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EstadoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EstadoFragment newInstance(String param1, String param2) {
        EstadoFragment fragment = new EstadoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    //endregion

    //region METODOS
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Objetos de Firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        mDataBase = firebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();

        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_estado, container, false);

        //Instanciamos el objeto Recycler view
        RecyclerView recyclerViewIncidencias = vista.findViewById(R.id.rv_estadosIncidencias);

        //Se necesita un objeto Linear Layaout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        //Seteamos el recycler con el LAyout Manager
        recyclerViewIncidencias.setLayoutManager(linearLayoutManager);

        cargarArrayRecycler(recyclerViewIncidencias);

        //Retornamos la vista que ha sido inflada
        return vista;
    }

    private void cargarArrayRecycler(final RecyclerView recyclerViewIncidencias) {

        DatabaseReference ref = mDataBase.child("Usuarios").child(mAuth.getCurrentUser().getUid()).child("incidencias");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<IncidenciaPojo> array = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    IncidenciaPojo incidenciaPojo = snapshot.getValue(IncidenciaPojo.class);
                    incidenciaPojo.setTipo(incidenciaPojo.getTipo());
                    incidenciaPojo.setFecha(incidenciaPojo.getFecha());
                    incidenciaPojo.setDescripcion(incidenciaPojo.getDescripcion());
                    incidenciaPojo.setUbicacion(incidenciaPojo.getUbicacion());
                    incidenciaPojo.setImagen(incidenciaPojo.getImagen());

                    array.add(incidenciaPojo);

                }//Fin del for

                //Colocamos la clase adaptadora del recycler que creamos para instanciar el view holder y manejar el objeto RecyclerView. El primer parametro que recibe el constructor debe ser un array de objetos de Incidencia que se obtienen de la bsae de datos
                AdaptadorRecyclerIncidencias adaptador = new AdaptadorRecyclerIncidencias(array, R.layout.cardview_incidencia, getActivity());

                //Seteamos al recycler el adaptador correspondiente
                recyclerViewIncidencias.setAdapter(adaptador);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }//Fin cargar recycler

    //endregion

}
