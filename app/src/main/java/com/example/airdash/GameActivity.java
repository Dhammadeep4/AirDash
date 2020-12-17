package com.example.airdash;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.WindowManager;

public class GameActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private Gameview gameview;
    public boolean gameover=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mediaPlayer = MediaPlayer.create(GameActivity.this,R.raw.m);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        gameview = new Gameview(this, point.x, point.y);
        setContentView(gameview);


    }

    @Override
    protected void onResume() {
        super.onResume();

        mediaPlayer.start();
        gameview.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mediaPlayer.pause();
        gameview.pause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }
}