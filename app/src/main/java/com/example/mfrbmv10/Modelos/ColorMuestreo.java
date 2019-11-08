package com.example.mfrbmv10.Modelos;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class ColorMuestreo implements Serializable, Parcelable {
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

    public static final Creator<ColorMuestreo> CREATOR = new Creator<ColorMuestreo>() {
        @Override
        public ColorMuestreo createFromParcel(Parcel in) {
            return new ColorMuestreo(in);
        }

        @Override
        public ColorMuestreo[] newArray(int size) {
            return new ColorMuestreo[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(clase);
        parcel.writeDoubleArray(colores);
    }
}
