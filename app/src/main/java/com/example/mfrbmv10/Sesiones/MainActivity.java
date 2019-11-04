package com.example.mfrbmv10.Sesiones;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mfrbmv10.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn_ingresar;
    TextView tv_registrar_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_ingresar = findViewById(R.id.btn_ingresar_main);
        btn_ingresar.setOnClickListener(this);
        tv_registrar_main = findViewById(R.id.tv_registrar_main);
        tv_registrar_main.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_ingresar_main:
                startActivity(new Intent(MainActivity.this, IniciarSesion.class));
                break;
            case R.id.tv_registrar_main:
                startActivity(new Intent (MainActivity.this, Registro.class));
            default:
                break;
        }
    }
}
