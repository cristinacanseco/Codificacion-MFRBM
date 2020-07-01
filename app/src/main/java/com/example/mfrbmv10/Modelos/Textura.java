package com.example.mfrbmv10.Modelos;

public class Textura {

    private int id_textura;
    private String tipo_textura;

    public Textura() { }

    public Textura(int id_textura, String tipo_textura) {
        this.id_textura = id_textura;
        this.tipo_textura = tipo_textura;
    }

    public int getId_textura() {
        return id_textura;
    }

    public void setId_textura(int id_textura) {
        this.id_textura = id_textura;
    }

    public String getTipo_textura() {
        return tipo_textura;
    }

    public void setTipo_textura(String tipo_textura) {
        this.tipo_textura = tipo_textura;
    }
}
