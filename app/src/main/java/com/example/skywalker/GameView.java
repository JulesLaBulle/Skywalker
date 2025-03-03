package com.example.skywalker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {

    private Thread gameThread;
    private boolean isPlaying;
    private SurfaceHolder holder;
    private Paint paint;

    private Bitmap spaceship;
    private int spaceshipX, spaceshipY;
    private boolean isInitialized = false;

    private List<Asteroid> asteroids;  // Liste des astéroïdes
    private Random random;

    public GameView(Context context) {
        super(context);
        holder = getHolder();
        paint = new Paint();

        // Charger le vaisseau
        spaceship = BitmapFactory.decodeResource(getResources(), R.drawable.tie);

        // Initialisation des astéroïdes
        asteroids = new ArrayList<>();
        random = new Random();
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
        // Ajouter un nouvel astéroïde aléatoirement toutes les X frames
        if (random.nextInt(100) < 2) {  // Petit pourcentage pour ajouter un astéroïde aléatoirement
            addAsteroid();
        }

        // Mettre à jour la position des astéroïdes
        Iterator<Asteroid> iterator = asteroids.iterator();
        while (iterator.hasNext()) {
            Asteroid asteroid = iterator.next();
            asteroid.move(0, 10);  // Déplacement vers le bas

            // Si l'astéroïde sort de l'écran, on le supprime
            if (asteroid.getY() > getHeight()) {
                iterator.remove();
            }
        }
    }

    private void draw() {
        if (holder.getSurface().isValid()) {
            Canvas canvas = holder.lockCanvas();

            if (!isInitialized) {
                // Redimensionner le vaisseau une seule fois
                int newWidth = getWidth() / 3;
                int newHeight = getHeight() / 9;
                spaceship = Bitmap.createScaledBitmap(spaceship, newWidth, newHeight, false);
                spaceshipX = getWidth() / 2 - spaceship.getWidth() / 2;
                spaceshipY = getHeight() / 2 - spaceship.getHeight() / 2;

                isInitialized = true;
            }

            // Dessiner le vaisseau
            canvas.drawBitmap(spaceship, spaceshipX, spaceshipY, paint);

            // Dessiner les astéroïdes
            for (Asteroid asteroid : asteroids) {
                asteroid.draw(canvas, paint);
            }

            holder.unlockCanvasAndPost(canvas);
        }
    }

    private void sleep() {
        try {
            Thread.sleep(16);  // Environ 60 FPS
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour ajouter un nouvel astéroïde
    private void addAsteroid() {
        Asteroid asteroid = new Asteroid(getContext(), new FrameLayout(getContext()));
        // Positionner l'astéroïde au sommet de l'écran, à une position X aléatoire
        int xPosition = random.nextInt(getWidth() - 100);  // Moins 100 pour éviter de dépasser l'écran
        asteroid.setPosition(xPosition, -200);  // On positionne l'astéroïde hors de l'écran, en haut
        asteroid.setSize(100, 100);  // Définir une taille fixe ou variable selon les besoins
        asteroids.add(asteroid);
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
