package com.example.mfrbmv10.Fragments.Muestreos;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mfrbmv10.FirebaseMotor.Crud;
import com.example.mfrbmv10.Modelos.Muestreo;
import com.example.mfrbmv10.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MuestreoNuevoIntermedioFragment2.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MuestreoNuevoIntermedioFragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MuestreoNuevoIntermedioFragment2 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Crud crud;
    private String id_bitacora, id_muestreo, nombre_bitacora, cantidad;
    private Muestreo m;

    public MuestreoNuevoIntermedioFragment2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MuestreoNuevoIntermedioFragment2.
     */
    // TODO: Rename and change types and number of parameters
    public static MuestreoNuevoIntermedioFragment2 newInstance(String param1, String param2) {
        MuestreoNuevoIntermedioFragment2 fragment = new MuestreoNuevoIntermedioFragment2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

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
        View intermedio_fragment =  inflater.inflate(R.layout.fragment_muestreo_nuevo_intermedio_fragment2, container, false);

        this.crud = new Crud(this);
        Toast.makeText(getContext(), "revivi", Toast.LENGTH_SHORT).show();
        generarCrud();


        return intermedio_fragment;
    }

    private void generarCrud() {
        Bundle bundle = getArguments();
        id_bitacora = bundle.getString("id_bitacora");
        id_muestreo = bundle.getString("id_muestreo");
        nombre_bitacora = bundle.getString("nombre_bitacora");
        m= (Muestreo) bundle.getSerializable("muestreo");
        cantidad = bundle.getString("cantidad");


        crud.actualizarCantidadMuestreos(id_bitacora, cantidad);
        crud.insertarMuestreo(m, id_bitacora, nombre_bitacora);
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
            try {

            }catch (Exception e){
                //Toast.makeText(context, ""+e.toString(), Toast.LENGTH_SHORT).show();
                Log.e("ERROR", e.getMessage().toString());
                throw new RuntimeException(context.toString()
                        + " must implement OnFragmentInteractionListener");
            }
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
}
