package com.example.mfrbmv10.Sesiones;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mfrbmv10.FirebaseMotor.Crud;
import com.example.mfrbmv10.FirebaseMotor.SesionesFirestore;
import com.example.mfrbmv10.Modelos.Usuario;
import com.example.mfrbmv10.R;

public class Registro extends AppCompatActivity implements View.OnClickListener {

    private Button btn_registrate_r;
    private ImageView iv_regresar_r;
    private TextView tv_inicia_ru;
    private EditText et_clave2_r, et_clave_r, et_correo_r,et_nombre_r,et_apellido_r;
    private String correo, clave, clave2, nombre, apellido;
    private ProgressBar pb_r;

    private SesionesFirestore crud;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        crud = new SesionesFirestore(Registro.this);

        iv_regresar_r = findViewById(R.id.iv_regresar_r);
        iv_regresar_r.setOnClickListener(this);

        tv_inicia_ru = findViewById(R.id.tv_inicia_ru);
        tv_inicia_ru.setOnClickListener(this);

        btn_registrate_r = findViewById(R.id.btn_registrate_r);
        btn_registrate_r.setOnClickListener(this);

        pb_r = findViewById(R.id.pb_r);
        pb_r.setVisibility(View.INVISIBLE);

        et_nombre_r = findViewById(R.id.et_nombre_r);
        et_apellido_r = findViewById(R.id.et_apellido_r);
        et_correo_r = findViewById(R.id.et_correo_r);
        et_clave_r = findViewById(R.id.et_clave_r);
        et_clave2_r = findViewById(R.id.et_clave2_r);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_regresar_r:
                startActivity(new Intent(Registro.this, MainActivity.class));
                break;
            case R.id.tv_inicia_ru:
                startActivity(new Intent(Registro.this, IniciarSesion.class));
                break;
            case R.id.btn_registrate_r:
                cambiarVisibilidad(true);
                comprobarCampos();
                break;
            default:
                break;
        }
    }

    private void comprobarCampos() {
        correo = et_correo_r.getText().toString().trim();
        clave = et_clave_r.getText().toString().trim();
        clave2 = et_clave2_r.getText().toString().trim();
        nombre = et_nombre_r.getText().toString().trim();
        apellido = et_apellido_r.getText().toString().trim();

        if(correo.isEmpty() && clave.isEmpty() && nombre.isEmpty() && clave2.isEmpty() && apellido.isEmpty()){
            Toast.makeText(Registro.this, "Faltan todos los campos por completar", Toast.LENGTH_SHORT).show();
            cambiarVisibilidad(false);
        }
        else if(correo.isEmpty()){
            et_correo_r.setError("Ingresa un correo electrónico");
            cambiarVisibilidad(false);
            et_correo_r.requestFocus();
        }else if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            et_correo_r.setError("Ingresa un correo electrónico válido");
            cambiarVisibilidad(false);
            et_correo_r.requestFocus();
        } else if(clave.isEmpty()){
            et_clave_r.setError("Ingresa una contraseña");
            cambiarVisibilidad(false);
            et_clave_r.requestFocus();
        }
        else if(clave2.isEmpty()){
            et_clave2_r.setError("Ingresa la confirmación de tu contraseña");
            cambiarVisibilidad(false);
            et_clave2_r.requestFocus();
        }
        else if(nombre.isEmpty()) {
            et_nombre_r.setError("Ingresa tu nombre");
            cambiarVisibilidad(false);
            et_nombre_r.requestFocus();
        }else if(apellido.isEmpty()){
            et_apellido_r.setError("Ingresa tu apellido");
            cambiarVisibilidad(false);
            et_apellido_r.requestFocus();
        }
        else{
            if(clave.length() >= 6){
                if(clave2.length() >=6){
                    if(clave.equals(clave2)) {
                        registrarUsuario();
                    }else{
                        //Toast.makeText(Registro.this, "Las contraseñas no coinciden, vuelve a intentarlo", Toast.LENGTH_SHORT).show();
                        crud.createAlert("Error", "No coinciden las contraseñas", "OK");
                    }
                }else{
                    et_clave2_r.setError("La clave tiene que tener al menos 6 caracteres");
                    et_clave2_r.requestFocus();
                    et_clave2_r.setText("");
                }
            }else {
                et_clave_r.setError("La clave tiene que tener al menos 6 caracteres");
                et_clave_r.requestFocus();
                et_clave_r.setText("");
            }
        }
    }


    private void registrarUsuario() {
        String imagen = "imagen";
        crud.registrarUsuario(new Usuario(nombre,apellido, correo,clave, imagen,false), btn_registrate_r, pb_r);
    }

    public void cambiarVisibilidad(boolean pb){
        if(pb){
            btn_registrate_r.setVisibility(View.INVISIBLE);
            pb_r.setVisibility(View.VISIBLE);
        }else{
            btn_registrate_r.setVisibility(View.VISIBLE);
            pb_r.setVisibility(View.INVISIBLE);
        }
    }


}

