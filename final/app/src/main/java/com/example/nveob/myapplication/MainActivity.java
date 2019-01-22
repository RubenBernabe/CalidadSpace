package com.example.nveob.myapplication;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.nveob.myapplication.Activity.ChildGameViewActivity;
import com.example.nveob.myapplication.Activity.GameViewActivity;

public class MainActivity extends AppCompatActivity {

    private static Context context;
    EditText nameInput;
    Button buttonSi;
    Button buttonNo;
    MediaPlayer mediaPlayer = new MediaPlayer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.nameInput = (EditText) findViewById(R.id.nombreusuario);
        mediaPlayer = MediaPlayer.create(this, R.raw.invaders_music3);
        mediaPlayer.start();

        //opcion para adultos
        buttonSi = (Button)findViewById(R.id.esmayor);
        buttonSi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GameViewActivity.class);
                startActivity(intent);
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        });

        //opcion para niños
        buttonNo = (Button)findViewById(R.id.esmenor);
        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChildGameViewActivity.class);
                startActivity(intent);
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        });
    }

    /*public void setActivityBackgroundColor(int color) {
        android.view.View view = this.getWindow().getDecorView();
        view.setBackgroundColor(color);
    }*/

    public static Context getContext() {
        return context;

    }
    public void salirJuego(){
        finish();
        System.exit(0);
        mediaPlayer.stop();
    }
}
