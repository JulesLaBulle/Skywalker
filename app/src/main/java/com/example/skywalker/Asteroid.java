package com.example.skywalker;

import android.content.Context;
import android.widget.ImageView;
import android.widget.FrameLayout;
import android.view.ViewGroup;
import java.util.Random;

public class Asteroid {
    private ImageView asteroidImageView;
    private Context context;
    private int[] asteroidDrawables = {
            R.drawable.asteroid1,
            R.drawable.asteroid2,
            R.drawable.asteroid3,
            R.drawable.asteroid4
    };

    // Constructeur qui initialise l'astéroïde
    public Asteroid(Context context, FrameLayout gameLayout) {
        this.context = context;

        // Créer l'ImageView pour l'astéroïde
        asteroidImageView = new ImageView(context);

        // Sélectionner une image aléatoire pour l'astéroïde
        Random random = new Random();
        int drawableIndex = random.nextInt(asteroidDrawables.length);
        asteroidImageView.setImageResource(asteroidDrawables[drawableIndex]);

        // Ajouter l'astéroïde à la zone de jeu (FrameLayout)
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        gameLayout.addView(asteroidImageView, params);
    }

    // Méthode pour définir la position de l'astéroïde
    public void setPosition(float x, float y) {
        asteroidImageView.setX(x);
        asteroidImageView.setY(y);
    }

    // Méthode pour définir la taille de l'astéroïde
    public void setSize(int width, int height) {
        ViewGroup.LayoutParams params = asteroidImageView.getLayoutParams();
        params.width = width;
        params.height = height;
        asteroidImageView.setLayoutParams(params);
    }

    // Méthode pour déplacer l'astéroïde sur l'écran
    public void move(float deltaX, float deltaY) {
        asteroidImageView.setX(asteroidImageView.getX() + deltaX);
        asteroidImageView.setY(asteroidImageView.getY() + deltaY);
    }

    // Méthode pour récupérer la position X de l'astéroïde
    public float getX() {
        return asteroidImageView.getX();
    }

    // Méthode pour récupérer la position Y de l'astéroïde
    public float getY() {
        return asteroidImageView.getY();
    }

    // Méthode pour rendre l'astéroïde invisible (par exemple, lors d'une collision)
    public void hide() {
        asteroidImageView.setVisibility(ImageView.GONE);
    }
}
