package com.alexlopezramos.juegorectangulos;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class JuegoRectangulos extends SurfaceView implements SurfaceHolder.Callback {

    private ArrayList<Rectangulo> rectangulos;
    private int rectActivo;

    //Rectangulos
    private Rectangulo rectRojo;
    private Rectangulo rectAzul;

    //Hilo
    private Hilo hilo;

    //Paint
    private Paint paint;
    private Paint paintText;

    //Coordenadas
    private float tX, tY; //Al tocar
    private float tX2, tY2;

    //Booleanos
    private boolean rectSelected;
    private boolean empezar;
    private boolean fusionados;
    private boolean gameover;

    public JuegoRectangulos(Context context) {
        super(context);

        setBackgroundColor(Color.BLACK);
        getHolder().addCallback(this);

        //Rectangulos
        rectRojo = new Rectangulo();
        rectAzul = new Rectangulo();

        //Paint
        paint = new Paint();
        paintText = new Paint();

    }

    @Override
    protected void onDraw(Canvas canvas) {

        if(tX == 0 && tY == 0) {
            //Rectangulo Rojo
            rectRojo.setAlto(getHeight() / 3);
            rectRojo.setAncho(getWidth() / 3);
            rectRojo.setCoorX((getWidth()/2) - (rectRojo.getAncho()/2));
            rectRojo.setCoorY((getHeight()/2) - (rectRojo.getAlto()/2));

            //Rectangulo Azul con coordenadas aleatorias
            Random rnd = new Random();
            float rndX = rnd.nextInt(getWidth() / 3 * 2);
            float rndY = rnd.nextInt(getHeight()/3 * 2);
            rectAzul.setAlto(getHeight()/3);
            rectAzul.setAncho(getWidth()/3);
            rectAzul.setCoorX(rndX);
            rectAzul.setCoorY(rndY);

            fusionados = false;
        } else {
            empezar = true;
        }

        if(!fusionados) {
            paint.setColor(Color.BLUE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(30);
            canvas.drawRect(
                    rectAzul.getCoorX(), rectAzul.getCoorY(),
                    (rectAzul.getCoorX() + rectAzul.getAncho()),
                    (rectAzul.getCoorY() + rectAzul.getAlto()),
                    paint
            );

            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(30);
            canvas.drawRect(
                    rectRojo.getCoorX(), rectRojo.getCoorY(),
                    (rectRojo.getCoorX() + rectRojo.getAncho()),
                    (rectRojo.getCoorY() + rectRojo.getAlto()),
                    paint
            );
        } else {
            Rectangulo rectMorado = new Rectangulo(rectAzul.getCoorX(), rectAzul.getCoorY(), rectAzul.getAlto(), rectAzul.getAncho());

            paint.setColor(Color.MAGENTA);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawRect(
                    rectMorado.getCoorX(), rectMorado.getCoorY(),
                    (rectMorado.getCoorX() + rectMorado.getAncho()),
                    (rectMorado.getCoorY() + rectMorado.getAlto()),
                    paint
            );

            paintText.setColor(Color.WHITE);
            paintText.setTextSize(80);
            paintText.setTypeface(Typeface.create("Arial", Typeface.BOLD));
            paintText.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("GAME OVER", getWidth()/2, getHeight()/2, paintText);
            gameover = true;
        }

        if(!empezar) {
            paintText.setColor(Color.WHITE);
            paintText.setTextSize(80);
            paintText.setTypeface(Typeface.create("Arial", Typeface.BOLD));
            paintText.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("TOCA PARA EMPEZAR", getWidth()/2, getHeight()/2, paintText);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN: //Pulsar
                tX = event.getX();
                tY = event.getY();

                if(tX > rectRojo.getCoorX() && tX < rectRojo.getCoorX()+rectRojo.getAncho() && tY > rectRojo.getCoorY() && tY < rectRojo.getCoorY()+rectRojo.getAlto()) {
                    rectSelected = true;
                }
                break;
            case MotionEvent.ACTION_MOVE: //Mover
                tX2 = event.getX();
                tY2 = event.getY();

                if(rectSelected) {
                    rectRojo.setCoorX(rectRojo.getCoorX()-(tX - tX2));
                    rectRojo.setCoorY(rectRojo.getCoorY()-(tY - tY2));
                    tX = tX2;
                    tY = tY2;
                }
                break;
            case MotionEvent.ACTION_CANCEL: //Cancel
                break;
            case MotionEvent.ACTION_UP: //Soltar
                tX = event.getX();
                tY = event.getY();

                if(rectSelected) {
                    rectSelected = false;
                }

                if(rectAzul.getCoorX() - rectRojo.getCoorX() < 50f && rectAzul.getCoorX() - rectRojo.getCoorX() > -50f) {
                    if(rectAzul.getCoorY() - rectRojo.getCoorY() < 50f && rectAzul.getCoorY() - rectRojo.getCoorY() > -50f) {
                        rectRojo.setCoorX(rectAzul.getCoorX());
                        rectRojo.setCoorY(rectAzul.getCoorY());
                        fusionados = true;
                    }
                }

                if(gameover) {
                    gameover = false;
                    fusionados = false;
                    empezar = false;
                    tX = 0;
                    tY = 0;
                }

                break;
        }

        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        hilo = new Hilo(surfaceHolder, this);
        hilo.setRunning(true);
        hilo.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        hilo.setRunning(false);

        while(retry){
            try{
                hilo.join();
                retry = false;
            }
            catch(InterruptedException e){}
        }
    }
}
