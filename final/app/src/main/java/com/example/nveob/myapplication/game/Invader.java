package com.example.nveob.myapplication.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

import com.example.nveob.myapplication.R;

import java.util.Random;

public class Invader {
    
    private RectF rectf;

    // bitmap que representa la nev del jugador
    protected Bitmap bitmap;

    // lo alto que va a estar el marciano
    private float length;
    private float height;

    // X lo lejos a la izq del rectangulo donde estara
    private float x;

    // X lo lejos a la izq del rectangulo donde estara
    private float y;

        //velocidad
    private float shipSpeed;

        //direccion
    private final int LEFT = 1;
    private final int RIGHT = 2;

    // indica la direccion de la nave y hacia donde
    private int shipMoving = RIGHT;

    private boolean isVisible;

    //Tiene color inicial
    private boolean incialcolor = true;

    private static Random r = new Random();

    public Invader(Context context, int row, int column, int screenY , int screenX ){
        rectf = new RectF();
        length = (float)screenX /15;
        height = (float)screenY /15;

        isVisible = true;

        int padding = screenX /120;

        y = row * (length + padding);

        x = column * (length + padding);

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.invader1);

        bitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (screenX/20),
                (int) (screenY/20),
                false);

        shipSpeed = 100;
    }



    public void setVisible(){
        this.isVisible = true;
    }

    public void setInvisible(){
        isVisible = false;
    }

    public RectF getRectf() {
        return rectf;
    }


    public Bitmap getBitmap() {
        return bitmap;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getlength() {
        return length;
    }

    public float getHeight() {
        return height;
    }


    public boolean update(long fps, int screenX){
        if(isVisible) {

            if (shipMoving == LEFT) {
                x = x - shipSpeed / fps;
            } else {
                x = x + shipSpeed / fps;
            }

            rectf.left = x;
            rectf.right = x + height;

            rectf.top = y;
            rectf.bottom = y + length;

            return this.getX() > screenX - this.getlength() || this.getX() < 0;
        }

        return false;

    }

    public void dropDownAndReverse(){
        if(shipMoving == LEFT){
            shipMoving = RIGHT;
        }else{
            shipMoving = LEFT;
        }
        y = height + y;
    }

    public boolean takeAim(){

        int t = r.nextInt(300);
        if(t==1){
            return true;
        }else{
            return false;
        }

    }

    public void changeImage(Context context){
        if(incialcolor){
            bitmap = BitmapFactory.decodeResource(
                    context.getResources(),
                    R.drawable.invader1);
            incialcolor = false;
        }else{

            bitmap = BitmapFactory.decodeResource(
                    context.getResources(),
                    R.drawable.invader5);
            incialcolor = true;
        }
    }

    public void changeImageRandom(Context context){
        int i = (int) (Math.random()*4);
        switch (i){
            case 0:
                bitmap = BitmapFactory.decodeResource(
                        context.getResources(),
                        R.drawable.invader1);
                break;
            case 1:
                bitmap = BitmapFactory.decodeResource(
                        context.getResources(),
                        R.drawable.invader3);
                break;

            case 2:
                bitmap = BitmapFactory.decodeResource(
                        context.getResources(),
                        R.drawable.invader5);
                break;
            case 3:
                bitmap = BitmapFactory.decodeResource(
                        context.getResources(),
                        R.drawable.invader7);
                break;

        }
    }
    /*Getters añadidos para testing*/
    public int getLEFT() {
        return LEFT;
    }

    public int getRIGHT() {
        return RIGHT;
    }


}
