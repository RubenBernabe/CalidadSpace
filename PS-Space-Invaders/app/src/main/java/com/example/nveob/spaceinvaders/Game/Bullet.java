package com.example.nveob.spaceinvaders.Game;

import android.graphics.RectF;

public class Bullet {

    private float x;
    private float y;

    private RectF rect;

    // hacia que direccion dispara
    public final int UP = 0;
    public final int DOWN = 1;

    // no va a ningun lado
    int heading = -1;
    float speed =  350;

    private int width = 5;
    private int height;

    private boolean isActive;

    public Bullet(int screenY) {

        height = screenY / 20;
        isActive = false;

        rect = new RectF();
    }

    public RectF getRect(){
        return  rect;
    }

    public boolean getStatus(){
        return isActive;
    }

    public void setInactive(){
        isActive = false;
    }

    public float getImpactPointY(){
        if (heading == DOWN){
            return y + height;
        }else{
            return  y;
        }

    }

    public boolean shoot(float startX, float startY, int direction) {
        if (!isActive) {
            x = startX;
            y = startY;
            heading = direction;
            isActive = true;
            return true;
        }

        // bala esta activa
        return false;
    }

    public void update(long fps){

        // solo un movimiento arriba o abajo
        if(heading == UP){
            y = y - speed / fps;
        }else{
            y = y + speed / fps;
        }

        // actualiza rect
        rect.left = x;
        rect.right = x + width;
        rect.top = y;
        rect.bottom = y + height;

    }

}
