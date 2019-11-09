package com.example.mfrbmv10.Imagen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
import com.bumptech.glide.request.RequestOptions;
import com.example.mfrbmv10.Extras.Medicion;
import com.example.mfrbmv10.Modelos.Colores;
import com.example.mfrbmv10.Modelos.Imagen;
import com.example.mfrbmv10.Modelos.Muestreo;
import com.example.mfrbmv10.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
public class CamaraIntento2 extends AppCompatActivity{
    private ImageView imgImagen;

    private TextView txtVibrant;
    private TextView txtDarkVibrant;
    private TextView txtLightVibrant;

    private TextView txtMuted;
    private TextView txtDarkMuted;
    private TextView txtLightMuted;

    public String rutaAbsoluta="";
    final int COD_FOTO=20;
    public File imagenFile;

    private String uriFoto;
    private ArrayList<Integer> paleta;
    private Imagen imagen;
    private Bitmap imgOriBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camara_intento2);

        if(validaPermisos()){
            try{
                tomarFotografia();
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        imgImagen=(ImageView)findViewById(R.id.imgImagen);

        txtVibrant=(TextView)findViewById(R.id.txtVibrant);
        txtDarkVibrant=(TextView)findViewById(R.id.txtDarkVibrant);
        txtLightVibrant=(TextView)findViewById(R.id.txtLightVibrant);

        txtMuted=(TextView)findViewById(R.id.txtMuted);
        txtDarkMuted=(TextView)findViewById(R.id.txtDarkMuted);
        txtLightMuted=(TextView)findViewById(R.id.txtLightMuted);

        uriFoto="";
        paleta=new ArrayList<Integer>();
    }

    private boolean validaPermisos(){

        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return true;
        }

        if((checkSelfPermission(CAMERA)==PackageManager.PERMISSION_GRANTED)&& (checkSelfPermission(WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)){
            return true;
        }

        if((shouldShowRequestPermissionRationale(CAMERA))|| (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))){
            cargarDialogoRecomendacion();
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String[]permissions,@NonNull int[]grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if(requestCode==100){
            if(grantResults.length==2&&grantResults[0]==PackageManager.PERMISSION_GRANTED &&grantResults[1]==PackageManager.PERMISSION_GRANTED){
                try{
                    tomarFotografia();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }else{
                solicitarPermisosManual();
            }
        }
    }

    private void solicitarPermisosManual(){
        final CharSequence[]opciones={"Sí","No"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(this);
        alertOpciones.setTitle("¿Deseasconfigurarlospermisosdeformamanual?");
        alertOpciones.setItems(opciones,new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface,int i){
                if(opciones[i].equals("Sí")){
                    Intent intent=new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri=Uri.fromParts("package",getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Lospermisosnofueronaceptados",Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }

    private void cargarDialogoRecomendacion(){
        AlertDialog.Builder dialogo=new AlertDialog.Builder(this);
        dialogo.setTitle("PermisosDesactivados");
        dialogo.setMessage("DebesaceptarlospermisosparaelcorrectofuncionamientodelaApp");

        dialogo.setPositiveButton("Aceptar",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface,int i){
                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
                    }
                    });
        dialogo.show();
    }

    public File crearDirectorioPublico(String nombreDirectorio){
        File directorio=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),nombreDirectorio);
        if(!directorio.mkdirs())
            Log.e("","Error:Nosecreoeldirectoriopúblico");
        return directorio;
    }

    private File crearFotoFile()throws IOException{
        File fileImagen=crearDirectorioPublico("MFRBM");
        //FilefileImagen=getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        String timeStamp="";
        String imagenFileName="";

        boolean isCreada=fileImagen.exists();
        if(isCreada==false){
        isCreada=fileImagen.mkdirs();
        }

        if(isCreada==true){
        //nombreImagen=(System.currentTimeMillis()/1000)+".jpg";
        timeStamp=new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
        imagenFileName="img_muestreo_"+timeStamp;
        }

        File fotoFile=File.createTempFile(imagenFileName,".jpg",fileImagen);
        rutaAbsoluta=fotoFile.getAbsolutePath();
        return fotoFile;
    }

    private void tomarFotografia()throws IOException{
        StrictMode.VmPolicy.Builder builder=new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        imagenFile=crearFotoFile();

        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(imagenFile));
        startActivityForResult(intent,COD_FOTO);
        Toast.makeText(this,""+rutaAbsoluta,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode==RESULT_OK){
            switch(requestCode){
                case COD_FOTO:
                MediaScannerConnection.scanFile(this,new String[]{rutaAbsoluta},null,
                new MediaScannerConnection.OnScanCompletedListener(){
                    @Override
                    public void onScanCompleted(String path,Uri uri){
                            Log.i("Rutadealmacenamiento","Path:"+path);
                            }
                });
                mostrarImagen();
                break;
            }
        }
    }

    public void mostrarImagen(){
        Uri imageUri=Uri.fromFile(imagenFile);
        uriFoto=String.valueOf(new File(imageUri.getPath()));

        imgImagen.setImageURI(imageUri);
        imagen=new Imagen(imgImagen.getDrawable());
        imgOriBitmap=imagen.getImagenB();

        Log.i("Medidas de la imagen nueva", "H:"+imgOriBitmap.getHeight()+" W:"+imgOriBitmap.getWidth());
        generarPaletaColores();
    }

    private void generarPaletaColores(){

       Palette p =  Palette.from(imgOriBitmap).maximumColorCount(24).generate();
                paleta.add(0,p.getVibrantSwatch().getRgb());
                paleta.add(1,p.getDarkVibrantSwatch().getRgb());
                paleta.add(2,p.getLightVibrantSwatch().getRgb());
                paleta.add(3,p.getMutedSwatch().getRgb());
                paleta.add(4,p.getDarkMutedSwatch().getRgb());
                paleta.add(5,p.getLightMutedSwatch().getRgb());

                paleta.add(6,p.getVibrantSwatch().getBodyTextColor());
                paleta.add(7,p.getDarkVibrantSwatch().getBodyTextColor());
                paleta.add(8,p.getLightVibrantSwatch().getBodyTextColor());
                paleta.add(9,p.getMutedSwatch().getBodyTextColor());
                paleta.add(10,p.getDarkMutedSwatch().getBodyTextColor());
                paleta.add(11,p.getLightMutedSwatch().getBodyTextColor());

                for(Palette.Swatch sw:p.getSwatches()){
                    Log.i("Palette","Color:#"+Integer.toHexString(sw.getRgb())+"("+sw.getPopulation()+"píxeles)");
                }
                obtenerDatos();
                //irAMedicion();
    }

    private void obtenerDatos(){
        ArrayList<Integer>p=paleta;
        if(p!=null||p.size()>0){
            setTextViewSwatch(txtVibrant,p.get(0),p.get(6));
            setTextViewSwatch(txtDarkVibrant,p.get(1),p.get(7));
            setTextViewSwatch(txtLightVibrant,p.get(2),p.get(8));
            setTextViewSwatch(txtMuted,p.get(3),p.get(9));
            setTextViewSwatch(txtDarkMuted,p.get(4),p.get(10));
            setTextViewSwatch(txtLightMuted,p.get(5),p.get(11));
        }
    }

    private void setTextViewSwatch(TextView tview,Integer swatch,Integer color){
        if(swatch!=null){
            tview.setBackgroundColor(swatch);
            tview.setTextColor(color);
            Colores c=new Colores(swatch);
            int r=c.getRed();
            int g=c.getGreen();
            int b=c.getBlue();
            String hexColor=String.format("#%02x%02x%02x",r,g,b).toUpperCase();
            tview.setText("\t\t\tRGB("+r+","+g+","+b+")\t\t\t"+hexColor);
        }
        else{
            tview.setBackgroundColor(Color.BLACK);
            tview.setTextColor(Color.WHITE);
            tview.setText("(Sindefinir)");
        }
    }

        private void irAMedicion(){

            String id_bitacora=getIntent().getStringExtra("id_bitacora");
            String nombre_bitacora=getIntent().getStringExtra("nombre_bitacora");
            String cantidad=getIntent().getStringExtra("cantidad");
            Bundle bundle1=getIntent().getBundleExtra("muestreo");
            Muestreo m=(Muestreo)bundle1.getSerializable("muestreo");
            m.setColor_mtr(paleta);
            m.setImagen_mtr(uriFoto);

            Bundle bundle=new Bundle();
            bundle.putSerializable("muestreo",m);

            Intent intent=new Intent(this,Medicion.class);
            intent.putExtra("id_bitacora",id_bitacora);
            intent.putExtra("nombre_bitacora",nombre_bitacora);
            intent.putExtra("muestreo",bundle);
            intent.putExtra("cantidad",cantidad);
            startActivity(intent);
        }
}
