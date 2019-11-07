package com.example.mfrbmv10.Fragments.Muestreos.MuestreoEditar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mfrbmv10.Adaptadores.ColorAdapter;
import com.example.mfrbmv10.Modelos.ColorMuestreo;
import com.example.mfrbmv10.Modelos.Muestreo;
import com.example.mfrbmv10.R;

import java.util.ArrayList;

public class TabMuestreoEditarColores extends Fragment {

    public ImageView img_mm;
    public TextView tv_nombre_mm, tv_colores_mec;
    private String id_bitacora, id_muestreo, nombre_bitacora;
    private Muestreo m;
    private RecyclerView rv_colores_me;
    private ColorAdapter colorAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View muestreo_mostrar_fragment =  inflater.inflate(R.layout.tab_muestreo_editar_colores, container, false);

        img_mm = muestreo_mostrar_fragment.findViewById(R.id.img_mec);
        tv_nombre_mm = muestreo_mostrar_fragment.findViewById(R.id.tv_nombre_mec);
        tv_colores_mec = muestreo_mostrar_fragment.findViewById(R.id.tv_colores_mec);

        rv_colores_me = muestreo_mostrar_fragment.findViewById(R.id.rv_colores_me);
        rv_colores_me.setHasFixedSize(true);
        rv_colores_me.setLayoutManager(new LinearLayoutManager(getContext()));

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

        if(m.getColor_mtr().size() != 0) {
            colorAdapter = new ColorAdapter(getContext(), m.getColor_mtr());
            rv_colores_me.setAdapter(colorAdapter);
        }
    }
}
