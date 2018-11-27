package com.example.nveob.spaceinvaders.Game;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.nveob.spaceinvaders.R;

import java.io.IOException;

public class SpaceInvadersView extends SurfaceView implements Runnable {

    Context context;

    // This is our thread
    private Thread gameThread = null;

    // Our SurfaceHolder to lock the surface before we draw our graphics
    private SurfaceHolder ourHolder;

    // boolean que dira
    // cuando el juego se ejecuta o no.
    private volatile boolean playing;

    // el juego inicia pausado
    private boolean paused = true;

    // objetos canvas y paint, para el fondo y demas
    private Canvas canvas;
    private Drawable fondo;
    private Paint paint;

    // monitoriza el frame rate
    private long fps;

    // usado para calcular los fps fps
    private long timeThisFrame;

    // tamaño de pantalla en pixels
    private int screenX;
    private int screenY;

    // nave
    private PlayerShip playerShip;

    // balas del jugador
    private Bullet bullet;

    // balas de marcianos
    private Bullet[] invadersBullets = new Bullet[200];
    private int nextBullet;
    private int maxInvaderBullets = 10;

    // hasta 60 marcianos
    Invader[] invaders = new Invader[60];
    int numInvaders = 0;

    // los escudos de la nave(por implemetnar, pero el codigo esta hecho)
    private DefenceBrick[] bricks = new DefenceBrick[400];
    private int numBricks;

    // sonidos
    private SoundPool soundPool;
    private int playerExplodeID = -1;
    private int invaderExplodeID = -1;
    private int shootID = -1;
    private int damageShelterID = -1;
    private int uhID = -1;
    private int ohID = -1;

    // puntuacion
    int score = 0;

    // vidas
    private int lives = 3;

    // sonido
    private long menaceInterval = 1000;
    // sonido despues del otro
    private boolean uhOrOh;
    // cuando se ejecuto sonido por ultima vez
    private long lastMenaceTime = System.currentTimeMillis();

    // cuando se inicializa el gameview
    // This special constructor method runs
    public SpaceInvadersView(Context context, int x, int y) {

        super(context);

        // crea copia del contexto para usar por otros metodos
        this.context = context;

        // Initialize ourHolder and paint objects
        ourHolder = getHolder();
        paint = new Paint();

        screenX = x;
        screenY = y;

        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,0);

        try{
            // crea objetos de 2 clases requeridas
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;

            // carga sonidos
            descriptor = assetManager.openFd("shoot.ogg");
            shootID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("invaderexplode.ogg");
            invaderExplodeID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("damageshelter.ogg");
            damageShelterID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("playerexplode.ogg");
            playerExplodeID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("damageshelter.ogg");
            damageShelterID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("uh.ogg");
            uhID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("oh.ogg");
            ohID = soundPool.load(descriptor, 0);

        }catch(IOException e){
            // error
            Log.e("error", "failed to load sound files");
        }

        prepareLevel();
    }

    private void prepareLevel(){

        // inicializamos los objetos

        // creamos nave jugador
        playerShip = new PlayerShip(context, screenX, screenY);

        // preparamos balas jugador
        bullet = new Bullet(screenY);

        // inicializamos array de balas de marcianos
        for(int i = 0; i < invadersBullets.length; i++){
            invadersBullets[i] = new Bullet(screenY);
        }

        // creamos marcianos
        numInvaders = 0;
        for(int column = 0; column < 6; column ++ ){
            for(int row = 0; row < 5; row ++ ){
                invaders[numInvaders] = new Invader(context, row, column, screenX, screenY);
                numInvaders ++;
            }
        }

        // reseteamos el nivel de amenaza
        menaceInterval = 1000;

        // construimos los refugios

        numBricks = 0;
        for(int shelterNumber = 0; shelterNumber < 4; shelterNumber++){
            for(int column = 0; column < 10; column ++ ) {
                for (int row = 0; row < 5; row++) {
                    bricks[numBricks] = new DefenceBrick(row, column, shelterNumber, screenX, screenY);
                    numBricks++;
                }
            }
        }

    }

    @Override
    public void run() {
        while (playing) {

            // capturamos el tiempo en milisegundos
            long startFrameTime = System.currentTimeMillis();

            // actualizamos el frame
            if(!paused){
                update();
            }

            // dibujamos el frame
            draw();

            // calculamos los fps
            // para que nos sirva para animaciones de tiempo y demas.
            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }

            // haremos algo mas por aqui
            // reproducir sonido dependiendo del nivel de amenaza
            if(!paused) {
                if ((startFrameTime - lastMenaceTime) > menaceInterval) {
                    if (uhOrOh) {
                        // Play Uh
                        soundPool.play(uhID, 1, 1, 0, 0, 1);

                    } else {
                        // Play Oh
                        soundPool.play(ohID, 1, 1, 0, 0, 1);
                    }

                    // resetear el ultimo tiempo
                    lastMenaceTime = System.currentTimeMillis();
                    // alterar el valor de uhoroh
                    uhOrOh = !uhOrOh;
                }
            }

        }

    }

    private void update(){

        // un marciano volvio al tocar la pantalla
        boolean bumped = false;

        // ha perdido el jugador
        boolean lost = false;

        // mover la nave
        playerShip.update(fps);

        // actualizar lo marcianos si se ven
        for(int i = 0; i < numInvaders; i++){

            if(invaders[i].getVisibility()) {
                // mover el sig marciano
                invaders[i].update(fps);

                // quiere disparar?
                if(invaders[i].takeAim(playerShip.getX(),
                        playerShip.getLength())){

                    // lo intenta y dispara
                    if(invadersBullets[nextBullet].shoot(invaders[i].getX()
                                    + invaders[i].getLength() / 2,
                            invaders[i].getY(), bullet.DOWN)) {

                        // ha dispardo
                        // prepara para el sig disparo
                        nextBullet++;

                        // vuelve al principio si ha llegado al ultimo
                        if (nextBullet == maxInvaderBullets) {
                            // no dispara otra bala hasta que no termina el viaje de una.
                            nextBullet = 0;
                        }
                    }
                }
                if (invaders[i].getX() > screenX - invaders[i].getLength()
                        || invaders[i].getX() < 0){

                    bumped = true;

                }
            }

        }

        // si un marciano ha dado l avuelta en el lado de la pantalla
        if(bumped){

            // mueve a los marcianos hacia abajo y cambia de posicion
            for(int i = 0; i < numInvaders; i++){
                invaders[i].dropDownAndReverse();
                // Have the invaders landed
                if(invaders[i].getY() > screenY - screenY / 10){
                    lost = true;
                }
            }

            // incrementa nivel
            // haciendo el sonido mas frecuente
            menaceInterval = menaceInterval - 80;
        }

        if(lost){
            prepareLevel();
        }

        // actualiza la bala del jugador
        if(bullet.getStatus()){
            bullet.update(fps);
        }

        // actualiza las balas de los marcianos que se ven
        for(int i = 0; i < invadersBullets.length; i++){
            if(invadersBullets[i].getStatus()) {
                invadersBullets[i].update(fps);
            }
        }

        // ha tocado la bala del jugaador el tope de la pantalla?
        if(bullet.getImpactPointY() < 0){
            bullet.setInactive();
        }

        // han tocado las balas de los marcianos el fondo de la pantalla?
        for(int i = 0; i < invadersBullets.length; i++){
            if(invadersBullets[i].getImpactPointY() > screenY){
                invadersBullets[i].setInactive();
            }
        }

        // ha dado la bala en un enemigo?
        if(bullet.getStatus()) {
            for (int i = 0; i < numInvaders; i++) {
                if (invaders[i].getVisibility()) {
                    if (RectF.intersects(bullet.getRect(), invaders[i].getRect())) {
                        invaders[i].setInvisible();
                        soundPool.play(invaderExplodeID, 1, 1, 0, 0, 1);
                        bullet.setInactive();
                        score = score + 10;

                        // Has the player won
                        if(score == numInvaders * 10){
                            paused = true;
                            score = 0;
                            lives = 3;
                            prepareLevel();
                        }
                    }
                }
            }
        }

        // ha roto una bala enemiga el refugio

        for(int i = 0; i < invadersBullets.length; i++){
            if(invadersBullets[i].getStatus()){
                for(int j = 0; j < numBricks; j++){
                    if(bricks[j].getVisibility()){
                        if(RectF.intersects(invadersBullets[i].getRect(), bricks[j].getRect())){
                            // A collision has occurred
                            invadersBullets[i].setInactive();
                            bricks[j].setInvisible();
                            soundPool.play(damageShelterID, 1, 1, 0, 0, 1);
                        }
                    }
                }
            }

        }

        // ha roto una bala del jugador el refugio

        if(bullet.getStatus()){
            for(int i = 0; i < numBricks; i++){
                if(bricks[i].getVisibility()){
                    if(RectF.intersects(bullet.getRect(), bricks[i].getRect())){
                        // A collision has occurred
                        bullet.setInactive();
                        bricks[i].setInvisible();
                        soundPool.play(damageShelterID, 1, 1, 0, 0, 1);
                    }
                }
            }
        }

        // ha dado una bala enemiga a la nave del jugador?
        for(int i = 0; i < invadersBullets.length; i++){
            if(invadersBullets[i].getStatus()){
                if(RectF.intersects(playerShip.getRect(), invadersBullets[i].getRect())){
                    invadersBullets[i].setInactive();
                    lives --;
                    soundPool.play(playerExplodeID, 1, 1, 0, 0, 1);

                    // Is it game over?
                    if(lives == 0){
                        paused = true;
                        lives = 3;
                        score = 0;
                        prepareLevel();

                    }
                }
            }
        }

    }

    private void draw(){

        if (ourHolder.getSurface().isValid()) {

            canvas = ourHolder.lockCanvas();
            //Imagen como fondo
            fondo= context.getResources().getDrawable(R.drawable.espacio);
            fondo.setBounds(0,0,fondo.getIntrinsicWidth(),fondo.getIntrinsicHeight());
            // color del fondo
            canvas.drawColor(Color.argb(255, 25, 25, 112));
            fondo.draw(canvas);

            // paleta de color para pintar
            paint.setColor(Color.argb(255,  255, 255, 255));

            // dibuja la nave
            canvas.drawBitmap(playerShip.getBitmap(), playerShip.getX(), screenY - 50, paint);

            // dibuja los marcianos
            for(int i = 0; i < numInvaders; i++){
                if(invaders[i].getVisibility()) {
                    if(uhOrOh) {
                        canvas.drawBitmap(invaders[i].getBitmap(), invaders[i].getX(), invaders[i].getY(), paint);
                    }else{
                        canvas.drawBitmap(invaders[i].getBitmap2(), invaders[i].getX(), invaders[i].getY(), paint);
                    }
                }
            }

            // dibuja los bloques del refugio que se ven

            for(int i = 0; i < numBricks; i++){
                if(bricks[i].getVisibility()) {
                    canvas.drawRect(bricks[i].getRect(), paint);
                }
            }

            // dibuja las balas del jugador si se activan
            if(bullet.getStatus()){
                canvas.drawRect(bullet.getRect(), paint);
            }

            // dibuja las balas de los marcianos

            // actualiza las balas de los enemigos si se ven
            for(int i = 0; i < invadersBullets.length; i++){
                if(invadersBullets[i].getStatus()) {
                    canvas.drawRect(invadersBullets[i].getRect(), paint);
                }
            }

            // dibuja el score y las vidas que quedan
            // cambia la paleta de colores
            paint.setColor(Color.argb(255,4,253,4));
            paint.setTextSize(44);
            canvas.drawText("Score: " + score + "   Lives: " + lives, 10,50, paint);

            // dibuja todo en pantalla
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    // pausa
    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }

    }

    // comienza
    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }


    // detectar toques en la pantalla.
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

            // jugador toca la pantalla
            case MotionEvent.ACTION_DOWN:

                paused = false;

                if(motionEvent.getY() > screenY - screenY / 8) {
                    if (motionEvent.getX() > screenX / 2) {
                        playerShip.setMovementState(playerShip.RIGHT);
                    } else {
                        playerShip.setMovementState(playerShip.LEFT);
                    }

                }

                if(motionEvent.getY() < screenY - screenY / 8) {
                    // dispara
                    if(bullet.shoot(playerShip.getX()+
                            playerShip.getLength()/2,screenY,bullet.UP)){
                        soundPool.play(shootID, 1, 1, 0, 0, 1);
                    }
                }
                break;

            // jugador retira dedo de pantalla
            case MotionEvent.ACTION_UP:

                if(motionEvent.getY() > screenY - screenY / 10) {
                    playerShip.setMovementState(playerShip.STOPPED);
                }

                break;

        }

        return true;
    }
}
