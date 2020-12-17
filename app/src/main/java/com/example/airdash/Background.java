package com.example.airdash;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Background {

    int x,y;
    Bitmap background;

    Background (int screenX, int screenY, Resources res)
    {
        background = BitmapFactory.decodeResource(res, R.drawable.b);
        background = Bitmap.createScaledBitmap(background, screenX, screenY, false);

    }

}
