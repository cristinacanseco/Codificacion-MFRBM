package com.example.mfrbmv10.Fragments.Muestreos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mfrbmv10.Adaptadores.MuestreoAdapter;
import com.example.mfrbmv10.FirebaseMotor.Crud;
import com.example.mfrbmv10.Modelos.Muestreo;
import com.example.mfrbmv10.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MuestreoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MuestreoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MuestreoFragment extends Fragment implements OnCompleteListener<QuerySnapshot>, MuestreoAdapter.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Crud crud;
    public RecyclerView rv_mis_muestreos;
    public FloatingActionButton fab_agregar_muestreo;
    private List<Muestreo> muestreoList;
    private List<String> muestreoIDList;
    private MuestreoAdapter muestreoAdapter;
    private TextView tv_numero_mm,tv_nombre_mtr_mm,tv_sin_muestreos;
    private String nombreBitacora, id_bitacora;


    public MuestreoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MuestreoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MuestreoFragment newInstance(String param1, String param2) {
        MuestreoFragment fragment = new MuestreoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View muestreo_fragment =  inflater.inflate(R.layout.fragment_muestreo, container, false);

        this.crud = new Crud(this);
        Bundle bundle = getArguments();
        id_bitacora = bundle.getString("id_bitacora");
        nombreBitacora = bundle.getString("nombre_bitacora");
        crud.mostrarMuestreos(id_bitacora);

        rv_mis_muestreos  = muestreo_fragment.findViewById(R.id.rv_mis_muestreos);
        rv_mis_muestreos.setHasFixedSize(true);
        rv_mis_muestreos.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        tv_nombre_mtr_mm = muestreo_fragment.findViewById(R.id.tv_nombre_mtr_mm);
        tv_numero_mm = muestreo_fragment.findViewById(R.id.tv_numero_mm);
        tv_sin_muestreos = muestreo_fragment.findViewById(R.id.tv_sin_muestreos);

        fab_agregar_muestreo = muestreo_fragment.findViewById(R.id.fab_agregar_muestreo);
        fab_agregar_muestreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MuestreoNuevoFragment fr=new MuestreoNuevoFragment();
                Bundle bundle = new Bundle();
                bundle.putString("id_bitacora", id_bitacora);
                bundle.putString("nombre_bitacora", nombreBitacora);
                bundle.putInt("cantidad_btc", muestreoList.size());
                fr.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_main,fr)
                        .addToBackStack(null)
                        .commit();
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.Theme_AppCompat_DayNight_Dialog);
                builder.setTitle("Eliminar bitácora")
                        .setMessage("¿Estás seguro de que deseas eliminar el muestreo?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int c = muestreoList.size() -1;
                                String cantidad = ""+c;
                                crud.actualizarCantidadMuestreos(id_bitacora, cantidad);
                                crud.borrarMuestreo(id_bitacora,muestreoIDList.get(viewHolder.getAdapterPosition()), nombreBitacora);
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                crud.irMuestreoFragment(id_bitacora,nombreBitacora);
                                //crud.irBitacoraFragment();
                            }
                        })
                        .create().show();

            }
        }).attachToRecyclerView(rv_mis_muestreos);

        return muestreo_fragment ;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onComplete(@NonNull Task<QuerySnapshot> task) {
        if(task.isSuccessful()){
            muestreoList = new ArrayList<Muestreo>();
            muestreoIDList = new ArrayList<String>();

            for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                Muestreo b = documentSnapshot.toObject(Muestreo.class);
                muestreoIDList.add(documentSnapshot.getId());
                muestreoList.add(b);
            }

            muestreoAdapter= new MuestreoAdapter(getContext(), muestreoList);
            rv_mis_muestreos.setAdapter(muestreoAdapter);
            tv_numero_mm.setText(muestreoList.size()+" muestreo(s)");
            tv_nombre_mtr_mm.setText(nombreBitacora);

            if(muestreoList.size()==0){
                tv_sin_muestreos.setVisibility(View.VISIBLE);
            }

            muestreoAdapter.setOnItemClickListener(this);

        }else{
            crud.createAlert("Error", "No se encontraron resultados " + task.getException(), "OK");;
        }
    }

    @Override
    public void onItemClick(Muestreo documentSnapshot, int posicion) {
        String id = muestreoIDList.get(posicion);

        MuestreoMostrarFragment bmf=new MuestreoMostrarFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id_bitacora", id_bitacora);
        bundle.putString("id_muestreo", id);
        bundle.putSerializable("muestreo", documentSnapshot);
        bmf.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_main,bmf)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onItemLongClick(Muestreo documentReference, int posicion) {
        String id = muestreoIDList.get(posicion);
        MuestreoEditarFragment bmf = new MuestreoEditarFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id_bitacora", id_bitacora);
        bundle.putString("id_muestreo", id);
        bundle.putString("nombre_bitacora", nombreBitacora);
        bundle.putSerializable("muestreo", documentReference);
        bmf.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_main,bmf)
                .addToBackStack(null)
                .commit();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
