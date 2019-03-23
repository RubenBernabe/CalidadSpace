package com.example.nveob.myapplication.game;

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
import com.example.nveob.myapplication.activity.youWon;

import java.security.SecureRandom;

public class SpaceInvadersView extends SurfaceView implements Runnable {

    private static SecureRandom r = new SecureRandom();
    Context context;
    String points = "SCORE";
    int numInvaders = 0;
    int score = 0;
    Activity gameActivity;
    Boolean adult;
    Bitmap buttonShoot;
    private Thread gameThread = null;
    private SurfaceHolder ourHolder;
    private volatile boolean playing;
    private boolean paused = true;
    private Paint paint;
    private long fps;
    private int screenX;
    private int screenY;
    private PlayerShip playerShip;
    private int nextBulletPlayer = 0;
    private int maxPlayerBullet = 3;
    private int maxInvaders = 60;
    Invader[] invaders = new Invader[maxInvaders];
    private int nextBullete = maxPlayerBullet;
    private int maxInvaderBullet = 10;
    private int totalBullet = maxInvaderBullet + maxPlayerBullet;
    private Bullet[] invadersBullets = new Bullet[totalBullet];
    private InvaderExtra invaderExtra;
    private int xInicialEx = 0;
    private int yInicialEx = 0;
    private DefenceBrick[] defenceBricks = new DefenceBrick[400];
    private int numBricks;
    private Timer time = new Timer();

    public SpaceInvadersView(Context context, int x, int y, Activity gameActivity, Boolean adult) {

        super(context);

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

        if (adult) {
            buttonShoot = BitmapFactory.decodeResource(context.getResources(), R.drawable.disparo);
            buttonShoot = Bitmap.createScaledBitmap(buttonShoot,
                    (int) (screenX / 15),
                    (int) (screenY / 10),
                    false);
        }

        // creamos nave jugador
        playerShip = new PlayerShip(context, screenX, screenY);

        //preparar el cronómetro
        time = new Timer();

        // creamos marcianos
        numInvaders = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 3; j++) {
                invaders[numInvaders] = new Invader(context, j, i, screenY, screenX);
                numInvaders++;
            }
        }

        invaderExtra = new InvaderExtra(context, xInicialEx, yInicialEx, screenX, screenY);
        invaderExtra.setInvisible();

        // inicializamos array de balas de marcianos
        for (int i = 0; i < totalBullet; i++) {
            invadersBullets[i] = new Bullet(screenY);
        }

        // construimos los refugios
        numBricks = 0;
        for (int shelterNumber = 0; shelterNumber < 4; shelterNumber++) {
            for (int column = 0; column < 10; column++) {
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

            long startFrameTime = System.currentTimeMillis();
            this.time.start();

            if (!paused) {
                update();
            }

            draw();

            long timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }
        }
    }

    private void end() {
        final Activity activity = (Activity) getContext();
        Intent intent = new Intent(activity, youWon.class);
        intent.putExtra(points, score);
        activity.finish();
        activity.startActivity(intent);
        Thread.currentThread().interrupt();
    }

    private void runInvaders() {

        boolean bumped = false;
        int countInvaders = 0;
        int numBloque = -1;

        for (int i = 0; i < numInvaders; i++) {
            bumped = bumped || invaders[i].update(fps, screenX);

            // si dispara suma bala
            if (invaders[i].takeAim() && invadersBullets[nextBullete].shoot(invaders[i].getX() * 2,
                    invaders[i].getY() + invaders[i].getHeight() * 3, Bullet.DOWN)) {
                nextBullete++;
            }

            if (nextBullete == totalBullet) {
                nextBullete = maxPlayerBullet;
            }

            if (invaders[i].isVisible()) {
                countInvaders++;
                if (invaders[i].getY() >= screenY - playerShip.getLength()) {
                    end();
                }
            }

            if (invaders[i].isVisible()) {
                for (int j = 0; j < numBricks; j++) {
                    if (defenceBricks[j].getVisibility() && RectF.intersects(invaders[i].getRectf(), defenceBricks[j].getRect())) {
                        this.defenceBricks[j].setInvisible();
                        numBloque = this.defenceBricks[j].getNumShelter();
                    }
                    if (defenceBricks[j].getNumShelter() == numBloque) {
                        this.defenceBricks[j].setInvisible();
                    }
                }
                if (RectF.intersects(playerShip.getRect(), invaders[i].getRectf())) {
                    end();
                }
            }
        }

        if (bumped) {
            // mueve a los marcianos hacia abajo y cambia de posicion
            for (int i = 0; i < numInvaders; i++) {
                invaders[i].dropDownAndReverse();
            }
        }

        if (countInvaders == 0) {
            end();
        }
    }

    private void runInvaderExtra() {
        invaderExtra.update(fps, screenX);
        if (invaderExtra.getX() >= screenX) {
            invaderExtra = new InvaderExtra(context, xInicialEx, yInicialEx, screenX, screenY);
            invaderExtra.setInvisible();
        }

        if (time.getSegundos() >= 10) {
            invaderExtra.setVisible();
            time.reset();
        }
    }

    private boolean runInvadersBullet() {
        boolean imp = false;

        for (int i = 0; i < totalBullet; i++) {
            if (invadersBullets[i].isActivated()) {
                invadersBullets[i].update(fps);

                if (RectF.intersects(invadersBullets[i].getRectf(), playerShip.getRect())) {
                    end();
                }
                for (int j = 0; j < numInvaders; j++) {
                    if (invaders[j].isVisible() && RectF.intersects(invaders[j].getRectf(), invadersBullets[i].getRectf())) {
                        invaders[j].setInvisible();
                        invadersBullets[i].setInactive();
                        score = score + 100;
                        if (i < maxPlayerBullet) {
                            nextBulletPlayer--;
                            System.out.println(nextBulletPlayer + " resta");
                        }
                    }
                }
                if (invaderExtra.isVisible() && RectF.intersects(invaderExtra.getRectf(), invadersBullets[i].getRectf())) {
                    invaderExtra = new InvaderExtra(context, xInicialEx, yInicialEx, screenX, screenY);
                    invaderExtra.setInvisible();
                    invadersBullets[i].setInactive();
                    score = score + 100;
                    if (i < maxPlayerBullet) {
                        nextBulletPlayer--;
                    }
                }

            }

            if (invadersBullets[i].getImpactPointY() <= 0 && invadersBullets[i].isActivated()) {
                invadersBullets[i].changeDirection();
            }
            if (invadersBullets[i].getImpactPointY() >= screenY && invadersBullets[i].isActivated()) {
                invadersBullets[i].changeDirection();
            }


            if (invadersBullets[i].isActivated()) {
                for (int j = 0; j < numBricks; j++) {
                    if (RectF.intersects(defenceBricks[j].getRect(), invadersBullets[i].getRectf()) && defenceBricks[j].getVisibility()) {
                        imp = true;
                        defenceBricks[j].setInvisible();
                        invadersBullets[i].setInactive();
                        if (i < maxPlayerBullet) {
                            nextBulletPlayer--;
                        }
                    }
                }
            }
        }
        return imp;
    }

    private void update() {
        playerShip.update(fps);
        runInvaders();
        runInvaderExtra();
        boolean imp = runInvadersBullet();
        if (imp) {
            change();
        }

        int t = r.nextInt(150);
        //aparecer y desaparecer nave
        if (t == 2) {
            playerShip.desaparecer();
            playerShip.aparecer(screenX);
        }
    }

    private void draw() {
        Canvas canvas;
        // Asegurate de que la superficie del dibujo sea valida o tronamos
        if (ourHolder.getSurface().isValid()) {
            // Bloquea el lienzo para que este listo para dibujar
            canvas = ourHolder.lockCanvas();

            canvas.drawColor(Color.argb(255, 0, 0, 0));
            paint.setColor(Color.argb(255, 255, 255, 255));
            canvas.drawBitmap(playerShip.getBitmap(), playerShip.getX() + 50, playerShip.getY() - playerShip.getHeight(), paint);
            paint.setColor(Color.argb(255, 255, 255, 255));

            for (int i = 0; i < numInvaders; i++) {
                if (invaders[i].isVisible()) {
                    canvas.drawBitmap(invaders[i].getBitmap(), invaders[i].getX(), invaders[i].getY(), paint);
                }
            }
            if (invaderExtra.isVisible()) {
                canvas.drawBitmap(invaderExtra.getBitmap(), invaderExtra.getX(), invaderExtra.getY(), paint);
            }
            // ladrillos visibles a dibujar
            for (int i = 0; i < numBricks; i++) {
                if (defenceBricks[i].getVisibility()) {
                    canvas.drawRect(defenceBricks[i].getRect(), paint);
                }
            }
            if (adult) {
                canvas.drawBitmap(buttonShoot, 0, (screenY / 2) - 100, paint);
            }

            // actualiza las balas de los enemigos si se ven
            for (int i = 0; i < maxInvaderBullet; i++) {
                if (invadersBullets[i].isActivated()) {
                    canvas.drawRect(invadersBullets[i].getRectf(), paint);
                }
            }

            // dibuja el score y las vidas que quedan
            // cambia la paleta de colores
            paint.setColor(Color.argb(255, 255, 255, 255));
            paint.setTextSize(60);
            canvas.drawText("Score: " + score, 10, 50, paint);
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

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                paused = false;
                if (motionEvent.getY() <= screenY / 2 && (motionEvent.getX() <= screenX / 15 || motionEvent.getX() >= screenX - screenX / 15) &&
                        motionEvent.getY() >= screenY / 2 - screenY / 10 && adult && nextBulletPlayer < maxPlayerBullet) {
                    invadersBullets[nextBulletPlayer].shoot(playerShip.getX() + playerShip.getLength(),
                            playerShip.getY() - playerShip.getHeight() / 2, Bullet.UP);
                    nextBulletPlayer++;
                    System.out.println(nextBulletPlayer + " player");
                } else if ((motionEvent.getY() >= screenY / 2)&&(motionEvent.getX() <= (screenX / 3))) {
                    if (motionEvent.getX() <= (screenX / 3)) {
                        playerShip.setMovementState(playerShip.LEFT);
                    } else if (((screenX / 3) * 2) < motionEvent.getX()) {
                        playerShip.setMovementState(playerShip.RIGHT);
                    } else if (((screenX / 3) < motionEvent.getX()) && (motionEvent.getX() <= ((screenX / 3) * 2))) {
                        if (motionEvent.getY() <= (screenY * 3 / 4)) {
                            playerShip.setMovementState(playerShip.UP);
                        } else if (motionEvent.getY() > (screenY * 3 / 4)) {
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

    private void change() {
        playerShip.chanImage(context);
        for (int i = 0; i < numInvaders; i++) {
            invaders[i].changeImage(context);
        }
        invaderExtra.changeImage(context);
    }
}