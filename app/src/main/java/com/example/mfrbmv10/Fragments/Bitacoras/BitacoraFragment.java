package com.example.mfrbmv10.Fragments.Bitacoras;

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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mfrbmv10.Adaptadores.BitacoraAdapter;
import com.example.mfrbmv10.Extras.Localizacion;
import com.example.mfrbmv10.FirebaseMotor.Crud;
import com.example.mfrbmv10.Modelos.Bitacora;
import com.example.mfrbmv10.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BitacoraFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BitacoraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BitacoraFragment extends Fragment implements OnCompleteListener<QuerySnapshot>, BitacoraAdapter.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Crud crud;
    public RecyclerView rv_mis_bitacoras_p;
    public FloatingActionButton fab_agregar_bitacora;
    private List<Bitacora> bitacoraList;
    private List<String> bitacoraIDList;
    private BitacoraAdapter bitacoraAdapter;
    private TextView tv_sin_bitacoras, tv_numero_b;


    public BitacoraFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BitacoraFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BitacoraFragment newInstance(String param1, String param2) {
        BitacoraFragment fragment = new BitacoraFragment();
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

        View bitacora_fragment =  inflater.inflate(R.layout.fragment_bitacora, container, false);

        this.crud = new Crud(this);
        crud.mostrarBitacoras();

        rv_mis_bitacoras_p  = bitacora_fragment.findViewById(R.id.rv_mis_bitacoras_p);
        rv_mis_bitacoras_p.setHasFixedSize(true);
        rv_mis_bitacoras_p.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        tv_sin_bitacoras = bitacora_fragment.findViewById(R.id.tv_sin_bitacoras);
        tv_numero_b = bitacora_fragment.findViewById(R.id.tv_numero_b);

        fab_agregar_bitacora = bitacora_fragment.findViewById(R.id.fab_agregar_bitacora);
        fab_agregar_bitacora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BitacoraNuevaFragment fr=new BitacoraNuevaFragment();
                Bundle args = new Bundle();
                args.putString("mensaje", "Hola, es tu mensaje");
                fr.setArguments(args);
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
                        .setMessage("¿Estás seguro de que deseas eliminar la bitácora?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                crud.borrarBitacora(bitacoraIDList.get(viewHolder.getAdapterPosition()));
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                crud.irBitacoraFragment();
                            }
                        })
                        .create().show();

            }
        }).attachToRecyclerView(rv_mis_bitacoras_p);


        return bitacora_fragment ;

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
            bitacoraList = new ArrayList<Bitacora>();
            bitacoraIDList = new ArrayList<String>();
            for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                Bitacora b = documentSnapshot.toObject(Bitacora.class);
                bitacoraIDList.add(documentSnapshot.getId());
                bitacoraList.add(b);
            }
            bitacoraAdapter = new BitacoraAdapter(getContext(), bitacoraList);
            rv_mis_bitacoras_p.setAdapter(bitacoraAdapter);
            tv_numero_b.setText(bitacoraList.size()+" bitácora(s)");

            if(bitacoraList.size()==0){
                tv_sin_bitacoras.setVisibility(View.VISIBLE);
            }

            bitacoraAdapter.setOnItemClickListener(this);

        }else{
            crud. createAlert("Error", "Hubo un problema", "OK");
        }
    }

    @Override
    public void onItemClick(Bitacora documentSnapshot, int posicion) {
        String id = bitacoraIDList.get(posicion);

        BitacoraMostrarFragment bmf=new BitacoraMostrarFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id_bitacora", id);
        bundle.putSerializable("bitacora", documentSnapshot);
        bmf.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_main,bmf)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onItemLongClick(Bitacora documentReference, int posicion) {
        String id = bitacoraIDList.get(posicion);
        BitacoraEditarFragment bmf=new BitacoraEditarFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id_bitacora", id);
        bundle.putSerializable("bitacora", documentReference);
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
