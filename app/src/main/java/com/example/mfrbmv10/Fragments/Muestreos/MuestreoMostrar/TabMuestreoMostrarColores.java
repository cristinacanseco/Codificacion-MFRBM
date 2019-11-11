package com.example.mfrbmv10.Fragments.Muestreos.MuestreoMostrar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mfrbmv10.Adaptadores.ColorAdapter;
import com.example.mfrbmv10.Modelos.Colores;
import com.example.mfrbmv10.Modelos.Muestreo;
import com.example.mfrbmv10.R;

import java.util.ArrayList;

public class TabMuestreoMostrarColores extends Fragment {

    public ImageView img_mm;
    public TextView tv_nombre_mm, tv_colores_mmc;
    private String id_bitacora, id_muestreo, nombre_bitacora;
    private Muestreo m;

    private TextView txtVibrant,txtDarkVibrant,txtLightVibrant;
    private TextView txtMuted,txtDarkMuted,txtLightMuted;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View muestreo_mostrar_fragment =  inflater.inflate(R.layout.tab_muestreo_mostrar_colores, container, false);

        img_mm = muestreo_mostrar_fragment.findViewById(R.id.img_mmc);
        tv_nombre_mm = muestreo_mostrar_fragment.findViewById(R.id.tv_nombre_mmc);
        tv_colores_mmc = muestreo_mostrar_fragment.findViewById(R.id.tv_colores_mmc);

        txtVibrant = (TextView)muestreo_mostrar_fragment.findViewById(R.id.txtVibrant);
        txtDarkVibrant = (TextView)muestreo_mostrar_fragment.findViewById(R.id.txtDarkVibrant);
        txtLightVibrant = (TextView)muestreo_mostrar_fragment.findViewById(R.id.txtLightVibrant);
        txtMuted = (TextView)muestreo_mostrar_fragment.findViewById(R.id.txtMuted);
        txtDarkMuted = (TextView)muestreo_mostrar_fragment.findViewById(R.id.txtDarkMuted);
        txtLightMuted = (TextView)muestreo_mostrar_fragment.findViewById(R.id.txtLightMuted);

        Bundle bundle = getArguments();
        id_bitacora = bundle.getString("id_bitacora");
        id_muestreo = bundle.getString("id_muestreo");
        nombre_bitacora = bundle.getString("nombre_bitacora");
        m= (Muestreo) bundle.getSerializable("muestreo");
        obtenerDatos(m);

        return muestreo_mostrar_fragment;
    }

    private void obtenerDatos(Muestreo muestreo) {
        verificarImagen(muestreo.getImagen_mtr());
        tv_nombre_mm.setText(muestreo.getNombre_mtr());
        ArrayList<Integer> p = (ArrayList<Integer>) muestreo.getColor_mtr();
        if(p!= null || p.size() <=0) {
            setTextViewSwatch(txtVibrant, p.get(0), p.get(1));
            setTextViewSwatch(txtDarkVibrant, p.get(2), p.get(3));
            setTextViewSwatch(txtLightVibrant, p.get(5), p.get(5));
            setTextViewSwatch(txtMuted, p.get(6), p.get(7));
            setTextViewSwatch(txtDarkMuted, p.get(8), p.get(9));
            setTextViewSwatch(txtLightMuted, p.get(10), p.get(11));
        }
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

    private void setTextViewSwatch(TextView tview, Integer swatch, Integer color) {
        if(swatch != null) {
            tview.setBackgroundColor(swatch);
            tview.setTextColor(color);
            Colores c = new Colores(swatch);
            int r = c.getRed();
            int g = c.getGreen();
            int b = c.getBlue();
            String hexColor = String.format( "#%02x%02x%02x", r, g, b ).toUpperCase();
            tview.setText("\t\t\tRGB("+r+","+g+","+b+")\t\t\t" +hexColor );
        }
        else {
            tview.setBackgroundColor(Color.BLACK);
            tview.setTextColor(Color.WHITE);
            tview.setText("(Sin definir)");
        }
    }
}
