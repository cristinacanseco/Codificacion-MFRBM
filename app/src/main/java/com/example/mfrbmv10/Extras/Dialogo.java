package com.example.mfrbmv10.Extras;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.mfrbmv10.FirebaseMotor.SesionesFirestore;
import com.example.mfrbmv10.R;

public class Dialogo extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        SesionesFirestore sesionesFirestore = new SesionesFirestore(getContext());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = getActivity().getLayoutInflater().inflate(R.layout.item_dialogo_correo, null);

        EditText et_correo_d = view.findViewById(R.id.et_correo_d);

        builder.setView(view)
        .setPositiveButton("Aceptar", null)
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                });

        final  AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button positivo = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                positivo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(et_correo_d.getText().toString().trim() != null){
                            sesionesFirestore.cambiarClave(et_correo_d.getText().toString().trim());
                            dismiss();
                        }else{
                            Toast.makeText(getContext(), "Ingresa un correo", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        return dialog;
    }


}
