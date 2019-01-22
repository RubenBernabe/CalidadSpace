package com.example.nveob.myapplication.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

import com.example.nveob.myapplication.R;

public class PlayerShip {
    RectF rect;


    // nave representada por bitmap
    private Bitmap bitmap;

    // largo y ancho
    private float length;
    private float height;

    // X coordenada (igual que marcianos)
    private float x;
    //Y coordenada (igual que marcianos)
    private float y;

    // pixels por segundo que s emovera
    private float shipSpeed;

    // maneras que tiene de moverse
    public final int STOPPED = 0;
    public final int LEFT = 1;
    public final int RIGHT = 2;
    public final int UP = 3;
    public final int DOWN = 4;

    // maneras que tiene de moverse
    private int shipMoving = STOPPED;

    //color inicial nave
    private boolean initialcolor = true;

    // constructor
    public PlayerShip(Context context, int screenX, int screenY){

        // Inicializa vacio
        rect = new RectF();
        x = screenX / 2;
        y = screenY - 20;
        length = screenX/10;
        height = screenY/10;


        // Inicializa el bitmap
        bitmap = BitmapFactory.decodeResource(
                context.getResources(),
                R.drawable.playership1);

        // ajusta el bitmap
        bitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (length),
                (int) (height),
                false);

        // que rapdio se mueve por pixels segundo
        shipSpeed = 200;
    }

    public RectF getRect(){
        return rect;
    }

    public Bitmap getBitmap(){
        return bitmap;
    }

    public float getX(){
        return x;
    }

    public float getY() {
        return y;
    }

    public float getLength(){
        return length;
    }

    public float getHeight() {
        return height;
    }

    public void setMovementState(int state) {
        this.shipMoving = state;
    }


    public void update(long fps){
        if(shipMoving == LEFT){
            if(x>0){
                x = x - shipSpeed / fps;
            }
        }

        if(shipMoving == RIGHT){
            if(x<length*9){
                x = x + shipSpeed / fps;
            }
        }
        if (shipMoving == UP){
            if (y>0){
                y = y - shipSpeed / fps;


            }
        }
        if (shipMoving == DOWN){

                y = y + shipSpeed / fps;

            
        }


        // actualiza rect
        rect.top = y;
        rect.bottom = y + height;
        rect.left = x;
        rect.right = x + length;

    }


    public void chanImage(Context context){
        if(initialcolor){
            bitmap = BitmapFactory.decodeResource(
                    context.getResources(),
                    R.drawable.playership1);
            initialcolor=false;
        }else{
            bitmap = BitmapFactory.decodeResource(
                    context.getResources(),
                    R.drawable.playership2);
            initialcolor=true;
        }
        bitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (length),
                (int) (height),
                false);
    }


    public void changeImageRandom(Context context){
        int i= (int) (Math.random()*4);

        switch (i){
            case 0:
                bitmap = BitmapFactory.decodeResource(
                        context.getResources(),
                        R.drawable.playership1);
                break;
            case 1:

                bitmap = BitmapFactory.decodeResource(
                        context.getResources(),
                        R.drawable.playership2);
                break;
            case 2:

                bitmap = BitmapFactory.decodeResource(
                        context.getResources(),
                        R.drawable.playership5);
                break;
            case 3:

                bitmap = BitmapFactory.decodeResource(
                        context.getResources(),
                        R.drawable.playership4);
                break;
        }

        bitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (length),
                (int) (height),
                false);
    }

    public void desaparecer (){
        x = -300;
        //y = (int)(Math.random() * getY()) - this.getHeight();
    }

    public void aparecer(int screenX){
        x = (int) (Math.random() * screenX)- this.length;
    }



}
