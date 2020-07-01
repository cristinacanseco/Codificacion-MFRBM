package com.example.mfrbmv10.FirebaseMotor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mfrbmv10.Adaptadores.FormaAdapter;
import com.example.mfrbmv10.Adaptadores.TexturaAdapter;
import com.example.mfrbmv10.Fragments.Bitacoras.BitacoraFragment;
import com.example.mfrbmv10.Fragments.HomeActivity;
import com.example.mfrbmv10.Fragments.Muestreos.MuestreoFragment;
import com.example.mfrbmv10.Fragments.Usuario.UsuarioPerfilFragment;
import com.example.mfrbmv10.Modelos.Bitacora;
import com.example.mfrbmv10.Modelos.Muestreo;
import com.example.mfrbmv10.Modelos.Usuario;
import com.example.mfrbmv10.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class Crud implements Serializable {
    private Fragment context;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private String usuario_db ="Usuario";
    private String bitacora_db ="Bitacora";
    private String muestreo_db ="Muestreo";
    Context context2;

    public Crud(Fragment context) {
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        context2 = context.getContext();
    }

    public Crud(Context context){
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        context2 =context;
    }

    //U S U A R I O
    public void mostrarUsuario(){
        String id= mAuth.getCurrentUser().getUid();
        mFirestore.collection(usuario_db).document(id)
                .get().addOnCompleteListener((UsuarioPerfilFragment) context)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        createAlert("Error", "Ups...hubo un problema. \nVuelve a intentarlo\n ", "OK");
                    }
                });
    }

    public void editarUsuario(Usuario usuario) {
        String id = mAuth.getCurrentUser().getUid();

        mFirestore.collection(usuario_db).document(id)
                .update(generarMapUsuario(usuario))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        createAlert("Éxito", "Se actualizaron tus datos ;)", "OK");
                        irBitacoraFragment();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                createAlert("Error", "No se actualizaron tus datos. \nVuelve a intentarlo\n " + e.getMessage().toString(), "OK");
            }
        });
    }


    //B I T Á C O R A S

    //Create
    public void insertarBitacora(Bitacora bitacora) {
        String id = mAuth.getCurrentUser().getUid();

        mFirestore.collection(usuario_db).document(id).collection(bitacora_db).add(generarMapBitacora(bitacora))
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(context2, R.style.Theme_AppCompat_DayNight_Dialog);
                            builder.setTitle("¡Nueva Bitácora!")
                                    .setMessage("Se generó correctamente tu muestreo. Ya puedes consultarlo")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            irMuestreoFragment(task.getResult().getId() ,bitacora.getNombre_btc());
                                        }
                                    })
                                    .create().show();

                        } else {
                            createAlert("Error", "No se agregaron los datos de la bitácora. \nVuelve a intentarlo\n "+task.getException(), "OK");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                createAlert("Error", "Ups...hubo un problema. \nVuelve a intentarlo\n ", "OK");
            }
        });
    }


    //Read
    public void mostrarBitacoras(){
        String id= mAuth.getCurrentUser().getUid();
        mFirestore.collection(usuario_db).document(id).collection(bitacora_db)
                .get().addOnCompleteListener((BitacoraFragment)context)
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                createAlert("Error", "Ups... hubo un problema. \nVuelve a intentarlo\n ", "OK");
            }
        });


        mFirestore.collection(usuario_db).document(id).collection(bitacora_db)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(e.toString(),"MALLLLLL");
            }
        });
    }


    //Update
    public void editarDatosBitacora(Bitacora bitacora, String id_bitacora) {
        String id = mAuth.getCurrentUser().getUid();

        mFirestore.collection(usuario_db).document(id).collection(bitacora_db).document(id_bitacora).update(generarMapBitacora(bitacora))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        createAlert("Éxito", "Bitácora actualizada", "OK");
                        irBitacoraFragment();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                createAlert("Error", "No se actualizó la bitácora. \nVuelve a intentarlo\n " + e.getMessage().toString(), "OK");
            }
        });
    }


    //Delete
    public void borrarBitacora(String id_bitacora) {

        String id = mAuth.getCurrentUser().getUid();
        mFirestore.collection(usuario_db).document(id).collection(bitacora_db).document(id_bitacora).collection(muestreo_db).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {

                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        mFirestore.collection(usuario_db).document(id)
                                .collection(bitacora_db).document(id_bitacora)
                                .collection(muestreo_db).document(documentSnapshot.getId())
                                .delete();
                    }
                }else{
                    createAlert("Error", "\nNo se pudo eliminar la bitácora seleccionada. \nVuelve a intentarlo\n ", "OK");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                createAlert("Error", "\nNo se pudo eliminar la bitácora seleccionada. \nVuelve a intentarlo\n " + e.getMessage().toString(), "OK");
            }
        });

        mFirestore.collection(usuario_db).document(id).collection(bitacora_db).document(id_bitacora).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                createAlert("Éxito", "Bitácora eliminada exitosamente", "OK");
                irBitacoraFragment();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                createAlert("Error", "\nNo se pudo eliminar la bitácora seleccionada. \nVuelve a intentarlo\n " + e.getMessage().toString(), "OK");
            }
        });
    }

    public void buscar(String query) {
        String id = mAuth.getCurrentUser().getUid();
        mFirestore.collection(usuario_db).document(id).collection(bitacora_db)
                .whereEqualTo("nombre_btc", query)
                .get().addOnCompleteListener((BitacoraFragment)context)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        createAlert("Error", "Ups...hubo un problema. \nVuelve a intentarlo\n ", "OK");
                    }
                });
    }


    //M U E S T R E O S

    //Create
    public void insertarMuestreo(Muestreo muestreo, String id_btc, String nombre_muestreo) {
        String id = mAuth.getCurrentUser().getUid();

        mFirestore.collection(usuario_db).document(id).collection(bitacora_db).document(id_btc).collection(muestreo_db)
                .add(generarMapMuestreo(muestreo))
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(context2, R.style.Theme_AppCompat_DayNight_Dialog);
                            builder.setTitle("¡Nuevo Muestreo!")
                                    .setMessage("Se generó correctamente el muestreo. Ya puedes consultarlo")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            context2.startActivity(new Intent(context2, HomeActivity.class));
                                        }
                                    })
                                    .create().show();
                        } else {
                            createAlert("Error", "No se agregaron los datos de la bitácora. \nVuelve a intentarlo\n ", "OK");
                            context2.startActivity(new Intent(context2, HomeActivity.class));
                        }
                    }

                }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            createAlert("Error", "Ups...hubo un problema. \nVuelve a intentarlo\n ", "OK");
                        }
                    });

        context2.startActivity(new Intent(context2, HomeActivity.class));
    }


    public void actualizarCantidadMuestreos(String id_bitacora, String cantidad){
        String id = mAuth.getCurrentUser().getUid();
        mFirestore.collection(usuario_db).document(id)
                .collection(bitacora_db).document(id_bitacora)
                .update("cantidad_btc", cantidad)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //createAlert("Error", "No se actualizó el número de muestreos " + e.getMessage().toString(), "OK");
                    }
                });
    }


    public void actualizarImagenBitacora(String id_bitacora, String imagen){
        String id = mAuth.getCurrentUser().getUid();
        mFirestore.collection(usuario_db).document(id)
                .collection(bitacora_db).document(id_bitacora)
                .update("imagen_btc", imagen)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context2, R.style.Theme_AppCompat_DayNight_Dialog);
                        builder.setTitle("¡Nuevo Muestreo!")
                                .setMessage("Se generó correctamente el muestreo. Ya puedes consultarlo")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        context2.startActivity(new Intent(context2, HomeActivity.class));
                                    }
                                })
                                .create().show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //createAlert("Error", "No se actualizó el número de muestreos " + e.getMessage().toString(), "OK");
                    }
                });
    }

    //Read
    public void mostrarMuestreos(String id_btc){
        String id= mAuth.getCurrentUser().getUid();
        mFirestore.collection(usuario_db).document(id).collection(bitacora_db).document(id_btc).collection(muestreo_db)
                .get().addOnCompleteListener((MuestreoFragment)context)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        createAlert("Error", "Ups... hubo un problema. \nVuelve a intentarlo\n ", "OK");
                    }
                });
    }


    //Update
    public void editarDatosMuestreo(Muestreo muestreo, String id_bitacora, String id_muestreo, String nombre_bitacora) {
        String id = mAuth.getCurrentUser().getUid();

        mFirestore.collection(usuario_db).document(id)
                .collection(bitacora_db).document(id_bitacora)
                .collection(muestreo_db).document(id_muestreo)
                .update(generarMapMuestreo(muestreo))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        createAlert("Éxito", "Muestreo actualizado", "OK");
                        //irBitacoraFragment();
                        irMuestreoFragment(id_bitacora, nombre_bitacora);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                createAlert("Error", "No se actualizó el muestreo. \nVuelve a intentarlo\n " + e.getMessage().toString(), "OK");
            }
        });
    }


    //Delete
    public void borrarMuestreo(String id_bitacora, String id_muestreo, String nombre_bitacora) {

        String id = mAuth.getCurrentUser().getUid();

        mFirestore.collection(usuario_db).document(id)
                .collection(bitacora_db).document(id_bitacora)
                .collection(muestreo_db).document(id_muestreo)
                .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                createAlert("Éxito", "Muestreo eliminado exitosamente", "OK");
                irMuestreoFragment(id_bitacora, nombre_bitacora);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                createAlert("Error", "\nNo se pudo eliminar el muestreo seleccionada. \nVuelve a intentarlo\n " + e.getMessage().toString(), "OK");
            }
        });
    }


    //O T R O S
    public void createAlert(String alertTitle, String alertMessage, String positiveText){

        AlertDialog.Builder builder = new AlertDialog.Builder(context2);
        builder.setTitle(alertTitle)
                .setMessage(alertMessage)
                .setPositiveButton(positiveText, null)
                .create().show();

    }

    private Map<String, Object> generarMapBitacora(Bitacora bitacora) {
        Map<String, Object> map = new HashMap<>();
        map.put("nombre_btc", bitacora.getNombre_btc());
        map.put("imagen_btc", bitacora.getImagen_btc());
        map.put("fecha_btc", bitacora.getFecha_btc());
        map.put("hora_btc", bitacora.getHora_btc());
        map.put("ubicacion_btc", bitacora.getUbicacion_btc());
        map.put("coordenadas_btc", bitacora.getCoordenadas_btc());
        map.put("cantidad_btc", bitacora.getCantidad_btc());
        map.put("descripcion_btc", bitacora.getDescripcion_btc());

        return map;
    }

    private Map<String, Object> generarMapMuestreo(Muestreo muestreo) {
        Map<String, Object> map = new HashMap<>();
        map.put("nombre_mtr", muestreo.getNombre_mtr());
        map.put("imagen_mtr", muestreo.getImagen_mtr());
        map.put("fecha_mtr",muestreo.getFecha_mtr());
        map.put("hora_mtr",muestreo.getHora_mtr());
        map.put("ubicacion_mtr", muestreo.getUbicacion_mtr());
        map.put("coordenadas_mtr", muestreo.getCoordenadas_mtr());
        map.put("forma_mtr", muestreo.getForma_mtr());
        map.put("textura_mtr",muestreo.getTextura_mtr());
        map.put("color_mtr", muestreo.getColor_mtr());
        map.put("dimension_mtr", muestreo.getDimension_mtr());
        return map;
    }

    private Map<String, Object> generarMapUsuario(Usuario usuario) {
        Map<String, Object> map = new HashMap<>();
        map.put("nombre_usr", usuario.getNombre_usr());
        map.put("apellido_usr", usuario.getApellido_usr());
        map.put("correo_usr", usuario.getCorreo_usr());
        map.put("clave_usr", usuario.getClave_usr());
        map.put("imagen_usr", usuario.getImagen_usr());
        map.put("admin_usr", usuario.getAdmin_usr());
        return map;
    }


    public Muestreo generarMuestreo(Map<String, Object> map){
        String nombre_mtr = (String) map.get("nombre_mtr");
        String imagen_mtr = (String) map.get("imagen_mtr");
        String forma_mtr= (String) map.get("forma_mtr");
        String textura_mtr= (String) map.get("textura_mtr");
        ArrayList<Integer> color_mtr=(ArrayList<Integer>) map.get("color_mtr");
        ArrayList<String> dimension_mtr= (ArrayList<String>) map.get("dimension_mtr");
        String ubicacion_mtr= (String)map.get("ubicacion_mtr");
        String coordenadas_mtr = (String)map.get("coordenadas_mtr");
        String hora_mtr= (String)map.get("hora_mtr");
        String fecha_mtr= (String)map.get("fecha_mtr");

        return new Muestreo (nombre_mtr,imagen_mtr,fecha_mtr,hora_mtr,ubicacion_mtr,coordenadas_mtr,forma_mtr,textura_mtr,color_mtr,dimension_mtr);
    }

    public void irBitacoraFragment() {
        BitacoraFragment bf=new BitacoraFragment();
        context.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_main,bf)
                .addToBackStack(null)
                .commit();
    }

    public void irMuestreoFragment(String id_bitacora, String nombre_bitacora){
        MuestreoFragment mf=new MuestreoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id_bitacora", id_bitacora);
        bundle.putString("nombre_bitacora", nombre_bitacora);
        mf.setArguments(bundle);
        context.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_main,mf)
                .addToBackStack(null)
                .commit();
    }

    public void generarAlert(String titulo, String mensaje){
        AlertDialog.Builder builder = new AlertDialog.Builder(context2, R.style.Theme_AppCompat_DayNight_Dialog);
        builder.setTitle(titulo)
                .setMessage(mensaje)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .create().show();

    }


    // F O R M A
    public void obtenerFormas(){
        mFirestore.collection("Descripciones").document("7HojItOAjpRKjH127655")
                .collection("Forma")
                .get()
                .addOnCompleteListener((FormaAdapter)context)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        createAlert("Error", "Ups...hubo un problema. \nVuelve a intentarlo\n ", "OK");
                    }
                });
    }

    //T E X T U R A
    public void obtenerTextura(){
        mFirestore.collection("Descripciones").document("sJid2DP0iiNaXpqoJNJk")
                .collection("Textura")
                .get().addOnCompleteListener((TexturaAdapter)context)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        createAlert("Error", "Ups...hubo un problema. \nVuelve a intentarlo\n ", "OK");
                    }
                });
    }

}
