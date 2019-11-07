package com.example.mfrbmv10.Adaptadores;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mfrbmv10.FirebaseMotor.Crud;
import com.example.mfrbmv10.Modelos.Forma;
import com.example.mfrbmv10.Modelos.Textura;
import com.example.mfrbmv10.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class TexturaAdapter extends Fragment implements OnCompleteListener<QuerySnapshot>  {

    private List<Textura> texturaList, texturaList2;
    private String[] textura;
    private Crud crud;
    private Context context;
    private Spinner spinner_textura;
    private String textura_mtr;
    private OnItemSelectedListener listener;

    public TexturaAdapter(Fragment context, Spinner spinner_textura, String textura_mtr) {
        this.context = context.getContext();
        this.spinner_textura = spinner_textura;
        crud = new Crud(this);
        crud.obtenerTextura();
        this.textura_mtr = textura_mtr;
    }

    public List<Textura> getTexturaList() {
        return texturaList;
    }

    public String[] getTexturas() {
        return textura;
    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if(task.isSuccessful()){

            texturaList = new ArrayList<Textura>();

            for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                Textura b = documentSnapshot.toObject(Textura.class);
                texturaList.add(b);
            }

            texturaList2 = new ArrayList<Textura>();
            for(int i=0; i<texturaList.size(); i++){
                if(!this.textura_mtr.equals(texturaList.get(i).getTipo_textura())){
                    texturaList2.add(texturaList.get(i));
                }
            }

            textura = new String[texturaList.size()];
            textura[0] = this.textura_mtr;
            for(int i=0; i<texturaList2.size(); i++){
                textura[i+1] = texturaList2.get(i).getTipo_textura();
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.estilo_spinner, textura);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
            spinner_textura.setAdapter(adapter);

            spinner_textura.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    listener.onItemSelectedTextura(adapterView.getItemAtPosition(i).toString(), i);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }else{
            crud.createAlert("Error", "Hubo un problema", "OK");
        }
    }

    public interface OnItemSelectedListener {
        void onItemSelectedTextura(String documentSnapshot, int posicion);
    }
    public void setOnItemSelectedListener(OnItemSelectedListener listener){
        this.listener = listener;
    }


}

