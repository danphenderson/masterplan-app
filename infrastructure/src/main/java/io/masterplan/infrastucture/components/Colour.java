package io.masterplan.infrastucture.components;

public final class Colour {

    public static final int COLOR_MAX = 255;

    private int r;
    private int g;
    private int b;
    private int o;


    public Colour(int r, int g, int b) {

        if(r < 0 || r > COLOR_MAX) {
            throw new IllegalArgumentException("r is " + r + ". r must be greater than 0 and less than equal to 255");
        }

        if(g < 0 || g > COLOR_MAX) {
            throw new IllegalArgumentException("");
        }

        if(b < 0 || b > COLOR_MAX) {
            throw new IllegalArgumentException("");
        }



        this.r = r;
        this.g = g;
        this.b = b;
        this.o = 255;
    }

    public Colour(int r, int g, int b, int o) {

        if(r < 0 || r > COLOR_MAX) {
            throw new IllegalArgumentException("r is " + r + ". r must be greater than 0 and less than equal to 255");
        }

        if(g < 0 || g > COLOR_MAX) {
            throw new IllegalArgumentException("");
        }

        if(b < 0 || b > COLOR_MAX) {
            throw new IllegalArgumentException("");
        }

        if(o < 0 || o > COLOR_MAX) {
            throw new IllegalArgumentException("o is  "+ o + ". o must be greater than 0 and less than equal to 255\"");
        }

        this.r = r;
        this.g = g;
        this.b = b;
        this.o = o;
    }


    public int getR(){
        return this.r;
    }

    public int getB(){
        return this.b;
    }

    public int getG() {
        return this.g;
    }

    public double getO() {
        return this.o;
    }

    public double[] getRGBO() {
        return new double[] {this.r / (float)COLOR_MAX, this.g / (float)COLOR_MAX,
                             this.b / (float)COLOR_MAX, this.o / (float)COLOR_MAX};
    }



}
