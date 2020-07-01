package com.example.mfrbmv10.Fragments.Usuario;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.mfrbmv10.Extras.Dialogo;
import com.example.mfrbmv10.FirebaseMotor.Crud;
import com.example.mfrbmv10.FirebaseMotor.SesionesFirestore;
import com.example.mfrbmv10.Modelos.Usuario;
import com.example.mfrbmv10.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UsuarioPerfilFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UsuarioPerfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UsuarioPerfilFragment extends Fragment implements View.OnClickListener, OnCompleteListener<DocumentSnapshot> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private EditText et_nombreUsuario, et_apellidoUsuario;
    private ImageView iv_imagen_u;
    private TextView tv_cambiarClave, tv_borrarCuenta, et_correoUsuario;
    private Button btn_salir_u;
    private SesionesFirestore sesionesFirestore;
    private Crud crud;
    private Usuario usuario;

    public UsuarioPerfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UsuarioPerfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UsuarioPerfilFragment newInstance(String param1, String param2) {
        UsuarioPerfilFragment fragment = new UsuarioPerfilFragment();
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
        View usuario_fragment =  inflater.inflate(R.layout.fragment_usuario_perfil, container, false);

        sesionesFirestore = new SesionesFirestore(getContext());
        crud = new Crud(this);
        crud.mostrarUsuario();

        //EditText
        et_nombreUsuario = usuario_fragment.findViewById(R.id.et_nombreUsuario);


        //TextView
        et_correoUsuario = usuario_fragment.findViewById(R.id.et_correoUsuario);

        tv_cambiarClave = usuario_fragment.findViewById(R.id.tv_cambiarClave);
        SpannableString mitextoU2 = new SpannableString(tv_cambiarClave.getText());
        mitextoU2.setSpan(new UnderlineSpan(), 0, mitextoU2.length(), 0);
        tv_cambiarClave.setText(mitextoU2);
        tv_cambiarClave.setOnClickListener(this);

        tv_borrarCuenta = usuario_fragment.findViewById(R.id.tv_borrarCuenta);
        SpannableString mitextoU = new SpannableString(tv_borrarCuenta.getText());
        mitextoU.setSpan(new UnderlineSpan(), 0, mitextoU.length(), 0);
        tv_borrarCuenta.setText(mitextoU);
        tv_borrarCuenta.setOnClickListener(this);

        //ImageView
        iv_imagen_u = usuario_fragment.findViewById(R.id.iv_imagen_u);
        Glide.with(getContext())
                .load(R.drawable.qq)
                .apply(RequestOptions.circleCropTransform())
                .into(iv_imagen_u);


        //Botonones
        btn_salir_u = usuario_fragment.findViewById(R.id.btn_salir_u);
        btn_salir_u.setOnClickListener(this);


        return usuario_fragment;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_cambiarClave:
                Dialogo dialogo = new Dialogo();
                dialogo.show(getFragmentManager(), "dialogo");
                break;
            case R.id.tv_borrarCuenta:
                borrarCuenta();
                break;
            case R.id.btn_salir_u:
                sesionesFirestore.cerrarSesion();
                break;
        }

    }

    private void borrarCuenta() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.Theme_AppCompat_DayNight_Dialog);
        builder.setTitle("Eliminar cuenta")
                .setMessage("¿Estás seguro de que deseas eliminar tu cuenta? \n\n Al hacerlo perderás todos tus datos y no podrás volver a acceder a ellos")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sesionesFirestore.borrarCuenta();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create().show();
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
    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
        if(task.isSuccessful()){
                usuario =  task.getResult().toObject(Usuario.class);
                et_nombreUsuario.setText(usuario.getNombre_usr() + " "+ usuario.getApellido_usr());
                et_correoUsuario.setText(usuario.getCorreo_usr());
        }else{
            crud. createAlert("Error", "Hubo un problema", "OK");
        }
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
