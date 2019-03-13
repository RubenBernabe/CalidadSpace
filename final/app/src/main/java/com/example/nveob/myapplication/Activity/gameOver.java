package com.example.nveob.myapplication.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.nveob.myapplication.MainActivity;
import com.example.nveob.myapplication.R;

public class gameOver extends AppCompatActivity {
    MediaPlayer mediaPlayer = new MediaPlayer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        mediaPlayer = MediaPlayer.create(this, R.raw.fleeting_dream);

        Button salir;
        Button reiniciar;
        Button scoreboard;
        TextView scoreLabel = (TextView)findViewById(R.id.scoreLabel);
        TextView highScoreLabel = (TextView)findViewById(R.id.highScoreLabel);

        int score = getIntent().getIntExtra("SCORE", 0);
        scoreLabel.setText(score + "");

        SharedPreferences settings = getSharedPreferences("GAME_DATA",Context.MODE_PRIVATE);

        int highScore = settings.getInt("HIGH_SCORE",0);
        if(score > highScore){
            highScoreLabel.setText("High Score: "+ score);

            //save
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("HIGH_SCORE", score);
            editor.commit();
        }else{
            highScoreLabel.setText("High Score :"+ highScore);
        }

        salir = (Button)findViewById(R.id.salirjuego);
        salir.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                mediaPlayer.pause();
                mediaPlayer.release();
            }
        });

        reiniciar = (Button)findViewById(R.id.reiniciarjuego);
        reiniciar.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),GameViewActivity.class);
                startActivity(intent);
                mediaPlayer.pause();
                mediaPlayer.release();
            }
        });

    }
}
