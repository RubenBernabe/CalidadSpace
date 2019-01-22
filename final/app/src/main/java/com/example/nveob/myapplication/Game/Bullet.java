package com.example.nveob.myapplication.Game;

import android.graphics.RectF;

public class Bullet {

    private float x;
    private float y;

    private RectF rectf;

    // hacia que direccion dispara
    static int UP = 1;
    static int DOWN = 0;


    // no va a ningun lado
    private int heading = -1;
    private float speed = 350;

    //¿Está isActive?
    private boolean isActive = false;

    //Dimensiones
    private int width;
    private int height;

    //endregion

    //CONSTRUCTOR
    public Bullet(int screenY){
        this.width = 5;
        this.height = screenY / 20;
        this.rectf = new RectF();
    }

    public RectF getRectf() {
        return rectf;
    }

    public void setInactive(){
        this.isActive = false;
    }

    public void active(){
        this.isActive = true;
    }

    public boolean isActivated(){
       return this.isActive;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void changeDirection(){
        if (heading == DOWN){ //Dirección DOWN (=0)
            heading = UP;
        }else{ //Dirección UP (=1)
            heading = DOWN;
        }
    }


    public float getImpactPointY(){
        if(this.heading==DOWN){
            return y + height;
        }else{
            return y;
        }
    }

    public boolean shoot(float startX, float startY, int direction){
        if(!this.isActive){
            setX(startX);
            setY(startY);
            this.heading = direction;
            active();
            return true;
        }else{
            return false;
            //ya está disparando
        }
    }

    public void update(long fps){

        // solo un movimiento arriba o abajo
        if(heading==UP){
            y = y - speed /fps;
        }else{
            y = y + speed /fps;
        }

        // actualiza rect
        rectf.left = x;
        rectf.right = x + width;
        rectf.top = y;
        rectf.bottom = y + height;
    }

}
