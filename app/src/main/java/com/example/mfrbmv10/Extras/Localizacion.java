package com.example.mfrbmv10.Extras;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mfrbmv10.BuildConfig;
import com.example.mfrbmv10.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Localizacion implements ActivityCompat.OnRequestPermissionsResultCallback {

    protected Location mLastLocation;

    private String mLatitudeLabel;
    private String mLongitudeLabel;
    private String mLocalizacion;
    private FusedLocationProviderClient mFusedLocationClient;
    private Geocoder geocoder;
    private static final String TAG = Localizacion.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 10;
    private Fragment fragment;
    private Context context;
    private Activity activity;

    public Localizacion(Fragment fragment) {
        this.fragment = fragment;
        this.context = fragment.getContext();
        this.activity = fragment.getActivity();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.context);
        if (!checkPermissions()) {
            requestPermissions();

        } else {
            getLastLocation();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                Log.i(TAG, "Cancelaste la interación");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                showSnackbar(R.string.textwarn, R.string.settings,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                activity.startActivity(intent);
                            }
                        });
            }
        }
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(this.activity.findViewById(android.R.id.content),
                this.activity.getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(this.activity.getString(actionStringId), listener).show();
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this.context,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(this.activity,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this.activity,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        if (shouldProvideRationale) {
            Log.i(TAG, "Desplegando permisos relacionados para proveer un contexto adicional");

            showSnackbar(R.string.textwarn, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            startLocationPermissionRequest();
                        }
                    });

        } else {
            Log.i(TAG, "Se requiere el permiso para ingresar a la localización");
            startLocationPermissionRequest();
        }
    }

    private void getLastLocation() {
        if (this.activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && this.activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this.activity, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();
                            mLatitudeLabel = String.format(Locale.ENGLISH, "%f",
                                    mLastLocation.getLatitude());
                            mLongitudeLabel = String.format(Locale.ENGLISH, "%f",
                                    mLastLocation.getLongitude());
                            nombreCoordenada(mLastLocation);
                        } else {
                            Log.w(TAG, "getLastLocation:exception", task.getException());
                        }
                    }
                });
    }


    public void nombreCoordenada(Location location) {
        try {
            geocoder = new Geocoder(this.context);
            List<Address> addressList = null;
            addressList = geocoder.getFromLocation(Double.parseDouble(mLatitudeLabel), Double.parseDouble(mLongitudeLabel), 1);
            String country = addressList.get(0).getCountryName();
            String city = addressList.get(0).getLocality();
            String state = addressList.get(0).getAdminArea();
            mLocalizacion = ""+city+","+state+","+country;
        }catch (IOException e){
            e.printStackTrace();
            Toast.makeText(this.context, ""+e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public String getUbicacion(){
        return ""+mLocalizacion;
    }

    public String getCoordenadas(){
        return ""+mLatitudeLabel + ","+mLongitudeLabel;
    }
}