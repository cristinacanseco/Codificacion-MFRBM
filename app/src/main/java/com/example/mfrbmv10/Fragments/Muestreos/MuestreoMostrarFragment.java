package com.example.mfrbmv10.Fragments.Muestreos;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mfrbmv10.Modelos.Muestreo;
import com.example.mfrbmv10.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MuestreoMostrarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MuestreoMostrarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MuestreoMostrarFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ImageView img_mm;
    public TextView tv_nombre_mm, tv_fecha_mm, tv_hora_mm, tv_coordenadas_mm, tv_localizacion_mm;
    private String id_bitacora, id_muestreo, nombre_bitacora;
    private Muestreo m;

    public MuestreoMostrarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MuestreoMostrarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MuestreoMostrarFragment newInstance(String param1, String param2) {
        MuestreoMostrarFragment fragment = new MuestreoMostrarFragment();
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
        // Inflate the layout for this fragment
        View muestreo_mostrar_fragment = inflater.inflate(R.layout.fragment_muestreo_mostrar, container, false);

        img_mm = muestreo_mostrar_fragment.findViewById(R.id.img_mm);
        tv_nombre_mm = muestreo_mostrar_fragment.findViewById(R.id.tv_nombre_mm);
        tv_fecha_mm = muestreo_mostrar_fragment.findViewById(R.id.tv_fecha_mm);
        tv_hora_mm = muestreo_mostrar_fragment.findViewById(R.id.tv_hora_mm);
        tv_coordenadas_mm = muestreo_mostrar_fragment.findViewById(R.id.tv_coordenadas_mm);
        tv_localizacion_mm = muestreo_mostrar_fragment.findViewById(R.id.tv_localizacion_mm);

        Bundle bundle = getArguments();
        id_bitacora = bundle.getString("id_bitacora");
        id_muestreo = bundle.getString("id_muestreo");
        nombre_bitacora = bundle.getString("nombre_bitacora");
        m= (Muestreo) bundle.getSerializable("muestreo");
        obtenerDatos(m);

        return muestreo_mostrar_fragment;
    }

    private void obtenerDatos(Muestreo muestreo) {
        img_mm.setImageResource(R.drawable.flores1);
        tv_nombre_mm.setText(muestreo.getNombre_mtr());
        tv_fecha_mm.setText(muestreo.getFecha_mtr());
        tv_hora_mm.setText(muestreo.getHora_mtr());
        tv_coordenadas_mm.setText(muestreo.getCoordenadas_mtr());
        tv_localizacion_mm.setText(muestreo.getUbicacion_mtr());
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
}
