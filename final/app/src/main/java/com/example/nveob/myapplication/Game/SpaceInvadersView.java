package com.example.nveob.myapplication.Game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.nveob.myapplication.R;
import com.example.nveob.myapplication.Activity.gameOver;
import com.example.nveob.myapplication.Activity.youWon;

import java.util.Random;


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

    // balas del nave
    private int nextBulletPlayer =0;
    private int maxPlayerBullet = 10;
    private Bullet[] PlayerBullets = new Bullet[maxPlayerBullet];


    private int maxInvaders=60;

    // balas de marcianos
    private int nextBullete =0;
    private int maxInvaderBullet = 3;
    private Bullet[] invadersBullets = new Bullet[maxInvaderBullet];
    private Bullet[] misilInvaderExtra = new Bullet[maxInvaderBullet];


    // hasta 60 Invaders
    Invader[] invaders = new Invader[maxInvaders];
    int numInvaders = 0;

    //Invader cada 10s y sus posicion inicial
    private Invader invaderExtra;
    private int xInicialEx=0;
    private int yInicialEx=0;

    // los escudos de la nave(por implemetnar, pero el codigo esta hecho)
    private DefenceBrick[] defenceBricks = new DefenceBrick[400];
    private int numBricks;

    // puntuación
    int score = 0;
    
    Activity gameActivity;

    Boolean adult;

    private boolean win = true;

    //Cronómetro
    private Timer time = new Timer();

    //botón disparar
    Bitmap buttonShoot;

    // cuando se inicializa el gameview
    // This special constructor method runs
    public SpaceInvadersView(Context context, int x, int y, Activity gameActivity, Boolean adult) {

        super(context);

        win = false;

        // crea copia del contexto para usar por otros metodos
        this.context = context;

        // Initialize ourHolder and paint objects
        ourHolder = getHolder();
        paint = new Paint();

        screenX = x;
        screenY = y;

        this.gameActivity = gameActivity;

        this.adult = adult;

        prepareLevel();
    }

    private void prepareLevel() {

        // inicializamos los objetos

        if(adult==true){
            buttonShoot = BitmapFactory.decodeResource(context.getResources(), R.drawable.disparo);
            buttonShoot = Bitmap.createScaledBitmap(buttonShoot,
                    (int) (screenX/15),
                    (int) (screenY/10),
                    false);

        }

        // creamos nave jugador
        playerShip = new PlayerShip(context, screenX, screenY);

        // preparamos balas jugador
        for (int i = 0; i < maxPlayerBullet; i++) {
            PlayerBullets[i]=new Bullet(screenY);
        }


        //preparar el cronómetro
        time=new Timer();


        // creamos marcianos
        numInvaders = 0;
        for (int i = 0; i < 6 ; i++) {
            for (int j = 0; j < 3 ; j++) {
                invaders[numInvaders] = new Invader(context,j,i,screenY,screenX);
                numInvaders++;
            }
        }


        invaderExtra=new Invader(context,xInicialEx,yInicialEx,screenX,screenY,true);
        invaderExtra.setInvisible();


        // inicializamos array de balas de marcianos
        for (int i = 0; i < maxInvaderBullet; i++) {
            invadersBullets[i]=new Bullet(screenY);
        }

        // construimos los refugios
        numBricks = 0;
        for(int shelterNumber = 0; shelterNumber < 4; shelterNumber++){
            for(int column = 0; column < 10; column ++ ) {
                for (int row = 0; row < 5; row++) {
                    defenceBricks[numBricks] = new DefenceBrick(row, column, shelterNumber, screenX, screenY);
                    numBricks++;
                }
            }
        }
        time.start();
    }

    @Override
public void run() {
    while (playing) {

        // capturamos el tiempo en milisegundos
        long startFrameTime = System.currentTimeMillis();

        // actualizamos el frame
        if (!paused) {
            update();
            this.time.run();
        }

        // actualizamos el frame
        draw();

        // calculamos los fps
        // para que nos sirva para animaciones de tiempo y demas.
        timeThisFrame = System.currentTimeMillis() - startFrameTime;
        if (timeThisFrame >= 1) {
            fps = 1000 / timeThisFrame;
        }

    }
}

    private void update() {

        // un marciano volvio al tocar la pantalla
        boolean bumped = false;

        // ha perdido el jugador
        boolean lost = false;

        // mover la nave
        playerShip.update(fps);

        // actualizar lo marcianos si se ven
        for (int i = 0; i <numInvaders ; i++) {
            if(invaders[i].isVisible()){
                // mover el sig marciano
                invaders[i].update(fps,screenX);
                if(invaders[i].getX()==0 || invaders[i].getX() + invaders[i].getHeight()==screenX){
                    bumped=true;
                }
            }
            // quiere disparar?
            if(invaders[i].takeAim() && this.adult){
                // lo intenta y dispara
                if(invadersBullets[nextBullete].shoot(invaders[i].getX()*2,
                        invaders[i].getY() + invaders[i].getHeight()*3,Bullet.DOWN)){
                    // vuelve al principio si ha llegado al ultimo
                    if(nextBullete == maxInvaderBullet-1){
                        // no dispara otra bala hasta que no termina el viaje de una.
                        nextBullete=0;
                    }else{
                        // ha dispardo
                        // prepara para el sig disparo
                        nextBullete++;
                    }
                }


            }

            if (invaders[i].getX() > screenX - invaders[i].getlength()
                    || invaders[i].getX() < 0) {

                bumped = true;
            }
        }

        invaderExtra.update(fps,screenX);
        if(invaderExtra.getX()>=screenX){
            invaderExtra=new Invader(context,xInicialEx,yInicialEx,screenX,screenY,true);
            invaderExtra.setInvisible();
        }
        //aparicion invaders extra
        if(time.getSegundos()==10){
            invaderExtra.setVisible();
            time.reset();
        }

        if(lost){
            final Activity activity = (Activity)getContext();
            Intent intent = new Intent(activity, gameOver.class);
            intent.putExtra("SCORE", score);
            activity.finish();
            activity.startActivity(intent);
            Thread.currentThread().interrupt();
        }

        for(int i=0; i < maxPlayerBullet; i++){
            if(PlayerBullets[i].isActivated()){
                PlayerBullets[i].update(fps);
            }
        }

        int countInvaders=0;

        for (int i = 0; i <numInvaders ; i++) {
            if(invaders[i].isVisible()){
                countInvaders++;
                if(invaders[i].getY() >= screenY - playerShip.getLength()){
                    final Activity activity = (Activity)getContext();
                    Intent intent = new Intent(activity, youWon.class);
                    intent.putExtra("SCORE", score);
                    activity.finish();
                    activity.startActivity(intent);
                    Thread.currentThread().interrupt();
                }
            }
        }

        if(countInvaders==0){
            win=true;
            final Activity activity = (Activity)getContext();
            Intent intent = new Intent(activity, youWon.class);
            intent.putExtra("SCORE", score);
            activity.finish();
            activity.startActivity(intent);
            Thread.currentThread().interrupt();
        }

        for(int i = 0; i < maxInvaderBullet; i++){
            if(invadersBullets[i].isActivated()) {
                invadersBullets[i].update(fps);
            }
        }
        // si un marciano ha dado l avuelta en el lado de la pantalla
        if (bumped) {
            // mueve a los marcianos hacia abajo y cambia de posicion
            for (int i = 0; i <numInvaders ; i++) {
                invaders[i].dropDownAndReverse();
                // Have the invaders landed
                if(invaders[i].getY() > playerShip.getY()){
                    lost = true;
                }
            }
        }


        //las balas han llagado al tope?
        for(int i = 0; i < maxPlayerBullet; i++) {
            if (PlayerBullets[i].isActivated() && PlayerBullets[i].getImpactPointY() <= 0) {
                PlayerBullets[i].changeDirection();
            }
        }
        for (int i = 0; i <maxInvaderBullet ; i++) {
            if(invadersBullets[i].getImpactPointY() <= 0 && invadersBullets[i].isActivated()){
                invadersBullets[i].changeDirection();
            }
        }

        //las balas han llegado al final
        for(int i = 0; i < maxPlayerBullet; i++) {
            if (PlayerBullets[i].isActivated() && PlayerBullets[i].getImpactPointY() >= screenY) {
                PlayerBullets[i].changeDirection();
            }
        }
        for (int i = 0; i <maxInvaderBullet ; i++) {
            if(invadersBullets[i].getImpactPointY()>= screenY && invadersBullets[i].isActivated()){
                invadersBullets[i].changeDirection();
            }
        }

        for (int i = 0; i < maxPlayerBullet ; i++) {
            if (PlayerBullets[i].isActivated()) {
                for (int j = 0; j < numInvaders; j++) {
                    if (invaders[j].isVisible() && RectF.intersects(invaders[j].getRectf(), PlayerBullets[i].getRectf())) {
                        invaders[j].setInvisible();
                        PlayerBullets[i].setInactive();
                        score = score + 100;
                    }
                }
            }
        }
        for (int i = 0; i < maxInvaderBullet ; i++) {
            for (int j = 0; j < numInvaders; j++) {
                if (invaders[j].isVisible() && RectF.intersects(invaders[j].getRectf(), invadersBullets[i].getRectf())) {
                    invaders[j].setInvisible();
                    invadersBullets[i].setInactive();
                    score = score + 100;
                }
            }
        }

        for (int i = 0; i < maxPlayerBullet ; i++) {
            if (PlayerBullets[i].isActivated()) {
                if (invaderExtra.isVisible() && RectF.intersects(invaderExtra.getRectf(), PlayerBullets[i].getRectf())) {
                    invaderExtra = new Invader(context, xInicialEx, yInicialEx, screenX, screenY, true);
                    invaderExtra.setInvisible();
                    PlayerBullets[i].setInactive();
                    score = score + 100;
                }
            }
        }
        for (int i = 0; i < maxInvaderBullet ; i++) {
            if (invadersBullets[i].isActivated()) {
                if (invaderExtra.isVisible() && RectF.intersects(invaderExtra.getRectf(), invadersBullets[i].getRectf())) {
                    invaderExtra = new Invader(context, xInicialEx, yInicialEx, screenX, screenY, true);
                    invaderExtra.setInvisible();
                    invadersBullets[i].setInactive();
                    score = score + 100;
                }
            }
        }



        boolean impactoDoble = false;
        boolean impacto=false;
        for (int i = 0; i <maxInvaderBullet ; i++) {
            if(invadersBullets[i].isActivated()){
                for (int j = 0; j <numBricks ; j++) {
                    if(RectF.intersects(defenceBricks[j].getRect(), invadersBullets[i].getRectf()) && defenceBricks[j].getVisibility()){
                        impactoDoble = true;
                        impacto = true;
                        defenceBricks[j].setInvisible();
                        invadersBullets[i].setInactive();
                    }
                }
            }
        }

        impactoDoble=false;
        for (int i = 0; i < maxPlayerBullet ; i++) {
            if (PlayerBullets[i].isActivated()) {
                for (int j = 0; j < numBricks; j++) {
                    if (defenceBricks[j].getVisibility() && RectF.intersects(defenceBricks[j].getRect(), PlayerBullets[i].getRectf())) {
                        impactoDoble = impactoDoble && true;
                        impacto = !impactoDoble;
                        PlayerBullets[i].setInactive();
                    }
                }
            }
        }


        if(impactoDoble){
            changeRamdon();
        }else if(impacto){
            change();
        }

        for (int i = 0; i < maxInvaderBullet ; i++) {
            if(invadersBullets[i].isActivated() && RectF.intersects(invadersBullets[i].getRectf(), playerShip.getRect())){
                final Activity activity = (Activity)getContext();
                Intent intent = new Intent(activity, gameOver.class);
                intent.putExtra("SCORE", score);
                activity.finish();
                activity.startActivity(intent);
                Thread.currentThread().interrupt();
            }
        }
        for (int i = 0; i < maxPlayerBullet ; i++) {
            if(PlayerBullets[i].isActivated() && RectF.intersects(PlayerBullets[i].getRectf(), playerShip.getRect())){
                final Activity activity = (Activity)getContext();
                Intent intent = new Intent(activity, gameOver.class);
                intent.putExtra("SCORE", score);
                activity.finish();
                activity.startActivity(intent);
                Thread.currentThread().interrupt();
            }
        }
        for(int i = 0; i<numInvaders;i++) {
            if (invaders[i].isVisible()) {

            }
        }

        int numBloque = - 1;
        for (int i = 0; i <numInvaders ; i++) {
            if(invaders[i].isVisible()){
                for (int j = 0; j <numBricks ; j++) {
                    if(defenceBricks[j].getVisibility() && RectF.intersects(invaders[i].getRectf(), defenceBricks[j].getRect())){
                        this.defenceBricks[j].setInvisible();
                        numBloque=this.defenceBricks[j].getNumShelter();
                    }
                    if(defenceBricks[j].getNumShelter() == numBloque){
                        this.defenceBricks[j].setInvisible();
                    }
                }
                if (RectF.intersects(playerShip.getRect(),invaders[i].getRectf())) {
                    final Activity activity = (Activity)getContext();
                    Intent intent = new Intent(activity, gameOver.class);
                    intent.putExtra("SCORE", score);
                    activity.finish();
                    activity.startActivity(intent);
                    Thread.currentThread().interrupt();
                }
            }
        }

        Random r = new Random();
        int t = r.nextInt(100);


        //aparecer y desaparecer nave
        if (t==2){
            System.out.println(t);
            playerShip.desaparecer();
            playerShip.aparecer(screenX);
        }

    }

    private void draw() {

        // Asegurate de que la superficie del dibujo sea valida o tronamos
        if (ourHolder.getSurface().isValid()) {
            // Bloquea el lienzo para que este listo para dibujar
            canvas = ourHolder.lockCanvas();

            // color fondo
            canvas.drawColor(Color.argb(255, 0, 0, 0));

            // color brocha
            paint.setColor(Color.argb(255, 255, 255, 255));

            // nave espacial dibujar
            canvas.drawBitmap(playerShip.getBitmap(), playerShip.getX()+50, playerShip.getY() - playerShip.getHeight(), paint);

            // color brocha
            paint.setColor(Color.argb(255, 255, 255, 255));

            // dibuja los marcianos
            for(int i = 0; i < numInvaders; i++){
                if(invaders[i].isVisible()) {
                    canvas.drawBitmap(invaders[i].getBitmap(), invaders[i].getX(), invaders[i].getY(), paint);
                }
            }
            if(invaderExtra.isVisible()) {
                canvas.drawBitmap(invaderExtra.getBitmap(), invaderExtra.getX(), invaderExtra.getY(), paint);
            }
            // ladrillos visibles a dibujar
            for(int i = 0; i < numBricks; i++){
                if(defenceBricks[i].getVisibility()) {
                    canvas.drawRect(defenceBricks[i].getRect(), paint);
                }
            }
            if(adult){
                canvas.drawBitmap(buttonShoot,0,(screenY/2)-100 , paint);
               // canvas.drawBitmap(buttonShoot2,screenX - screenX/15,(screenY/2)- 100, paint);
            }

            // balas activas de la nave a dibujar
            for (int i=0; i < maxPlayerBullet; i++ ) {
                if (PlayerBullets[i].isActivated()) {
                    RectF rect = PlayerBullets[i].getRectf();
                    rect.right += 45;
                    rect.left += 45;
                    canvas.drawRect(rect, paint);
                }
            }

            // dibuja las balas de los marcianos

            // actualiza las balas de los enemigos si se ven
            for (int i = 0; i <maxInvaderBullet ; i++) {
                if(invadersBullets[i].isActivated()){
                    canvas.drawRect(invadersBullets[i].getRectf(),paint);
                }
            }

            // dibuja el score y las vidas que quedan
            // cambia la paleta de colores
            paint.setColor(Color.argb(255, 255, 255, 255));
            paint.setTextSize(60);
            canvas.drawText("Score: " + score, 10, 50, paint);

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
                if(motionEvent.getY()<=screenY/2 && (motionEvent.getX()<=screenX/15 || motionEvent.getX()>=screenX-screenX/15) &&
                        motionEvent.getY()>=screenY/2 - screenY/10 ) {
                    if(adult){

                        if(PlayerBullets[nextBulletPlayer].shoot(playerShip.getX()+ playerShip.getLength(),
                                playerShip.getY()- playerShip.getHeight()/2,Bullet.UP)){

                            if(nextBulletPlayer == maxPlayerBullet-1){
                                nextBulletPlayer=0;
                            }else{
                                nextBulletPlayer++;
                            }
                        }
                    }
                }else if(motionEvent.getY()>=screenY/2){
                    if (motionEvent.getX() <= (screenX / 3)) {
                        playerShip.setMovementState(playerShip.LEFT);
                    } else if (((screenX / 3) * 2) < motionEvent.getX()) {
                        playerShip.setMovementState(playerShip.RIGHT);
                    } else if (((screenX/3)<motionEvent.getX())&& (motionEvent.getX()<=((screenX/3)*2))){
                        if (motionEvent.getY()<= (screenY*3/4)){
                            playerShip.setMovementState(playerShip.UP);
                        }else if (motionEvent.getY()>(screenY*3/4)){
                            playerShip.setMovementState(playerShip.DOWN);
                        }
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                playerShip.setMovementState(playerShip.STOPPED);
                break;
        }

        return true;
    }

    private void change(){
        playerShip.chanImage(context);
        for (int i = 0; i <numInvaders; i++) {
            invaders[i].changeImage(context);
        }
        invaderExtra.changeImage(context);
    }

    private void changeRamdon(){
        playerShip.changeImageRandom(context);
        for (int i = 0; i <numInvaders ; i++) {
            invaders[i].changeImageRamdom(context);
        }
        invaderExtra.changeImageRamdom(context);
    }

}