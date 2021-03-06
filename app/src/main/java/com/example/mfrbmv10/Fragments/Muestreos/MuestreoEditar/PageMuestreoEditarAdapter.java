package com.example.mfrbmv10.Fragments.Muestreos.MuestreoEditar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.mfrbmv10.Fragments.Muestreos.MuestreoEditar.TabMuestreoEditarColores;
import com.example.mfrbmv10.Fragments.Muestreos.MuestreoEditar.TabMuestreoEditarDescripcion;
import com.example.mfrbmv10.Fragments.Muestreos.MuestreoEditar.TabMuestreoEditarDimensiones;
import com.example.mfrbmv10.Modelos.Muestreo;

public class PageMuestreoEditarAdapter extends FragmentStatePagerAdapter {

    int contarTab;
    private String id_bitacora, id_muestreo, nombre_bitacora;
    private Muestreo m;

    public PageMuestreoEditarAdapter(FragmentManager fm, int contarTab, String id_bitacora, String id_muestreo, String nombre_bitacora, Muestreo m) {
        super(fm);
        this.contarTab = contarTab;
        this.id_bitacora = id_bitacora;
        this.id_muestreo = id_muestreo;
        this.nombre_bitacora = nombre_bitacora;
        this.m = m;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        Bundle bundle = new Bundle();
        bundle.putString("id_bitacora", id_bitacora);
        bundle.putString("id_muestreo", id_muestreo);
        bundle.putString("nombre_bitacora", nombre_bitacora);
        bundle.putSerializable("muestreo", m);

        switch (position){
            case 0:
                TabMuestreoEditarDescripcion tmd = new TabMuestreoEditarDescripcion();
                tmd.setArguments(bundle);
                return tmd;
            case 1:
                TabMuestreoEditarColores tmc = new TabMuestreoEditarColores();
                tmc.setArguments(bundle);
                return tmc;
            case 2:
                TabMuestreoEditarDimensiones tmdm = new TabMuestreoEditarDimensiones();
                tmdm.setArguments(bundle);
                return  tmdm;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return contarTab;
    }
}
