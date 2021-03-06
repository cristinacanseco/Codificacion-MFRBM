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
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mfrbmv10.Extras.Medicion;
import com.example.mfrbmv10.Modelos.ColorMuestreo;
import com.example.mfrbmv10.Modelos.ColorPaleta;
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

    private String fileUri;
    private ArrayList<Integer> paleta;

    private int color;
    private float pincel;

    public ImageView imagen_pv, imagen_nn;
    private DrawableView drawableView;
    private ImageButton botonPincelGrande, undo, listo, botonPincelChico;
    private DrawableViewConfig config;

    public static final int DENSITY_HIGH = 320;
    //public static final int DENSITY_HIGH = 240;

    public int densidad;

    public ProgressBar pb_camara;

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
        imagen_nn = (ImageView) findViewById(R.id.imagen_nn);
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
        pb_camara =  findViewById(R.id.pb_camara);
        pb_camara.setVisibility(View.INVISIBLE);
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
        File fileImagen = crearDirectorioPublico("Quitacora");

        String timeStamp="";
        String imagenFileName="";

        boolean isCreada=fileImagen.exists();
        if(isCreada==false){
            isCreada=fileImagen.mkdirs();
        }

        if(isCreada==true){
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
        densidad = bitmap.getDensity();
        Uri imageUri = Uri.fromFile(imagenFile);
        fileUri = String.valueOf(new File(imageUri.getPath()));
        imagen_pv.setImageURI(imageUri);
        imagen = new Imagen(imagen_pv.getDrawable());

        imgOriBitmap = imagen.getImagenB();
        imgOriBitmap.setDensity(densidad);
        configurarCanvas();
    }


    private void configurarCanvas() {
        config = new DrawableViewConfig();
        config.setStrokeColor(color);
        config.setShowCanvasBounds(true);
        config.setStrokeWidth(pincel);
        config.setCanvasHeight(imgOriBitmap.getHeight());
        config.setCanvasWidth(imgOriBitmap.getWidth());

        drawableView.setConfig(config);
        drawableView.setBackground(imagen.getImagenD());
        drawableView.onScaleChange(0.75f);
 }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.botonUndo:
                drawableView.undo();
                break;
            case R.id.botonPincelChico:
                float pc = pincel-10;
                if(pc <=0 && pc<100) {
                   pc = 20f;
                }
                pincel=pc;
                config.setStrokeWidth(pincel);
                break;
            case R.id.botonPincel:
                float pg = pincel+10;
                if(pg <= 0 && pg<100){
                    pg = 20f;
                }
                pincel = pg;
                config.setStrokeWidth(pincel);
                break;
            case R.id.botonListo:
                pb_camara.setVisibility(View.VISIBLE);
                guardarImagen();
                obtenerInstancias();
                pb_camara.setVisibility(View.INVISIBLE);
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
        Toast.makeText(this, "Colores obtenidos", Toast.LENGTH_SHORT).show();
        return colorList;
    }

    private void irAMedicion() {

        String id_bitacora = getIntent().getStringExtra("id_bitacora");
        String nombre_bitacora = getIntent().getStringExtra("nombre_bitacora");
        String cantidad = getIntent().getStringExtra("cantidad");
        Bundle bundle1 = getIntent().getBundleExtra("muestreo");
        Muestreo m = (Muestreo) bundle1.getSerializable("muestreo");
        m.setColor_mtr(paleta);
        m.setImagen_mtr(fileUri);

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
                fileOutputStream.flush();
                fileOutputStream.close();
                Toast.makeText(this, "Imagaen Guardada", Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                Toast.makeText(this, "Ups... ocurrió un problema", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(this, "Ups... ocurrió un problema "+e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void guardarImagenBitmap(Bitmap bitmapIB) {

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
            File image = new File(subDirectory, "/imagenBitmapResize_" + (count + 1) + ".png");
            FileOutputStream fileOutputStream;
            try {
                fileOutputStream = new FileOutputStream(image);
                bitmapIB.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                Toast.makeText(this, "Imagaen Guardada", Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                Toast.makeText(this, "Ups... ocurrió un problema", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(this, "Ups... ocurrió un problema "+e.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }

    public void obtenerInstancias(){
        imgNvoBitmap = drawableView.obtainBitmap(drawableView.obtainBitmap());
        imgNvoBitmap.setDensity(densidad);

        //Generar bitmap original
        imagen_pv.setImageBitmap(Bitmap.createScaledBitmap(imgOriBitmap, imgOriBitmap.getWidth() / 2, imgOriBitmap.getHeight() / 2, false));
        Imagen imagenOriginal = new Imagen(imagen_pv.getDrawable());
        Bitmap imgOriginalBitmap = imagenOriginal.getImagenB();
        imgOriginalBitmap.setDensity(densidad);

        //Generar bitmap nuevo
        imagen_nn.setImageBitmap(Bitmap.createScaledBitmap(imgNvoBitmap, imgNvoBitmap.getWidth() / 2, imgNvoBitmap.getHeight() / 2, false));
        Imagen imagenNuevo = new Imagen(imagen_nn.getDrawable());
        Bitmap imgNuevoBitmap = imagenNuevo.getImagenB();
        imgNuevoBitmap.setDensity(densidad);

        ArrayList<ColorPaleta> aux = new ArrayList<>();

        for (int i = 0; i < imgOriginalBitmap.getWidth(); i++) {
            for (int j = 0; j < imgOriginalBitmap.getHeight(); j++) {
                if (imgNuevoBitmap.getPixel(i, j) == color) {
                    Colores color = new Colores(imgOriginalBitmap.getPixel(i, j));
                    ColorPaleta cp = new ColorPaleta(color.getRed(), color.getGreen(), color.getBlue());
                    aux.add(cp);
                }
            }
        }


        if(aux.size()==0){
            generarPaleta(imgOriBitmap);
        }else {

            int div=0;

            if(aux.size()>10){
                div=10;
            }else{
                div = 2;
            }

            int tam = (int) Math.sqrt(aux.size());
            div = tam;

            Bitmap.Config conf = Bitmap.Config.ARGB_8888;
            Bitmap bmp = Bitmap.createBitmap(div, tam, conf);

            int x = 0;
            for (int i = 0; i < div; i++) {
                for (int j = 0; j < tam; j++) {
                    int r = aux.get(x).getR();
                    int g = aux.get(x).getG();
                    int b = aux.get(x).getB();
                    bmp.setPixel(i, j, Color.rgb(r, g, b));
                    x++;
                }
            }

            guardarImagenBitmap(bmp);
            generarPaleta(bmp);
        }
    }

    private void generarPaleta(Bitmap bitmaps) {
        paleta = new ArrayList<Integer>();
        Palette p = Palette.from(bitmaps).generate();

        if(p.getVibrantSwatch() != null){
            paleta.add(0,p.getVibrantSwatch().getRgb());
            paleta.add(1,p.getVibrantSwatch().getBodyTextColor());
        }else{
            paleta.add(0,Color.BLACK);
            paleta.add(1,Color.WHITE);
        }

        if(p.getDarkVibrantSwatch() != null){
            paleta.add(2,p.getDarkVibrantSwatch().getRgb());
            paleta.add(3,p.getDarkVibrantSwatch().getBodyTextColor());
        }else{
            paleta.add(2,Color.BLACK);
            paleta.add(3,Color.WHITE);
        }

        if(p.getLightVibrantSwatch() != null){
            paleta.add(4,p.getLightVibrantSwatch().getRgb());
            paleta.add(5,p.getLightVibrantSwatch().getBodyTextColor());
        }else{
            paleta.add(4,Color.BLACK);
            paleta.add(5,Color.WHITE);
        }

        if(p.getMutedSwatch() != null){
            paleta.add(6,p.getMutedSwatch().getRgb());
            paleta.add(7,p.getMutedSwatch().getBodyTextColor());
        }else{
            paleta.add(6,Color.BLACK);
            paleta.add(7,Color.WHITE);
        }

        if(p.getDarkMutedSwatch()!= null){
            paleta.add(8,p.getDarkMutedSwatch().getRgb());
            paleta.add(9,p.getDarkMutedSwatch().getBodyTextColor());
        }else{
            paleta.add(8,Color.BLACK);
            paleta.add(9,Color.WHITE);
        }

        if(p.getLightMutedSwatch() != null){
            paleta.add(10,p.getLightMutedSwatch().getRgb());
            paleta.add(11,p.getLightMutedSwatch().getBodyTextColor());
        }else{
            paleta.add(10,Color.BLACK);
            paleta.add(11,Color.WHITE);
        }

        irAMedicion();
    }



}
