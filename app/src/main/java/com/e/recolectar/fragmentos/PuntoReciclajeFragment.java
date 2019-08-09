package com.e.recolectar.fragmentos;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.e.recolectar.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PuntoReciclajeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PuntoReciclajeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PuntoReciclajeFragment extends Fragment {

    //region Atributos por Default
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    //endregion

    //region Atributos
    private TextView txt_ubicacion;
    //endregion

    //region Metodos por Default
    public PuntoReciclajeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PuntoReciclajeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PuntoReciclajeFragment newInstance(String param1, String param2) {
        PuntoReciclajeFragment fragment = new PuntoReciclajeFragment();
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

    //region Metodos

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View vista = inflater.inflate(R.layout.fragment_punto_reciclaje, container, false);
        //hacer el bind del textview de la ubicacion del ecopunto
        txt_ubicacion = vista.findViewById(R.id.tv_Ubicacion);
        txt_ubicacion.setTextColor(getResources().getColor(R.color.linkColor));
        //Lo hacemos clickable
        txt_ubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hacer lo del maps app
                llamarMapsApp();
              Toast.makeText(getActivity(), "Abriendo Ecopunto en Maps de Google", Toast.LENGTH_SHORT).show();

            }
        });
        // Inflate the layout for this fragment
        return vista;

    }

    private void llamarMapsApp() {
//        Uri gmmIntentUri = Uri.parse("google.navigation:q=Plaza+Libertad");
        Uri gmmIntentUri = Uri.parse("geo:-27.4703795,-58.8244543,17?z=15&q=Plaza+Libertad");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }

    }


    //endregion

}
