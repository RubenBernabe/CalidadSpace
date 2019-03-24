package com.example.nveob.myapplication.SpaceInvaderView;

import android.app.Activity;
import android.content.Context;
import com.example.nveob.myapplication.game.SpaceInvadersView;


import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class ExtraInvaderTest {

    @Test
    public void extraInvader(){
        Context context = mock(Context.class);
        Activity activity = mock(Activity.class);
        SpaceInvadersView spaceInvadersView = new SpaceInvadersView(context,5,5,activity,true);
        Assert.assertNull(spaceInvadersView.getInvaderExtra());
        spaceInvadersView.runInvaderExtra();
        Assert.assertNotNull(spaceInvadersView.getInvaderExtra());
    }
}
