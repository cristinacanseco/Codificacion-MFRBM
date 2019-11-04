package com.example.mfrbmv10.Modelos;

import java.io.Serializable;

public class Bitacora implements Serializable {

    public String nombre_btc;
    public String imagen_btc;
    public String fecha_btc;
    public String hora_btc;
    public String ubicacion_btc;
    public String coordenadas_btc;
    public String cantidad_btc;
    public String descripcion_btc;

    public Bitacora(){}

    public Bitacora(String nombre_btc, String imagen_btc, String fecha_btc, String hora_btc, String ubicacion_btc, String coordenadas_btc, String cantidad_btc, String descripcion_btc) {
        this.nombre_btc = nombre_btc;
        this.imagen_btc = imagen_btc;
        this.fecha_btc = fecha_btc;
        this.hora_btc = hora_btc;
        this.ubicacion_btc = ubicacion_btc;
        this.coordenadas_btc = coordenadas_btc;
        this.cantidad_btc = cantidad_btc;
        this.descripcion_btc = descripcion_btc;
    }

    public String getNombre_btc() {
        return nombre_btc;
    }

    public void setNombre_btc(String nombre_btc) {
        this.nombre_btc = nombre_btc;
    }

    public String getImagen_btc() {
        return imagen_btc;
    }

    public void setImagen_btc(String imagen_btc) {
        this.imagen_btc = imagen_btc;
    }

    public String getFecha_btc() {
        return fecha_btc;
    }

    public void setFecha_btc(String fecha_btc) {
        this.fecha_btc = fecha_btc;
    }

    public String getHora_btc() {
        return hora_btc;
    }

    public void setHora_btc(String hora_btc) {
        this.hora_btc = hora_btc;
    }

    public String getUbicacion_btc() {
        return ubicacion_btc;
    }

    public void setUbicacion_btc(String ubicacion_btc) {
        this.ubicacion_btc = ubicacion_btc;
    }

    public String getCoordenadas_btc() {
        return coordenadas_btc;
    }

    public void setCoordenadas_btc(String coordenadas_btc) {
        this.coordenadas_btc = coordenadas_btc;
    }

    public String getCantidad_btc() {
        return cantidad_btc;
    }

    public void setCantidad_btc(String cantidad_btc) {
        this.cantidad_btc = cantidad_btc;
    }

    public String getDescripcion_btc() {
        return descripcion_btc;
    }

    public void setDescripcion_btc(String descripcion_btc) {
        this.descripcion_btc = descripcion_btc;
    }
}
