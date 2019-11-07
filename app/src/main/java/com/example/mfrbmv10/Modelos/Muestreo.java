package com.example.mfrbmv10.Modelos;

import java.io.Serializable;
import java.util.ArrayList;

public class Muestreo implements Serializable {

    public String nombre_mtr;
    public String imagen_mtr;
    public String fecha_mtr;
    public String hora_mtr;
    public String ubicacion_mtr;
    public String coordenadas_mtr;
    public String forma_mtr;
    public String textura_mtr;
    public ArrayList<ColorMuestreo> color_mtr;
    public ArrayList<String> dimension_mtr;


    public Muestreo() {
    }

    public Muestreo(String nombre_mtr, String imagen_mtr, String fecha_mtr, String hora_mtr, String ubicacion_mtr, String coordenadas_mtr, String forma_mtr, String textura_mtr, ArrayList<ColorMuestreo> color_mtr, ArrayList<String> dimension_mtr) {

        this.nombre_mtr = nombre_mtr;
        this.imagen_mtr = imagen_mtr;
        this.fecha_mtr = fecha_mtr;
        this.hora_mtr = hora_mtr;
        this.ubicacion_mtr = ubicacion_mtr;
        this.coordenadas_mtr = coordenadas_mtr;
        this.forma_mtr = forma_mtr;
        this.textura_mtr = textura_mtr;
        this.color_mtr = color_mtr;
        this.dimension_mtr = dimension_mtr;
    }

    public String getNombre_mtr() {
        return nombre_mtr;
    }

    public void setNombre_mtr(String nombre_mtr) {
        this.nombre_mtr = nombre_mtr;
    }

    public String getImagen_mtr() {
        return imagen_mtr;
    }

    public void setImagen_mtr(String imagen_mtr) {
        this.imagen_mtr = imagen_mtr;
    }

    public String getForma_mtr() {
        return forma_mtr;
    }

    public void setForma_mtr(String forma_mtr) {
        this.forma_mtr = forma_mtr;
    }

    public String getTextura_mtr() {
        return textura_mtr;
    }

    public void setTextura_mtr(String textura_mtr) {
        this.textura_mtr = textura_mtr;
    }

    public ArrayList<ColorMuestreo> getColor_mtr() {
        return color_mtr;
    }

    public void setColor_mtr(ArrayList<ColorMuestreo> color_mtr) {
        this.color_mtr = color_mtr;
    }

    public ArrayList<String> getDimension_mtr() {
        return dimension_mtr;
    }

    public void setDimension_mtr(ArrayList<String> dimension_mtr) { this.dimension_mtr = dimension_mtr; }

    public String getUbicacion_mtr() {
        return ubicacion_mtr;
    }

    public void setUbicacion_mtr(String ubicacion_mtr) {
        this.ubicacion_mtr = ubicacion_mtr;
    }

    public String getCoordenadas_mtr() {
        return coordenadas_mtr;
    }

    public void setCoordenadas_mtr(String coordenadas_mtr) {
        this.coordenadas_mtr = coordenadas_mtr;
    }

    public String getFecha_mtr() {
        return fecha_mtr;
    }

    public void setFecha_mtr(String fecha_mtr) {
        this.fecha_mtr = fecha_mtr;
    }

    public String getHora_mtr() {
        return hora_mtr;
    }

    public void setHora_mtr(String hora_mtr) {
        this.hora_mtr = hora_mtr;
    }

}
