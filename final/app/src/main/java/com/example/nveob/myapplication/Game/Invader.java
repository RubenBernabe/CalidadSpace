package com.example.nveob.myapplication.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

import com.example.nveob.myapplication.R;

public class Invader {
    
    private RectF rectf;

    // bitmap que representa la nev del jugador
    private Bitmap bitmap;

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

    public Invader(Context context, int row, int column, int screenY , int screenX ){
        rectf = new RectF();
        length = screenX /15;
        height = screenY /15;

        isVisible = true;

        int padding = screenX /120;

        y = row * (length + padding);

        x = column * (length + padding);

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.invader1);

        shipSpeed = 100;
    }

    public Invader(Context context, int x, int y, int screenY, int screenX, boolean extra){
        rectf = new RectF();
        length = screenX/15;
        height = screenY/15;

        isVisible = true;
        y = y;
        x = x;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.invader3);

        shipSpeed = 300;

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


    public void update(long fps, int screenX){
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
        }

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
        int random = (int) Math.random()*10 +1;
        if(random==1){
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

    public void changeImageRamdom(Context context){
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
}
