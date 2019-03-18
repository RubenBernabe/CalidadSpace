package com.example.nveob.myapplication.Activity;

import android.graphics.Point;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;

import com.example.nveob.myapplication.Game.SpaceInvadersView;
import com.example.nveob.myapplication.R;

public class GameViewActivity extends AppCompatActivity {

    // spaceInvadersView será la visualización del juego
    // También tendrá la lógica del juego
    // y responderá a los toques a la pantalla
    SpaceInvadersView spaceInvadersSpaceInvadersView;
    MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Obtener un objeto de Display para accesar a los detalles de la pantalla
        Display display = getWindowManager().getDefaultDisplay();
        // Cargar la resolución a un objeto de Point
        Point size = new Point();
        display.getSize(size);

        // Inicializar gameView y establecerlo como la visualización
        spaceInvadersSpaceInvadersView = new SpaceInvadersView(this, size.x, size.y, this, true);
        setContentView(spaceInvadersSpaceInvadersView);
        mediaPlayer = MediaPlayer.create(this, R.raw.invaders_music);

    }

    // Este método se ejecuta cuando el jugador empieza el juego
    @Override
    protected void onResume() {
        super.onResume();

        // Le dice al método de reanudar del gameView que se ejecute
        spaceInvadersSpaceInvadersView.resume();
        mediaPlayer.start();
    }

    // Este método se ejecuta cuando el jugador se sale del juego
    @Override
    protected void onPause() {
        super.onPause();

        // Le dice al método de pausa del gameView que se ejecute
        spaceInvadersSpaceInvadersView.pause();
        mediaPlayer.pause();
    }
}
