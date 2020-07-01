package com.example.mfrbmv10.Sesiones;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mfrbmv10.Extras.Dialogo;
import com.example.mfrbmv10.FirebaseMotor.Crud;
import com.example.mfrbmv10.FirebaseMotor.SesionesFirestore;
import com.example.mfrbmv10.R;

public class IniciarSesion extends AppCompatActivity implements View.OnClickListener {
    public ImageView iv_regresar_is;
    public TextView tv_registrar_is, et_clave_is,et_correo_is, tv_olvidar_clave;
    public Button btn_ingresar_is;
    public SesionesFirestore crud;
    public String correo, clave;
    private ProgressBar pb_is;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        crud = new SesionesFirestore(IniciarSesion.this);

        iv_regresar_is = findViewById(R.id.iv_regresar_is);
        iv_regresar_is.setOnClickListener(this);

        tv_registrar_is = findViewById(R.id.tv_registrar_is);
        tv_registrar_is.setOnClickListener(this);

        tv_olvidar_clave = findViewById(R.id.tv_olvidar_clave);
        tv_olvidar_clave.setOnClickListener(this);

        btn_ingresar_is = findViewById(R.id.btn_ingresar_is);
        btn_ingresar_is.setOnClickListener(this);

        pb_is = findViewById(R.id.pb_is);
        pb_is.setVisibility(View.INVISIBLE);

        et_correo_is = findViewById(R.id.et_correo_is);
        et_clave_is = findViewById(R.id.et_clave_is);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_regresar_is:
                startActivity(new Intent(IniciarSesion.this, MainActivity.class));
                break;
            case R.id.tv_registrar_is:
                startActivity(new Intent(IniciarSesion.this, Registro.class));
                break;
            case R.id.btn_ingresar_is:
                correo = et_correo_is.getText().toString().trim();
                clave = et_clave_is.getText().toString().trim();
                cambiarVisibilidad(true);
                iniciarSesion();
                finish();
                break;
            case R.id.tv_olvidar_clave:
                Dialogo dialogo = new Dialogo();
                dialogo.show(getSupportFragmentManager(), "dialogo2");
                break;
            default:
                break;
        }
    }

    private void iniciarSesion() {
        if(correo.isEmpty() && clave.isEmpty()){
            Toast.makeText(IniciarSesion.this, "Faltan todos los campos por completar", Toast.LENGTH_SHORT).show();
            cambiarVisibilidad(false);
        }else if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            et_correo_is.setError("Ingresa un correo electrónico válido");
            et_clave_is.requestFocus();
            cambiarVisibilidad(false);
        }
        else if(clave.isEmpty()){
            et_clave_is.setError("Ingresa una contraseña");
            et_clave_is.requestFocus();
            cambiarVisibilidad(false);
        }else{
            crud.iniciarSesion(correo, clave,btn_ingresar_is, pb_is );
        }
    }

    public void cambiarVisibilidad(boolean pb){
        if(pb){
            btn_ingresar_is.setVisibility(View.INVISIBLE);
            pb_is.setVisibility(View.VISIBLE);
        }else{
            btn_ingresar_is.setVisibility(View.VISIBLE);
            pb_is.setVisibility(View.INVISIBLE);
        }
    }
}
