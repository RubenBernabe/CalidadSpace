package com.example.nveob.spaceinvaders.Activity;

import android.app.Activity;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;

import com.example.nveob.spaceinvaders.Game.SpaceInvadersView;

public class esMayor extends Activity {

    // sera la vista del juego y respondera a cciones
    SpaceInvadersView spaceInvadersView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // display para acceder a las opciones de pantalla
        Display display = getWindowManager().getDefaultDisplay();
        // carga la resolucion en un objeto
        Point size = new Point();
        display.getSize(size);

        // inicializa gameview
        spaceInvadersView = new SpaceInvadersView(this, size.x, size.y);
        setContentView(spaceInvadersView);

    }

    // este metodo se ejecuta cuando se inicia el juego
    @Override
    protected void onResume() {
        super.onResume();

        // le dice al gameview que s eha vuelto al juego
        spaceInvadersView.resume();
    }

    // este metodo se ejecuta cuando se deja de jugar
    @Override
    protected void onPause() {
        super.onPause();

        // le dice al gameview que pause
        spaceInvadersView.pause();
    }
}
