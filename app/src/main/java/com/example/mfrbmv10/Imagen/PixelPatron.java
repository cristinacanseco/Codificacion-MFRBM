package com.example.mfrbmv10.Imagen;

public class PixelPatron extends Patron {

    private int x;
    private int y;


    public PixelPatron(double[] vector, String clase,int x,int y) {
        super(vector, clase);
        this.x = x;
        this.y = y;

    }


    public PixelPatron(int n, int x, int y) {
        super(n);
        this.x = x;
        this.y = y;
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

}