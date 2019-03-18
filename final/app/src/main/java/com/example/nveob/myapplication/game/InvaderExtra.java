package com.example.nveob.myapplication.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.nveob.myapplication.R;

public class InvaderExtra extends Invader {

    public InvaderExtra(Context context, int x, int y, int screenY, int screenX) {
        super(context, x, y, screenY, screenX);
        this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.invader3);
        bitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (screenX/20),
                (int) (screenY/20),
                false);

    }

    @Override
    public boolean update(long fps, int screenX) {
         super.update(fps, screenX);;
        return true;
    }
}
