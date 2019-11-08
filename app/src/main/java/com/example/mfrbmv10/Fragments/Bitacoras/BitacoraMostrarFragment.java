package com.example.mfrbmv10.Fragments.Bitacoras;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mfrbmv10.FirebaseMotor.Crud;
import com.example.mfrbmv10.Fragments.Muestreos.MuestreoFragment;
import com.example.mfrbmv10.Fragments.Muestreos.MuestreoNuevoFragment;
import com.example.mfrbmv10.Modelos.Bitacora;
import com.example.mfrbmv10.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BitacoraMostrarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BitacoraMostrarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BitacoraMostrarFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public TextView tv_nombre_bp, tv_fecha_bp, tv_coordenadas_bm, tv_localizacion_bm, tv_muestreos_bp,
            tv_descripcion_bp;
    public ImageView img_bp;
    private String id_bitacora;
    private FloatingActionButton fab_agregar_bm;

    public BitacoraMostrarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BitacoraMostrarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BitacoraMostrarFragment newInstance(String param1, String param2) {
        BitacoraMostrarFragment fragment = new BitacoraMostrarFragment();
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
        View bitacora_mostrar_fragment = inflater.inflate(R.layout.fragment_bitacora_mostrar, container, false);

        tv_nombre_bp = bitacora_mostrar_fragment.findViewById(R.id.tv_nombre_bm);
        tv_fecha_bp = bitacora_mostrar_fragment.findViewById(R.id.tv_fecha_bm);
        img_bp = bitacora_mostrar_fragment.findViewById(R.id.img_bm);
        tv_coordenadas_bm = bitacora_mostrar_fragment.findViewById(R.id.tv_coordenadas_bm);
        tv_localizacion_bm = bitacora_mostrar_fragment.findViewById(R.id.tv_localizacion_bm);
        tv_muestreos_bp = bitacora_mostrar_fragment.findViewById(R.id.tv_muestreos_bm);
        tv_descripcion_bp = bitacora_mostrar_fragment.findViewById(R.id.tv_descripcion_bm);
        tv_muestreos_bp.setOnClickListener(this);
        fab_agregar_bm = bitacora_mostrar_fragment.findViewById(R.id.fab_agregar_bm);
        fab_agregar_bm.setOnClickListener(this);

        Bundle bundle = getArguments();
        id_bitacora = bundle.getString("id_bitacora");
        //Toast.makeText(this.getContext(), ""+id_bitacora, Toast.LENGTH_SHORT).show();
        Bitacora b = (Bitacora) bundle.getSerializable("bitacora");
        actualizarDatos(b);
        return bitacora_mostrar_fragment;
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

    private void actualizarDatos(Bitacora model) {
        tv_nombre_bp.setText(model.getNombre_btc());
        tv_fecha_bp.setText(model.getFecha_btc()+" | " + model.getHora_btc());
        //img_bp.setImageResource(R.drawable.flores1);
        /*Glide.with(getContext())
                .load(model.getImagen_btc())
                .into(img_bp);*/
        verificarImagen(model.getImagen_btc());
        tv_coordenadas_bm.setText(model.getCoordenadas_btc());
        tv_localizacion_bm.setText(model.getUbicacion_btc());
        tv_muestreos_bp.setText(model.getCantidad_btc() + " muestreo(s)");
        tv_descripcion_bp.setText(model.getDescripcion_btc());
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
            case R.id.tv_muestreos_bm:
                MuestreoFragment bmf=new MuestreoFragment();
                Bundle bundle = new Bundle();
                bundle.putString("id_bitacora", id_bitacora);
                bundle.putSerializable("nombre_bitacora", tv_nombre_bp.getText().toString());
                bundle.putString("cantidad", tv_muestreos_bp.getText().toString());
                bmf.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_main,bmf)
                        .addToBackStack(null)
                        .commit();
                break;
            default:
                break;
        }

    }

    private void generarPDF() throws IOException {
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300,600,1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Paint mPaint = new Paint();
        int x=20, y=25;
        page.getCanvas().drawText("Hola", x, y, mPaint);
        pdfDocument.finishPage(page);

        File mFilePath = crearDirectorioPublico("MFRBM");
        File mFile = null;

        boolean isCreada=mFilePath.exists();
        if(isCreada==false){
            isCreada=mFilePath.mkdirs();
        }

        if(isCreada==true) {
            String imagenFileName = "" + tv_nombre_bp.getText().toString().trim();
            mFile = File.createTempFile(imagenFileName, ".dfg", mFilePath);
        }

        try {
            pdfDocument.writeTo(new FileOutputStream(mFile));
        }catch (Exception e){
            Toast.makeText(getContext(), "Ups, algo sucedió mal"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public File crearDirectorioPublico(String nombreDirectorio) {
        File directorio = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), nombreDirectorio);
        if (!directorio.mkdirs())
            Log.e("" ,"Error: No se creo el directorio público");
        return directorio;
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
