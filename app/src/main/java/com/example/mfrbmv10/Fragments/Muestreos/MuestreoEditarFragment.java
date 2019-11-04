package com.example.mfrbmv10.Fragments.Muestreos;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mfrbmv10.FirebaseMotor.Crud;
import com.example.mfrbmv10.Modelos.Muestreo;
import com.example.mfrbmv10.R;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MuestreoEditarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MuestreoEditarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MuestreoEditarFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ImageView img_me;
    public EditText tv_nombre_me;
    public TextView tv_fecha_me, tv_hora_me, tv_coordenadas_me, tv_localizacion_me;
    public Crud crud;
    private Button btn_editar_me;
    private String id_bitacora, id_muestreo, nombre_bitacora;
    private Muestreo m;

    public MuestreoEditarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MuestreoEditarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MuestreoEditarFragment newInstance(String param1, String param2) {
        MuestreoEditarFragment fragment = new MuestreoEditarFragment();
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
        View muestreo_editar_fragment =  inflater.inflate(R.layout.fragment_muestreo_editar, container, false);

        crud = new Crud(this);

        img_me = muestreo_editar_fragment.findViewById(R.id.img_me);
        tv_nombre_me = muestreo_editar_fragment.findViewById(R.id.tv_nombre_me);
        tv_fecha_me = muestreo_editar_fragment.findViewById(R.id.tv_fecha_me);
        tv_hora_me = muestreo_editar_fragment.findViewById(R.id.tv_hora_me);
        tv_coordenadas_me = muestreo_editar_fragment.findViewById(R.id.tv_coordenadas_me);
        tv_localizacion_me = muestreo_editar_fragment.findViewById(R.id.tv_localizacion_me);
        btn_editar_me = muestreo_editar_fragment.findViewById(R.id.btn_editar_me);
        btn_editar_me.setOnClickListener(this);

        Bundle bundle = getArguments();
        id_bitacora = bundle.getString("id_bitacora");
        id_muestreo = bundle.getString("id_muestreo");
        nombre_bitacora = bundle.getString("nombre_bitacora");
        m= (Muestreo) bundle.getSerializable("muestreo");
        obtenerDatos(m);

        return muestreo_editar_fragment;
    }

    private void obtenerDatos(Muestreo muestreo) {
        img_me.setImageResource(R.drawable.flores1);
        tv_nombre_me.setText(muestreo.getNombre_mtr());
        tv_fecha_me.setText(muestreo.getFecha_mtr());
        tv_hora_me.setText(muestreo.getHora_mtr());
        tv_coordenadas_me.setText(muestreo.getCoordenadas_mtr());
        tv_localizacion_me.setText(muestreo.getUbicacion_mtr());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_editar_me:
                actualizarDatos();
                break;
            default:
                break;
        }
    }

    private void actualizarDatos() {
        String nombre_mtr = tv_nombre_me.getText().toString();
        String imagen_mtr = "R.drawable.flores1";
        String fecha_mtr = tv_fecha_me.getText().toString();
        String hora_mtr = tv_hora_me.getText().toString();
        String ubicacion_mtr = tv_localizacion_me.getText().toString();
        String coordenadas_mtr = tv_coordenadas_me.getText().toString();
        String forma_mtr = "algo";
        String textura_mtr = "algo2";
        String color_mtr = "colres";
        String dimension_mtr = "dimensiones";

        Muestreo muestreo = new Muestreo(nombre_mtr,imagen_mtr,fecha_mtr, hora_mtr,ubicacion_mtr,coordenadas_mtr,forma_mtr,textura_mtr,color_mtr,dimension_mtr);

        crud.editarDatosMuestreo(muestreo, id_bitacora, id_muestreo, nombre_bitacora);
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
