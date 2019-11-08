package com.example.mfrbmv10.Fragments.Muestreos.MuestreoEditar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.mfrbmv10.Modelos.Muestreo;
import com.example.mfrbmv10.R;

public class TabMuestreoEditarDimensiones extends Fragment {

    public ImageView img_med;
    public TextView tv_nombre_med, tv_dimensiones_med;
    private String id_bitacora, id_muestreo, nombre_bitacora;
    private Muestreo m;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View muestreo_mostrar_fragment =  inflater.inflate(R.layout.tab_muestreo_editar_dimensiones, container, false);

        img_med = muestreo_mostrar_fragment.findViewById(R.id.img_med);
        tv_nombre_med = muestreo_mostrar_fragment.findViewById(R.id.tv_nombre_med);
        tv_dimensiones_med = muestreo_mostrar_fragment.findViewById(R.id.tv_dimensiones_med);

        Bundle bundle = getArguments();
        id_bitacora = bundle.getString("id_bitacora");
        id_muestreo = bundle.getString("id_muestreo");
        nombre_bitacora = bundle.getString("nombre_bitacora");
        m= (Muestreo) bundle.getSerializable("muestreo");
        obtenerDatos(m);

        return muestreo_mostrar_fragment;
    }

    private void obtenerDatos(Muestreo muestreo) {
        //img_med.setImageResource(R.drawable.flores1);
        //Glide.with(getContext()).load(muestreo.getImagen_mtr()).into(img_med);
        verificarImagen(muestreo.getImagen_mtr());
        tv_nombre_med.setText(muestreo.getNombre_mtr());
        tv_dimensiones_med.setText(obtenerDimensiones(muestreo));
    }

    public void verificarImagen(String imagen){
        if(imagen == ""){
            img_med.setImageResource(R.drawable.flores1);
        }else{
            Glide.with(getContext())
                    .load(imagen)
                    //.apply(new RequestOptions().override(80, 80))
                    .into(img_med);
        }
    }

    private String obtenerDimensiones(Muestreo muestreo) {
        String dimensiones = "";
        for (int i=0; i<muestreo.getDimension_mtr().size(); i++){
            dimensiones += muestreo.getDimension_mtr().get(i) + "\n";
        }
        return dimensiones;
    }
}
