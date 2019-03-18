package com.example.nveob.myapplication.Game;

import org.junit.Test;

import static org.junit.Assert.*;

public class BulletTest {

    @Test
    public void changeDirectionTest() {
        Bullet bullet = new Bullet(1);
        bullet.setHeading(1);
        bullet.changeDirection();
        assertEquals(0,bullet.getHeading());
    }
}