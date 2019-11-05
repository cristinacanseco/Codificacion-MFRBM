package com.example.mfrbmv10.Modelos;

public class Forma {
    private int id_forma;
    private String tipo_forma;

    public Forma() {
    }

    public Forma(int id_forma, String tipo_forma) {
        this.id_forma = id_forma;
        this.tipo_forma = tipo_forma;
    }

    public int getId_forma() {
        return id_forma;
    }

    public void setId_forma(int id_forma) {
        this.id_forma = id_forma;
    }

    public String getTipo_forma() {
        return tipo_forma;
    }

    public void setTipo_forma(String tipo_forma) {
        this.tipo_forma = tipo_forma;
    }
}
