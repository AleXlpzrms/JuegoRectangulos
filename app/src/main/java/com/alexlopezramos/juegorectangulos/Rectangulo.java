package com.alexlopezramos.juegorectangulos;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Rectangulo {
    private float coorX;
    private float coorY;
    private float alto;
    private float ancho;

    public Rectangulo(float coorX, float coorY, float alto, float ancho) {
        this.coorX = coorX;
        this.coorY = coorY;
        this.alto = alto;
        this.ancho = ancho;
    }

    public Rectangulo() {
    }

    public float getCoorX() {
        return coorX;
    }

    public void setCoorX(float coorX) {
        this.coorX = coorX;
    }

    public float getCoorY() {
        return coorY;
    }

    public void setCoorY(float coorY) {
        this.coorY = coorY;
    }

    public float getAlto() {
        return alto;
    }

    public void setAlto(float alto) {
        this.alto = alto;
    }

    public float getAncho() {
        return ancho;
    }

    public void setAncho(float ancho) {
        this.ancho = ancho;
    }
}
