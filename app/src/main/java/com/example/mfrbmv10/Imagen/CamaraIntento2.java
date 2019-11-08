package com.example.mfrbmv10.Imagen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mfrbmv10.Modelos.Imagen;
import com.example.mfrbmv10.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class CamaraIntento2 extends AppCompatActivity {

    private ImageView imgImagen, imgImagenI2;

    private TextView txtVibrant;
    private TextView txtDarkVibrant;
    private TextView txtLightVibrant;

    private TextView txtMuted;
    private TextView txtDarkMuted;
    private TextView txtLightMuted;

    public String rutaAbsoluta="";
    final int COD_FOTO=20;
    public File imagenFile;
    private Bitmap bitmap;
    private Imagen imagen;
    private String uriFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camara_intento2);

        if (validaPermisos()){
            try {
                tomarFotografia();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        imgImagen = (ImageView)findViewById(R.id.imgImagen);

        txtVibrant = (TextView)findViewById(R.id.txtVibrant);
        txtDarkVibrant = (TextView)findViewById(R.id.txtDarkVibrant);
        txtLightVibrant = (TextView)findViewById(R.id.txtLightVibrant);

        txtMuted = (TextView)findViewById(R.id.txtMuted);
        txtDarkMuted = (TextView)findViewById(R.id.txtDarkMuted);
        txtLightMuted = (TextView)findViewById(R.id.txtLightMuted);

        //imgImagen.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.flores1));

        //generarPaletaColores();

    }

    private boolean validaPermisos() {

        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return true;
        }

        if((checkSelfPermission(CAMERA)== PackageManager.PERMISSION_GRANTED)&&
                (checkSelfPermission(WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)){
            return true;
        }

        if((shouldShowRequestPermissionRationale(CAMERA)) ||
                (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))){
            cargarDialogoRecomendacion();
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100){
            if(grantResults.length==2 && grantResults[0]==PackageManager.PERMISSION_GRANTED
                    && grantResults[1]==PackageManager.PERMISSION_GRANTED){
                try {
                    tomarFotografia();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                solicitarPermisosManual();
            }
        }
    }

    private void solicitarPermisosManual() {
        final CharSequence[] opciones={"Sí","No"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(this);
        alertOpciones.setTitle("¿Deseas configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Sí")){
                    Intent intent=new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri=Uri.fromParts("package",getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Los permisos no fueron aceptados",Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }

    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo=new AlertDialog.Builder(this);
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debes aceptar los permisos para el correcto funcionamiento de la App");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
            }
        });
        dialogo.show();
    }

    public File crearDirectorioPublico(String nombreDirectorio) {
        File directorio = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), nombreDirectorio);
        if (!directorio.mkdirs())
            Log.e("" ,"Error: No se creo el directorio público");
        return directorio;
    }

    private File crearFotoFile() throws IOException{
        File fileImagen = crearDirectorioPublico("MFRBM");
        //File fileImagen=getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        String timeStamp="";
        String imagenFileName="";

        boolean isCreada=fileImagen.exists();
        if(isCreada==false){
            isCreada=fileImagen.mkdirs();
        }

        if(isCreada==true){
            //nombreImagen=(System.currentTimeMillis()/1000)+".jpg";
            timeStamp=new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
            imagenFileName ="img_muestreo_"+timeStamp;
        }

        File fotoFile=File.createTempFile(imagenFileName,".jpg",fileImagen);
        rutaAbsoluta=fotoFile.getAbsolutePath();
        return fotoFile;
    }

    private void tomarFotografia() throws IOException {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        imagenFile= crearFotoFile();

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagenFile));
        startActivityForResult(intent,COD_FOTO);
        Toast.makeText(this, ""+rutaAbsoluta, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case COD_FOTO:
                    MediaScannerConnection.scanFile(this, new String[]{rutaAbsoluta}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("Ruta de almacenamiento","Path: "+path);
                                }
                            });
                    mostrarImagen();
                    break;
            }
        }
    }

    public void mostrarImagen(){
        bitmap= BitmapFactory.decodeFile(rutaAbsoluta);
        Uri imageUri = Uri.fromFile(imagenFile);

        //Glide.with(this).load(new File(imageUri.getPath())).into(imgImagen);

        //imgImagen.setImageURI(imageUri);
        //imagen_pv.setImageURI(imageUri);
        //imagen = new Imagen(imgImagen.getDrawable());
        //imgOriBitmap = imagen.getImagenB();
        //imgImagen.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.flores1));
        generarPaletaColores(imageUri);
    }

    private void generarPaletaColores(Uri imageUri) {
        String file = String.valueOf(new File(imageUri.getPath()));

        Bitmap bitmap = (Bitmap) BitmapFactory.decodeFile(file);

        //Imagen imagen = new Imagen(bitmap);
        //imgImagen.setImageDrawable(imagen.getImagenD());

        Glide.with(this).load(file).into(imgImagen);


        Toast.makeText(this, ""+file, Toast.LENGTH_SHORT).show();
        //Bitmap bitmap1 = imagen.getImagenB();
        Palette.from(bitmap).maximumColorCount(24).generate(new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette p) {

                /*
                //Opción 1: Acceso directo a los colores principales
                txtVibrant.setBackgroundColor(p.getVibrantColor(Color.BLACK));
                txtDarkVibrant.setBackgroundColor(p.getDarkVibrantColor(Color.BLACK));
                txtLightVibrant.setBackgroundColor(p.getLightVibrantColor(Color.BLACK));

                txtMuted.setBackgroundColor(p.getMutedColor(Color.BLACK));
                txtDarkMuted.setBackgroundColor(p.getDarkMutedColor(Color.BLACK));
                txtLightMuted.setBackgroundColor(p.getLightMutedColor(Color.BLACK));
                */

                //Opción 2: Acceso a los swatches pricipales completos
                setTextViewSwatch(txtVibrant, p.getVibrantSwatch());
                setTextViewSwatch(txtDarkVibrant, p.getDarkVibrantSwatch());
                setTextViewSwatch(txtLightVibrant, p.getLightVibrantSwatch());
                setTextViewSwatch(txtMuted, p.getMutedSwatch());
                setTextViewSwatch(txtDarkMuted, p.getDarkMutedSwatch());
                setTextViewSwatch(txtLightMuted, p.getLightMutedSwatch());

                //Opción 3: Acceso a todos los swatches generados
                for (Palette.Swatch sw : p.getSwatches()) {
                    Log.i("Palette", "Color: #" + Integer.toHexString(sw.getRgb()) + " (" + sw.getPopulation() + " píxeles)");
                }
            }
        });
    }

    private void setTextViewSwatch(TextView tview, Palette.Swatch swatch) {
        if(swatch != null) {
            tview.setBackgroundColor(swatch.getRgb());
            tview.setTextColor(swatch.getBodyTextColor());
            tview.setText("Pixeles: " + swatch.getPopulation());
        }
        else {
            tview.setBackgroundColor(Color.BLACK);
            tview.setTextColor(Color.WHITE);
            tview.setText("(sin definir)");
        }
    }


}
