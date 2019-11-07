package com.example.mfrbmv10.Modelos;

public class ColorMuestreo {
    String clase;
    double [] colores;

    public ColorMuestreo(String clase, double[] colores) {
        this.clase = clase;
        this.colores = colores;
    }

    public ColorMuestreo() {
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
