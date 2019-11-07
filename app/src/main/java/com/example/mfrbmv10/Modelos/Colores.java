package com.example.mfrbmv10.Modelos;

import android.graphics.Color;

import static android.graphics.Color.pack;

public class Colores {

    private  int red, green, blue, resultante,alpha;

    public Colores(int pixel) {
        alpha = Color.alpha(pixel);
        red = Color.red(pixel);
        green = Color.green(pixel);
        blue = Color.blue(pixel);
        pack(red, green, blue);

        resultante = (alpha & 0xff) << 24 | (red & 0xff) << 16 | (green & 0xff) << 8 | (blue & 0xff);


    }


    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public int getResultante() {
        return resultante;
    }

}
