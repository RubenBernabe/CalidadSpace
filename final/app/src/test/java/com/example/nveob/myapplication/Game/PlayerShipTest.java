package com.example.nveob.myapplication.game;

import android.content.Context;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class PlayerShipTest {

    @Test
    public void update() {
        Context context = mock(Context.class);
        PlayerShip player = new PlayerShip(context,20,20);
        player.setShipMoving(4);
        player.setY(25);
        player.update(1000);
        assertEquals(25,player.getY(),0);

    }

   @Test
    public void update2(){
       Context context = mock(Context.class);
       PlayerShip player = new PlayerShip(context,20,20);
       player.setShipMoving(3);
       player.setY(-6);
       player.update(1000);
       assertEquals(-6,player.getY(),0);


   }
}