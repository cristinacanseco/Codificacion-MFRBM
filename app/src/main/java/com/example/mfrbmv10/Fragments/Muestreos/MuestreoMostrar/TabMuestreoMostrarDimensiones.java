package com.example.mfrbmv10.Fragments.Muestreos.MuestreoMostrar;

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
import com.bumptech.glide.request.RequestOptions;
import com.example.mfrbmv10.Modelos.Muestreo;
import com.example.mfrbmv10.R;

public class TabMuestreoMostrarDimensiones extends Fragment {

    public ImageView img_mm;
    public TextView tv_nombre_mm, tv_dimensiones_mmd;
    private String id_bitacora, id_muestreo, nombre_bitacora;
    private Muestreo m;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View muestreo_mostrar_fragment =  inflater.inflate(R.layout.tab_muestreo_mostrar_dimensiones, container, false);

        img_mm = muestreo_mostrar_fragment.findViewById(R.id.img_mmd);
        tv_nombre_mm = muestreo_mostrar_fragment.findViewById(R.id.tv_nombre_mmd);
        tv_dimensiones_mmd = muestreo_mostrar_fragment.findViewById(R.id.tv_dimensiones_mmd);

        Bundle bundle = getArguments();
        id_bitacora = bundle.getString("id_bitacora");
        id_muestreo = bundle.getString("id_muestreo");
        nombre_bitacora = bundle.getString("nombre_bitacora");
        m= (Muestreo) bundle.getSerializable("muestreo");
        obtenerDatos(m);

        return muestreo_mostrar_fragment;
    }

    private void obtenerDatos(Muestreo muestreo) {
        //img_mm.setImageResource(R.drawable.flores1);
        //Glide.with(getContext()).load(muestreo.getImagen_mtr()).into(img_mm);
        verificarImagen(muestreo.getImagen_mtr());
        tv_nombre_mm.setText(muestreo.getNombre_mtr());
        tv_dimensiones_mmd.setText(obtenerDimensiones(muestreo));
    }

    public void verificarImagen(String imagen){
        if(imagen == ""){
            img_mm.setImageResource(R.drawable.flores1);
        }else{
            Glide.with(getContext())
                    .load(imagen)
                    .apply(new RequestOptions().override(getResources().getDisplayMetrics().widthPixels, 200))
                    .into(img_mm);
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
