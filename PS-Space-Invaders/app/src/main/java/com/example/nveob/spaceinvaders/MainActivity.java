package com.example.nveob.spaceinvaders;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.nveob.spaceinvaders.Activity.esMayor;
import com.example.nveob.spaceinvaders.Activity.esMenor;

public class MainActivity extends AppCompatActivity {
    Button buttonSi;
    Button buttonNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonSi =(Button)findViewById(R.id.esmayor);
        buttonSi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), esMayor.class);
                startActivity(intent);
            }
        });

        buttonNo = (Button)findViewById(R.id.esmenor);
        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), esMenor.class);
                startActivity(intent);
            }
        });
    }

    public void salirJuego(View v){
        finish();
        System.exit(0);
    }
}
