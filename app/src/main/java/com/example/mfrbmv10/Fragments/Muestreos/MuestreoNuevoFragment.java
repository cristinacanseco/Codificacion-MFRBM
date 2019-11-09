package com.example.mfrbmv10.Fragments.Muestreos;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.mfrbmv10.Extras.Localizacion;
import com.example.mfrbmv10.Extras.Medicion;
import com.example.mfrbmv10.Extras.Timestamp;
import com.example.mfrbmv10.FirebaseMotor.Crud;
import com.example.mfrbmv10.Fragments.Bitacoras.BitacoraFragment;
import com.example.mfrbmv10.Imagen.CamaraDibujo;
import com.example.mfrbmv10.Imagen.CamaraIntento2;
import com.example.mfrbmv10.Modelos.ColorMuestreo;
import com.example.mfrbmv10.Modelos.Muestreo;
import com.example.mfrbmv10.R;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MuestreoNuevoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MuestreoNuevoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MuestreoNuevoFragment extends Fragment implements View.OnClickListener,Serializable{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText et_nombre_btc_nm;
    private Crud crud;
    private Button btn_guardar_nm;
    public String id_bitacora, nombreBitacora;
    private int cantidad_btc;
    public Timestamp timestamp;
    private Localizacion localizacion;

    private OnFragmentInteractionListener mListener;

    public MuestreoNuevoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MuestreoNuevoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MuestreoNuevoFragment newInstance(String param1, String param2) {
        MuestreoNuevoFragment fragment = new MuestreoNuevoFragment();
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
        View mn_fragment =  inflater.inflate(R.layout.fragment_muestreo_nuevo, container, false);

        this.crud = new Crud(this);
        timestamp = new Timestamp();
        localizacion = new Localizacion(this);

        et_nombre_btc_nm = mn_fragment.findViewById(R.id.et_nombre_mtr_nm);
        btn_guardar_nm = mn_fragment.findViewById(R.id.btn_guardar_nm);
        btn_guardar_nm.setOnClickListener(this);

        Bundle bundle = getArguments();
        id_bitacora = bundle.getString("id_bitacora");
        nombreBitacora = bundle.getString("nombre_bitacora");
        cantidad_btc = bundle.getInt("cantidad_btc");
        //Toast.makeText(this.getContext(), ""+id_bitacora, Toast.LENGTH_SHORT).show();


        return mn_fragment ;
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

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_guardar_nm:
                if (et_nombre_btc_nm.getText().toString().trim().isEmpty()) {
                    crud.createAlert("Error", "Agrega un nombre al muestreo. \nVuelve a intentarlo\n ", "OK");
                }else {
                    int c = cantidad_btc+1;
                    String cantidad = ""+c;
                    irACamara(cantidad);
                    //irAMedicion(cantidad);
                }
                break;
            default:
                break;
        }
    }

    private void irACamara(String cantidad) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("muestreo", generarDatosMuestreo());
        //bundle.putSerializable("contexto",this);

        Intent intent = new Intent(getContext(), CamaraDibujo.class);
        intent.putExtra("id_bitacora", id_bitacora);
        intent.putExtra("nombre_bitacora", nombreBitacora);
        intent.putExtra("muestreo",bundle);
        intent.putExtra("cantidad", cantidad);
        startActivity(intent);

    }

    private void irAMedicion(String cantidad) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("muestreo", generarDatosMuestreo());

        Intent intent = new Intent(getContext(), Medicion.class);
        intent.putExtra("id_bitacora", id_bitacora);
        intent.putExtra("nombre_bitacora", nombreBitacora);
        intent.putExtra("muestreo",bundle);
        intent.putExtra("cantidad", cantidad);
        startActivity(intent);

    }

    private Muestreo generarDatosMuestreo() {
        String nombre_mtr = et_nombre_btc_nm.getText().toString();
        String imagen_mtr = "";
        String forma_mtr = "Pilado";
        String textura_mtr= "Cartilaginoso";
        ArrayList<Integer> color_mtr = null;
        ArrayList<String> dimension_mtr = null;
        String ubicacion_mtr = localizacion.getUbicacion();
        String coordenadas_mtr = localizacion.getCoordenadas();
        String fecha_mtr = timestamp.obtenerFecha();
        String hora_mtr = timestamp.obtenerHora();
        return new Muestreo(nombre_mtr,imagen_mtr,fecha_mtr,hora_mtr,ubicacion_mtr,coordenadas_mtr, forma_mtr,textura_mtr,color_mtr,dimension_mtr);
    }

    public void regresar(){
        BitacoraFragment bf=new BitacoraFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id_bitacora", id_bitacora);
        bundle.putString("nombre_bitacora", nombreBitacora);
        bf.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_main,bf)
                .addToBackStack(null)
                .commit();
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
