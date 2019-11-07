package com.example.mfrbmv10.Fragments.Muestreos.MuestreoEditar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mfrbmv10.Adaptadores.FormaAdapter;
import com.example.mfrbmv10.Adaptadores.TexturaAdapter;
import com.example.mfrbmv10.FirebaseMotor.Crud;
import com.example.mfrbmv10.Modelos.Muestreo;
import com.example.mfrbmv10.R;

public class TabMuestreoEditarDescripcion extends Fragment  implements View.OnClickListener, TexturaAdapter.OnItemSelectedListener, FormaAdapter.OnItemSelectedListener{

    public ImageView img_me;
    public EditText tv_nombre_me;
    public TextView tv_fecha_me, tv_hora_me, tv_coordenadas_me, tv_localizacion_me;
    public Crud crud;
    private Button btn_editar_me;
    private String id_bitacora, id_muestreo, nombre_bitacora, forma, textura;
    private Muestreo m;
    private Spinner spinner_forma,spinner_textura;
    private FormaAdapter formaAdapter;
    private TexturaAdapter texturaAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View muestreo_editar_fragment =  inflater.inflate(R.layout.tab_muestreo_editar_descripcion, container, false);

        crud = new Crud(this);

        img_me = muestreo_editar_fragment.findViewById(R.id.img_me);
        tv_nombre_me = muestreo_editar_fragment.findViewById(R.id.tv_nombre_me);
        tv_fecha_me = muestreo_editar_fragment.findViewById(R.id.tv_fecha_me);
        tv_hora_me = muestreo_editar_fragment.findViewById(R.id.tv_hora_me);
        tv_coordenadas_me = muestreo_editar_fragment.findViewById(R.id.tv_coordenadas_me);
        tv_localizacion_me = muestreo_editar_fragment.findViewById(R.id.tv_localizacion_me);
        spinner_forma = muestreo_editar_fragment.findViewById(R.id.spinner_forma_me);
        spinner_textura = muestreo_editar_fragment.findViewById(R.id.spinner_textura_me);
        btn_editar_me = muestreo_editar_fragment.findViewById(R.id.btn_editar_me);
        btn_editar_me.setOnClickListener(this);

        Bundle bundle = getArguments();
        id_bitacora = bundle.getString("id_bitacora");
        id_muestreo = bundle.getString("id_muestreo");
        nombre_bitacora = bundle.getString("nombre_bitacora");
        m= (Muestreo) bundle.getSerializable("muestreo");

        obtenerDatos(m);

        return muestreo_editar_fragment;

    }

    private void obtenerDatos(Muestreo muestreo) {
        img_me.setImageResource(R.drawable.flores1);
        tv_nombre_me.setText(muestreo.getNombre_mtr());
        tv_fecha_me.setText(muestreo.getFecha_mtr());
        tv_hora_me.setText(muestreo.getHora_mtr());
        tv_coordenadas_me.setText(muestreo.getCoordenadas_mtr());
        tv_localizacion_me.setText(muestreo.getUbicacion_mtr());
        generarSpinners(muestreo.getForma_mtr(), muestreo.getTextura_mtr());
    }

    private void generarSpinners(String forma_mtr, String textura_mtr) {
        formaAdapter = new FormaAdapter(this, spinner_forma, forma_mtr);
        texturaAdapter = new TexturaAdapter(this, spinner_textura, textura_mtr);

        texturaAdapter.setOnItemSelectedListener(this);
        formaAdapter.setOnItemSelectedListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_editar_me:
                actualizarDatos();
                break;
            default:
                break;
        }
    }

    private void actualizarDatos() {
        m.setNombre_mtr(tv_nombre_me.getText().toString());
        m.setForma_mtr(forma);
        m.setTextura_mtr(textura);
        crud.editarDatosMuestreo(m, id_bitacora, id_muestreo, nombre_bitacora);
    }

    @Override
    public void onItemSelectedTextura(String documentSnapshot, int posicion) {
        textura = documentSnapshot;
    }

    @Override
    public void onItemSelectedForma(String documentSnapshot, int posicion) {
        forma = documentSnapshot;
    }
}
