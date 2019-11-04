package com.example.mfrbmv10.FirebaseMotor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.example.mfrbmv10.Fragments.HomeActivity;
import com.example.mfrbmv10.Modelos.Usuario;
import com.example.mfrbmv10.Sesiones.IniciarSesion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SesionesFirestore {

    private Context context;
    private FirebaseAuth mAuth;
    public FirebaseFirestore mFirestore;
    private FirebaseUser mUser;
    private String usuario_db ="Usuario";


    public SesionesFirestore(Context context) {
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mUser = mAuth.getCurrentUser();
    }


    public void registrarUsuario(final Usuario usuario, Button btn_registrate_r, ProgressBar pb_r) {
        mAuth.createUserWithEmailAndPassword(usuario.getCorreo_usr(), usuario.getClave_usr()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                cambiarBotones(btn_registrate_r, pb_r);
                if (task.isSuccessful()) {
                    String id = mAuth.getCurrentUser().getUid();

                    Map<String, Object> map = new HashMap<>();
                    map.put("nombre_usr", usuario.getNombre_usr());
                    map.put("apellido_usr", usuario.getApellido_usr());
                    map.put("correo_usr", usuario.getCorreo_usr());
                    map.put("clave_usr", usuario.getClave_usr());
                    map.put("imagen_usr", usuario.getNombre_usr());
                    map.put("admin", usuario.getAdmin_usr());

                    mFirestore.collection(usuario_db).document(id).set(map)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    context.startActivity(new Intent(new Intent(context, HomeActivity.class)));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    createAlert("Error", "No se agregó al usuario. \nVuelve a intentarlo\n " + e.getMessage().toString(), "OK");
                                    // Log.e(TAG, "Mensaje: "+ e.getMessage().toString()+ " Localización: " +e.getLocalizedMessage().toString());
                                }
                            });
                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        createAlert("Error", "El correo ya existe. \nVuelve a intentarlo\n ", "OK");
                    } else {
                        createAlert("Error", "No se pudo regisrar al usuario.", "OK");
                    }
                }
            }
        });
    }

    public void iniciarSesion(String correo, String clave, Button btn_ingresar_is, ProgressBar pb_is) {
        mAuth.signInWithEmailAndPassword(correo, clave)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        cambiarBotones(btn_ingresar_is, pb_is);
                        if (task.isSuccessful()) {
                            //createAlert("Éxito", "Bienvenido ", "OK");
                            context.startActivity(new Intent(context, HomeActivity.class));
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                createAlert("Error", "No coindice la contraseña con el coreo \nVuelve a intentarlo\n ", "OK");

                            } else {
                                createAlert("Error", "No existe el usuario.", "OK");
                            }
                        }
                    }
                });
    }

    public void cerrarSesion() {
        mAuth.signOut();
        context.startActivity(new Intent(context, IniciarSesion.class));
    }


    public void createAlert(String alertTitle, String alertMessage, String positiveText){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(alertTitle)
                .setMessage(alertMessage)
                .setPositiveButton(positiveText, null)
                .create().show();
    }
    private void cambiarBotones(Button btn, ProgressBar pb) {
        btn.setVisibility(View.VISIBLE);
        pb.setVisibility(View.INVISIBLE);
    }

    public FirebaseUser getmUser() {
        return mUser;
    }

}
