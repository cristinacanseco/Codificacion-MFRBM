package com.example.mfrbmv10.Modelos;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class ColorMuestreo implements Serializable {
    String clase;
    double [] colores;

    public ColorMuestreo(String clase, double[] colores) {
        this.clase = clase;
        this.colores = colores;
    }

    public ColorMuestreo() {
    }

    protected ColorMuestreo(Parcel in) {
        clase = in.readString();
        colores = in.createDoubleArray();
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public double[] getColores() {
        return colores;
    }

    public void setColores(double[] colores) {
        this.colores = colores;
    }

}
