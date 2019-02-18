package com.example.nveob.myapplication.Game;

import com.example.nveob.myapplication.Activity.GameViewActivity;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PlayerShipTest {
//ESTE NO FUNCIONA TODAVIA
    @Test
    public void updateTest() {
        /*PlayerShip ps = new PlayerShip();
        ps.setY(10);
        ps.setShipSpeed(200);
        ps.setMovementState(4);
        ps.update(5);
        assertEquals(50,ps.getY()+ps.getShipSpeed()/5);*/
        int input = 800;
        int expected = 398;
        double delta = 0.1;
        GameViewActivity gameViewActivity=mock(GameViewActivity.class);
        //SpaceInvadersView spaceInvadersView = new SpaceInvadersView(gameViewActivity,0,0,gameViewActivity,true);
        PlayerShip playerShip = mock(PlayerShip.class);
        playerShip.setX(400);
        playerShip.setMovementState(1);
        playerShip.update(100);

        assertEquals(expected,playerShip.getX(),delta);
    }
}