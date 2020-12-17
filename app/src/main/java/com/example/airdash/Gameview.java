package com.example.airdash;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.SurfaceView;

import java.util.Random;

public class Gameview extends SurfaceView implements Runnable{

    private Thread thread;
    private Boolean isPlaying,isGameOver=false;
    private int screenX, screenY,score=0,sound;
    private float s=0.0f;
    public static float screenRatioX, screenRatioY;
    private Flight flight;
    private GameActivity activity;
    private Paint paint;
    private Random random;
    private Bird[] birds;
    private SharedPreferences prefs;
    private Background background1, background2;


    public Gameview(GameActivity activity, int screenX, int screenY) {
        super(activity);
        this .activity = activity;

        prefs =  activity.getSharedPreferences("game", Context.MODE_PRIVATE);


        this.screenX = screenX;
        this.screenY = screenY;
        screenRatioX = 2960f/screenX;
        screenRatioY = 1440f/screenY;
        background1 = new Background(screenX, screenY, getResources());
        background2 = new Background(screenX, screenY, getResources());

        flight = new Flight(screenY, getResources());
        background2.x=screenX;

        paint = new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.BLACK);
        birds= new Bird[3];
        for(int i=0;i<3; i++)
        {
             Bird bird = new Bird(getResources());
             birds[i] = bird;
        }

        random = new Random();
    }

    @Override
    public void run() {

        while(isPlaying)
        {
            draw();
            update();
            sleep();
        }

    }

    private void update()
    {

        background1.x-= 10*screenRatioX;
        background2.x-= 10*screenRatioX;

        if(background1.x + background1.background.getWidth() <=0)
        {
            background1.x = screenX;
        }
        if(background2.x + background2.background.getWidth() <=0)
        {
            background2.x = screenX;
        }
        if(flight.isGoingUp)
        {
            flight.y -=12 * screenRatioY;
            flight.x -=2*screenRatioX;
        }
        else
        {
            flight.y += 12 * screenRatioY;
            flight.x +=2*screenRatioX;
        }

        if(flight.y<0)
            flight.y=0;

        if(flight.y>screenY-flight.height-120)
            flight.y=screenY-flight.height-120;

        if(flight.x<0)
            flight.x=0;

        if(flight.x>screenX-flight.width)
            flight.x=screenX-flight.width;




        for(Bird bird : birds)
        {
            bird.x-= bird.speed;

            if(bird.x + bird.width <0)
            {
                int bound = (int) (30*screenRatioX);
                bird.speed=random.nextInt(bound);

                if(bird.speed < 10 *screenRatioX)
                {
                    bird.speed = (int) (10*screenRatioX);

                    bird.x=screenX;
                    bird.y = random.nextInt(screenY-bird.height-135);

                }

            }
            if(isGameOver == false)
            {
                s+=0.1;
                score= (int) s;
            }
            //if flight hits the bird
            if(Rect.intersects(bird.getCollisonShape(), flight.getCollisonShape()))
            {
                isGameOver = true;
                activity.gameover =true;
            }

        }

    }

    private void draw()
    {
        if(getHolder().getSurface().isValid())
        {
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);

            for(Bird bird: birds)
                canvas.drawBitmap(bird.getBird(), bird.x,bird.y,paint);
            canvas.drawText(score +" ", screenX/2f, 164, paint);
            if(isGameOver)
            {
                int xd=flight.x,yd=flight.y;
                //xd=xd+200;
                //yd=yd-20;
                isPlaying=false;
               canvas.drawBitmap(flight.getDead(),xd,yd,paint);
                getHolder().unlockCanvasAndPost(canvas);
               saveIfHighScore();
               waitBeforeExiting();

               return;
            }
            canvas.drawBitmap(flight.getFlight(),flight.x, flight.y, paint);
            getHolder().unlockCanvasAndPost(canvas);
        }


    }

    private void waitBeforeExiting()
    {
        try {
            Thread.sleep(3000);
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void saveIfHighScore()
    {
        if(prefs.getInt("highscore", 0) < score)
        {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("highscore", score);
            editor.apply();
        }
    }

    private void sleep()
    {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void resume()
    {
        isPlaying = true;
        thread = new Thread(this);
        thread.start();


    }

    public void pause()
    {
        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getX() < screenX) {
                    flight.isGoingUp = true;
                }
                break;

                case MotionEvent.ACTION_UP:
                    flight.isGoingUp = false;
                    break;

        }

        return true;
    }
}
