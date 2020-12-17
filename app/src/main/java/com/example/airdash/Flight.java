package com.example.airdash;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.airdash.Gameview.screenRatioX;
import static com.example.airdash.Gameview.screenRatioY;

public class Flight
{
    public boolean isGoingUp=false;
    int x,y,width,height,w,h,wingcounter=0;
    Bitmap flight1,flight2,dead;

    Flight(int screenY, Resources res)
    {
        flight1= BitmapFactory.decodeResource(res, R.drawable.h1);
        flight2 = BitmapFactory.decodeResource(res, R.drawable.h1);
        dead = BitmapFactory.decodeResource(res,R.drawable.h2);

        width= flight1.getWidth();
        height= flight1.getHeight();
        w=dead.getWidth();
        h=dead.getHeight();

        width/=8;
        height/=8;
        w/=8;
        h/=8;

        width *=(int)screenRatioX;
        height *= (int)screenRatioY;
        w*=(int)screenRatioX;
        h*= (int)screenRatioY;

        flight1 = Bitmap.createScaledBitmap(flight1,width,height,false);
        flight2 = Bitmap.createScaledBitmap(flight2,width,height,false);
        dead= Bitmap.createScaledBitmap(dead,w, h, false);
        y=screenY;
        x=(int)(64*screenRatioX);
    }

    Bitmap getFlight()
    {
      if(wingcounter==0)
      {
          wingcounter++;
          return flight1;
      }
      wingcounter--;

      return flight2;

    }

    Rect getCollisonShape()
    {
        return new Rect(x,y,x+width,y+height);
    }

    Bitmap getDead()
    {
        return dead;
    }



}

