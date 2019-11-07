package com.example.mfrbmv10.Fragments.Bitacoras;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mfrbmv10.Extras.Localizacion;
import com.example.mfrbmv10.Extras.Timestamp;
import com.example.mfrbmv10.FirebaseMotor.Crud;
import com.example.mfrbmv10.Modelos.Bitacora;
import com.example.mfrbmv10.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BitacoraNuevaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BitacoraNuevaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BitacoraNuevaFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private EditText et_nombre_btc_nb, et_descripcion_btc_nb;
    private Crud crud;
    private Button btn_guardar_nb;
    private Timestamp timestamp;
    private Localizacion localizacion;

    public BitacoraNuevaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BitacoraNuevaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BitacoraNuevaFragment newInstance(String param1, String param2) {
        BitacoraNuevaFragment fragment = new BitacoraNuevaFragment();
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

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View bn_fragment =  inflater.inflate(R.layout.fragment_bitacora_nueva, container, false);

        this.crud = new Crud(this);
        timestamp = new Timestamp();
        localizacion = new Localizacion(this);

        et_nombre_btc_nb = bn_fragment.findViewById(R.id.et_nombre_btc_nb);
        et_descripcion_btc_nb = bn_fragment.findViewById(R.id.et_descripcion_btc_nb);
        btn_guardar_nb = bn_fragment.findViewById(R.id.btn_guardar_nb);
        btn_guardar_nb.setOnClickListener(this);

        //Bundle bundle = getArguments();
        //Toast.makeText(this.getContext(), ""+bundle.getString("mensaje"), Toast.LENGTH_SHORT).show();

        return bn_fragment ;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_guardar_nb:
                if (et_nombre_btc_nb.getText().toString().trim().isEmpty()) {
                    crud.createAlert("Error", "Agrega un nombre a la bitácora. \nVuelve a intentarlo\n ", "OK");
                }else {
                    crud.insertarBitacora(generarDatosBitacora());
                }
                break;
            default:
                break;
        }

    }

    private Bitacora generarDatosBitacora() {

        String nombre_btc = et_nombre_btc_nb.getText().toString();
        String imagen_btc = "R.drawable.flores1";
        String fecha_btc = timestamp.obtenerFecha();
        String hora_btc = timestamp.obtenerHora();
        String ubicacion_btc = localizacion.getUbicacion();
        String coordenadas_btc = localizacion.getCoordenadas();
        String cantidad_btc = "1";
        String descripcion_btc = et_descripcion_btc_nb.getText().toString();

        return new Bitacora(nombre_btc,imagen_btc,fecha_btc,hora_btc,ubicacion_btc,coordenadas_btc,cantidad_btc,descripcion_btc);
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
