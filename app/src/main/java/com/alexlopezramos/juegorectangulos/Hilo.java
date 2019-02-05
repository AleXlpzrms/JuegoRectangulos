package com.alexlopezramos.juegorectangulos;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class Hilo extends Thread {

    private SurfaceHolder surfHolder;
    private JuegoRectangulos view;
    private boolean run;

    public Hilo(SurfaceHolder surfHolder, JuegoRectangulos view) {
        this.surfHolder = surfHolder;
        this.view = view;
        this.run = false;
    }

    public void setRunning(boolean run) {
        this.run = run;
    }

    public void run() {
        Canvas canvas;
        while(run){ //Habilita la edición multiple
            canvas = null;
            try{
                canvas = surfHolder.lockCanvas(null);

                if(canvas != null) {
                    synchronized (surfHolder) {
                        view.postInvalidate(); //Pinta
                    }
                }
            }
            finally{
                if(canvas != null)
                    surfHolder.unlockCanvasAndPost(canvas); //Desbloqueamos el canvas
            }
        }
    }
}
