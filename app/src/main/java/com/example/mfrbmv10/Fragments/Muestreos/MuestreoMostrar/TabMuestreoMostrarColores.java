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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mfrbmv10.Adaptadores.ColorAdapter;
import com.example.mfrbmv10.Modelos.Muestreo;
import com.example.mfrbmv10.R;

public class TabMuestreoMostrarColores extends Fragment {

    public ImageView img_mm;
    public TextView tv_nombre_mm, tv_colores_mmc;
    private String id_bitacora, id_muestreo, nombre_bitacora;
    private Muestreo m;
    private RecyclerView rv_colores_mm;
    private ColorAdapter colorAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View muestreo_mostrar_fragment =  inflater.inflate(R.layout.tab_muestreo_mostrar_colores, container, false);

        img_mm = muestreo_mostrar_fragment.findViewById(R.id.img_mmc);
        tv_nombre_mm = muestreo_mostrar_fragment.findViewById(R.id.tv_nombre_mmc);
        tv_colores_mmc = muestreo_mostrar_fragment.findViewById(R.id.tv_colores_mmc);

        rv_colores_mm = muestreo_mostrar_fragment.findViewById(R.id.rv_colores_mm);
        rv_colores_mm.setHasFixedSize(true);
        rv_colores_mm.setLayoutManager(new LinearLayoutManager(getContext()));

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
        if(m.getColor_mtr().size() !=0) {
            colorAdapter = new ColorAdapter(getContext(), m.getColor_mtr());
            rv_colores_mm.setAdapter(colorAdapter);
        }

    }
}
