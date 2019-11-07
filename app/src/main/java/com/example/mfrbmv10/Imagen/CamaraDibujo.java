package com.example.mfrbmv10.Imagen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mfrbmv10.Extras.Medicion;
import com.example.mfrbmv10.Modelos.ColorMuestreo;
import com.example.mfrbmv10.Modelos.Colores;
import com.example.mfrbmv10.Modelos.Imagen;
import com.example.mfrbmv10.Modelos.Muestreo;
import com.example.mfrbmv10.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import me.panavtec.drawableview.DrawableView;
import me.panavtec.drawableview.DrawableViewConfig;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class CamaraDibujo extends AppCompatActivity implements View.OnClickListener {

    public String rutaAbsoluta="";
    final int COD_FOTO=20;
    public File imagenFile;

    private Imagen imagen;
    private Bitmap bitmap;
    private Bitmap imgOriBitmap, imgNvoBitmap;

    private int color;
    private float pincel;

    public ImageView imagen_pv;
    private DrawableView drawableView;
    private ImageButton botonPincelGrande, undo, listo, botonPincelChico;
    private DrawableViewConfig config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camara);

        if (validaPermisos()){
            try {
                tomarFotografia();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        imagen_pv = (ImageView) findViewById(R.id.imagen_pv);
        drawableView = (DrawableView) findViewById(R.id.paintView);

        botonPincelGrande = findViewById(R.id.botonPincel);
        botonPincelGrande.setOnClickListener(this);
        undo = findViewById(R.id.botonUndo);
        undo.setOnClickListener(this);
        listo = findViewById(R.id.botonListo);
        listo.setOnClickListener(this);
        botonPincelChico = findViewById(R.id.botonPincelChico);
        botonPincelChico.setOnClickListener(this);
        color = getResources().getColor(android.R.color.holo_purple);
        pincel = 20f;
    }

    private boolean validaPermisos() {

        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return true;
        }

        if((checkSelfPermission(CAMERA)==PackageManager.PERMISSION_GRANTED)&&
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
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(CamaraDibujo.this);
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
        AlertDialog.Builder dialogo=new AlertDialog.Builder(CamaraDibujo.this);
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
        imagen_pv.setImageURI(imageUri);
        imagen = new Imagen(imagen_pv.getDrawable());
        imgOriBitmap = imagen.getImagenB();
        configurarCanvas();
    }

    private void configurarCanvas() {
        config = new DrawableViewConfig();
        config.setStrokeColor(color);
        config.setShowCanvasBounds(true);
        config.setStrokeWidth(pincel);
        config.setMinZoom(1.0f);
        config.setMaxZoom(3.0f);
        config.setCanvasHeight(imgOriBitmap.getHeight());
        config.setCanvasWidth(imgOriBitmap.getWidth());

        drawableView.setConfig(config);
        drawableView.setBackground(imagen.getImagenD());
        //Toast.makeText(this, "HD:"+config.getCanvasHeight()+" WD:"+config.getCanvasWidth()+"\nHB:"+bitmap.getHeight()+" WB:"+bitmap.getWidth()+ " \nHI:"+imagen.getImagenB().getHeight()+" WI."+imagen.getImagenB().getWidth(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.botonUndo:
                drawableView.undo();
                break;
            case R.id.botonPincelChico:
                float pc = pincel-5;
                if(pc <=0 && pc<50) {
                   pc = 20f;
                }
                pincel=pc;
                config.setStrokeWidth(pincel);
                break;
            case R.id.botonPincel:
                //Random random = new Random();
                //config.setStrokeColor(Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256)));
                float pg = pincel+5;
                if(pg <= 0 && pg<50){
                    pg = 20f;
                }
                pincel = pg;
                config.setStrokeWidth(pincel);
                break;
            case R.id.botonListo:
                guardarImagen();
                ArrayList<ColorMuestreo> cm = generarColorMuestreo(clasificar());
                irAMedicion(cm);
                //Toast.makeText(this, "W:"+bitmapR.getWidth()+" H:"+bitmapR.getHeight(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private ArrayList<ColorMuestreo> generarColorMuestreo(PatronRepresentativo[] clasificar) {
        ArrayList<ColorMuestreo>  colorList = new ArrayList<ColorMuestreo>();
        for (int i=0; i<clasificar.length; i++){
            ColorMuestreo c = new ColorMuestreo();
            c.setClase(clasificar[i].getClase());
            c.setColores(clasificar[i].getVector());
            colorList.add(c);
        }
        return colorList;
    }

    private void irAMedicion(ArrayList<ColorMuestreo> colorMuestreos) {

        String id_bitacora = getIntent().getStringExtra("id_bitacora");
        String nombre_bitacora = getIntent().getStringExtra("nombre_bitacora");
        String cantidad = getIntent().getStringExtra("cantidad");
        Bundle bundle1 = getIntent().getBundleExtra("muestreo");
        Muestreo m = (Muestreo) bundle1.getSerializable("muestreo");
        m.setColor_mtr(colorMuestreos);
        m.setImagen_mtr(imagenFile.toString());

        Bundle bundle = new Bundle();
        bundle.putSerializable("muestreo", m);

        Intent intent = new Intent(this, Medicion.class);
        intent.putExtra("id_bitacora", id_bitacora);
        intent.putExtra("nombre_bitacora", nombre_bitacora);
        intent.putExtra("muestreo",bundle);
        intent.putExtra("cantidad", cantidad);
        startActivity(intent);
    }

    private void guardarImagen() {
        int count = 0;

        File sdDirectory = Environment.getExternalStorageDirectory();
        File subDirectory = new File(sdDirectory.toString() + "/Pictures/Paint");

        if (subDirectory.exists()) {
            File[] existing = subDirectory.listFiles();
            for (File file : existing) {
                if (file.getName().endsWith(".jpg") || file.getName().endsWith(".png")) {
                    count++;
                }
            }

        } else {
            subDirectory.mkdir();
        }

        if (subDirectory.exists()) {
            File image = new File(subDirectory, "/drawing_" + (count + 1) + ".png");
            FileOutputStream fileOutputStream;
            try {
                fileOutputStream = new FileOutputStream(image);
                drawableView.obtainBitmap(drawableView.obtainBitmap()).compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                //mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                Toast.makeText(this, "W:"+drawableView.obtainBitmap(drawableView.obtainBitmap()).getWidth()+" H:"+drawableView.obtainBitmap(drawableView.obtainBitmap()).getHeight(), Toast.LENGTH_SHORT).show();
                fileOutputStream.flush();
                fileOutputStream.close();
                //Toast.makeText(this, "Guardada", Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {

            } catch (IOException e) {

            }

        }
    }

    private PatronRepresentativo[] clasificar() {
        ArrayList<Patron> instancias = obtenerInstancias();
        CMeans cmm = new CMeans(5);
        cmm.entrenar(instancias);
        cmm.clasificar(instancias);
        return cmm.getCentroides();
    }

    public ArrayList<Patron> obtenerInstancias(){
        imgNvoBitmap = drawableView.obtainBitmap(drawableView.obtainBitmap());
        ArrayList<Patron> aux = new ArrayList<>();
        for(int i=0; i< imgOriBitmap.getWidth();i++){
            for(int j=0; j <imgOriBitmap.getHeight();j++){
                //int pixel = imgNvoBitmap.getPixel(i,j);
                if(imgNvoBitmap.getPixel(i,j) == color) {
                    Colores color = new Colores(imgOriBitmap.getPixel(i, j));
                    PixelPatron pp = new PixelPatron(new double[]{color.getRed(),
                            color.getGreen(),
                            color.getBlue()}, "", i, j);
                    aux.add(pp);
                }
            }
        }
        return aux;
    }


}