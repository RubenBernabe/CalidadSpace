package com.example.nveob.myapplication.Game;

import android.content.Context;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class InvaderTest {

    @Test
    public void horizontalMovementTest(){
        Context context = mock(Context.class);
        Invader invader = new Invader(context,5,5,5,5);
        invader.update(60,6);
        assertEquals(invader.getLEFT(),1);
        invader.update(60,5);
        assertEquals(invader.getRIGHT(),2);
    }
    @Test
    public void verticalMovementTest(){
        Context context = mock(Context.class);
        Invader invader = new Invader(context,5,5,500,500);
        float alturaInicial = invader.getY();
        invader.dropDownAndReverse();
        assertTrue(alturaInicial<invader.getY());
    }
}
