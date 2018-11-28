package com.example.nveob.spaceinvaders.Game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

import com.example.nveob.spaceinvaders.R;

public class PlayerShip {

    RectF rect;

    // nave representada por bitmap
    private Bitmap bitmap;

    // largo y ancho
    private float length;
    private float height;

    // X coordenada (igual que marcianos)
    private float x;
    private float sX;
    private float sY;

    // Y coordenada (igual que marcianos)
    private float y;

    // pixels por segundo que s emovera
    private float shipSpeed;

    // maneras que tiene de moverse
    public final int STOPPED = 0;
    public final int LEFT = 1;
    public final int RIGHT = 2;

    // direccion en la que se mueve
    private int shipMoving = STOPPED;

    // constructor
    public PlayerShip(Context context, int screenX, int screenY){

        // Inicializa vacio
        rect = new RectF();
        sX=screenX;
        sY=screenY;
        length = screenX/10;
        height = screenY/10;

        // inicia la nave mas o menos centrada
        x = screenX / 2;
        y = screenY - 20;

        // Inicializa el bitmap
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.playership1);

        // ajusta bitmap
        bitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (length),
                (int) (height),
                false);

        // que rapdio se mueve por pixels segundo
        shipSpeed = 350;
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

    public float getLength(){
        return length;
    }

    public float getHeight(){
        return height;
    }

    // para cambiar o poner si la nave se esta moviedno o no
    public void setMovementState(int state){
        shipMoving = state;
    }


    // actualiza viendo si l anave s emueve y las coordenadas
    public void update(long fps){
        if(shipMoving == LEFT){
            if(x-shipSpeed>=-330) //No se salga de pantalla
                x = x - shipSpeed / fps;
        }

        if(shipMoving == RIGHT){
            if((x+shipSpeed)<=(sX+230)) //No se salga de pantalla
                x = x + shipSpeed / fps;
        }

        // actualiza rect
        rect.top = y;
        rect.bottom = y + height;
        rect.left = x;
        rect.right = x + length;

    }

    public void changePlayerColor(Context context){
        int r = (int)(Math.random() * 4);

        switch (r){
            case 0:
                // Inicializa el bitmap
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.playership1);
                break;
            case 1:
                // Inicializa el bitmap
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.playership2);
                break;
            case 2:
                // Inicializa el bitmap
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.playership3);
                break;
            case 3:
                // Inicializa el bitmap
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.playership4);
                break;
            case 4:
                // Inicializa el bitmap
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.playership5);
                break;
        }

        // ajusta bitmap
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) (length), (int) (height), false);

    }
}
