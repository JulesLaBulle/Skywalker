package com.example.skywalker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable {

    private Thread gameThread;
    private boolean isPlaying;
    private SurfaceHolder holder;
    private Paint paint;

    private Bitmap background;
    private Bitmap spaceship;
    private int spaceshipX, spaceshipY;
    private boolean isInitialized = false; // Permet d'initialiser après la première frame

    public GameView(Context context) {
        super(context);
        holder = getHolder();
        paint = new Paint();

        // Charger l’image de fond (on ne redimensionne pas encore)
        background = BitmapFactory.decodeResource(getResources(), R.drawable.etoilefond);

        // Charger le vaisseau mais sans le redimensionner
        spaceship = BitmapFactory.decodeResource(getResources(), R.drawable.tie);
    }

    @Override
    public void run() {
        while (isPlaying) {
            update();
            draw();
            sleep();
        }
    }

    private void update() {
        // Logique du jeu ici
    }

    private void draw() {
        if (holder.getSurface().isValid()) {
            Canvas canvas = holder.lockCanvas();

            if (!isInitialized) {
                // Redimensionner le fond
                background = Bitmap.createScaledBitmap(background, getWidth(), getHeight(), false);

                // Redimensionner le vaisseau
                int newWidth = getWidth() / 6;
                int newHeight = newWidth;
                spaceship = Bitmap.createScaledBitmap(spaceship, newWidth, newHeight, false);

                // Positionner le vaisseau
                spaceshipX = getWidth() / 2 - spaceship.getWidth() / 2;
                spaceshipY = getHeight() - spaceship.getHeight() - 50;

                isInitialized = true; // Empêche de recalculer à chaque frame
            }

            // Dessiner l’arrière-plan
            canvas.drawBitmap(background, 0, 0, paint);

            // Dessiner le vaisseau
            canvas.drawBitmap(spaceship, spaceshipX, spaceshipY, paint);

            holder.unlockCanvasAndPost(canvas);
        }
    }

    private void sleep() {
        try {
            Thread.sleep(16);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        isPlaying = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void pause() {
        isPlaying = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
