package com.example.mfrbmv10.Fragments.Bitacoras;

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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mfrbmv10.FirebaseMotor.Crud;
import com.example.mfrbmv10.Modelos.Bitacora;
import com.example.mfrbmv10.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BitacoraEditarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BitacoraEditarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BitacoraEditarFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Crud crud;
    public TextView tv_fecha_bp, tv_hora_bp, tv_localizacion_bp, tv_muestreos_bp,tv_coordenadas_be;
    public ImageView img_bp;
    public EditText et_nombre_bp, et_descripcion_bp;
    public Button btn_editar_be;
    private String id_bitacora;
    private Bitacora b;

    public BitacoraEditarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BitacoraEditarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BitacoraEditarFragment newInstance(String param1, String param2) {
        BitacoraEditarFragment fragment = new BitacoraEditarFragment();
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
        View bitacora_editar_fragment =  inflater.inflate(R.layout.fragment_bitacora_editar, container, false);

        crud = new Crud(this);

        et_nombre_bp = bitacora_editar_fragment.findViewById(R.id.tv_nombre_be);
        tv_fecha_bp = bitacora_editar_fragment.findViewById(R.id.tv_fecha_be);
        img_bp = bitacora_editar_fragment.findViewById(R.id.img_be);
        tv_localizacion_bp = bitacora_editar_fragment.findViewById(R.id.tv_localizacion_be);
        tv_hora_bp = bitacora_editar_fragment.findViewById(R.id.tv_hora_be);
        tv_coordenadas_be = bitacora_editar_fragment.findViewById(R.id.tv_coordenadas_be);
        tv_muestreos_bp = bitacora_editar_fragment.findViewById(R.id.tv_muestreos_be);
        et_descripcion_bp = bitacora_editar_fragment.findViewById(R.id.et_descripcion_be);
        btn_editar_be = bitacora_editar_fragment.findViewById(R.id.btn_editar_be);
        btn_editar_be.setOnClickListener(this);

        Bundle bundle = getArguments();
        id_bitacora = bundle.getString("id_bitacora");
        Toast.makeText(this.getContext(), ""+id_bitacora, Toast.LENGTH_SHORT).show();
        b = (Bitacora) bundle.getSerializable("bitacora");
        obtenerDatos(b);

        return bitacora_editar_fragment ;
    }

    private void obtenerDatos(Bitacora model) {
        et_nombre_bp.setText(model.getNombre_btc());
        tv_fecha_bp.setText(model.getFecha_btc());
        //img_bp.setImageResource(R.drawable.flores1);
        /*Glide.with(getContext())
                .load(model.getImagen_btc())
                .into(img_bp);*/
        verificarImagen(model.getImagen_btc());
        tv_hora_bp.setText(model.getHora_btc());
        tv_localizacion_bp.setText(model.getUbicacion_btc());
        tv_coordenadas_be.setText(model.getCoordenadas_btc());
        tv_muestreos_bp.setText(model.getCantidad_btc());
        et_descripcion_bp.setText(model.getDescripcion_btc());
    }

    public void verificarImagen(String imagen){
        if(imagen == ""){
            img_bp.setImageResource(R.drawable.flores1);
        }else{
            Glide.with(getContext())
                    .load(imagen)
                    //.apply(new RequestOptions().override(80, 80))
                    .into(img_bp);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_editar_be:
                actualizarDatos(id_bitacora);
                break;
            default:
                break;
        }
    }

    private void actualizarDatos(String id_bitacora) {
        String nombre_btc = (String) et_nombre_bp.getText().toString();
        String ubicacion_btc = (String) b.getUbicacion_btc();
        String coordenadas_btc = b.getCoordenadas_btc();
        String cantidad_btc= (String) b.getCantidad_btc();
        String fecha_btc= (String) b.getFecha_btc();
        String hora_btc= (String) b.getHora_btc();
        String imagen_btc=(String) b.getImagen_btc();
        String descripcion_btc= (String) et_descripcion_bp.getText().toString();

        Bitacora bitacora = new Bitacora(nombre_btc,imagen_btc,fecha_btc,hora_btc,ubicacion_btc,coordenadas_btc,cantidad_btc,descripcion_btc);

        crud.editarDatosBitacora(bitacora, id_bitacora);
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
