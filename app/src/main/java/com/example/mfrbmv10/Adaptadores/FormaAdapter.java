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
import com.example.mfrbmv10.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FormaAdapter extends Fragment implements OnCompleteListener<QuerySnapshot> {

    private List<Forma> formaList, formaList2;
    private String[] formas;
    private Crud crud;
    private Context context;
    private Spinner spinner_forma;
    private String forma_mtr;
    private OnItemSelectedListener listener;

    public FormaAdapter(Fragment context, Spinner spinner_forma, String forma_mtr) {
        this.context = context.getContext();
        this.spinner_forma = spinner_forma;
        crud = new Crud(this);
        crud.obtenerFormas();
        this.forma_mtr = forma_mtr;
    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if(task.isSuccessful()){

            formaList = new ArrayList<Forma>();

            for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                Forma b = documentSnapshot.toObject(Forma.class);
                formaList.add(b);
            }

            formaList2 = new ArrayList<Forma>();
            for(int i=0; i<formaList.size(); i++){
                if(!this.forma_mtr.equals(formaList.get(i).getTipo_forma())){
                    formaList2.add(formaList.get(i));
                }
            }

            formas = new String[formaList.size()];
            formas[0] = this.forma_mtr;
            for(int i=0; i<formaList2.size(); i++){
                formas[i+1] = formaList2.get(i).getTipo_forma();
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.estilo_spinner, formas);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
            spinner_forma.setAdapter(adapter);

            spinner_forma.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    listener.onItemSelectedForma(adapterView.getItemAtPosition(i).toString(), i);
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
        void onItemSelectedForma(String documentSnapshot, int posicion);
    }
    public void setOnItemSelectedListener(OnItemSelectedListener listener){
        this.listener = listener;
    }
}
