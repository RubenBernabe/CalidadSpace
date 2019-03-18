package com.example.nveob.myapplication.game;

import android.graphics.RectF;

public class  Bullet {

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

    public void setRectf(RectF rectf) {
        this.rectf = rectf;
    }

    public static int getUP() {
        return UP;
    }

    public static void setUP(int UP) {
        Bullet.UP = UP;
    }

    public static int getDOWN() {
        return DOWN;
    }

    public static void setDOWN(int DOWN) {
        Bullet.DOWN = DOWN;
    }

    public int getHeading() {
        return heading;
    }

    public void setHeading(int heading) {
        this.heading = heading;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
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
