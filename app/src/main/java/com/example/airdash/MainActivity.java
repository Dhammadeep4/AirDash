package com.example.airdash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private boolean isMute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.bg);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,GameActivity.class));
            }
        });

        TextView highScoreTxt =  findViewById(R.id.highScoreTxt);
        final SharedPreferences prefs = getSharedPreferences("game", MODE_PRIVATE );
        highScoreTxt.setText("HighScore: " + prefs.getInt("highscore",0));
        isMute = prefs.getBoolean("isMute", false);

        final ImageView volumeCtrl = findViewById(R.id.voulme_on);
        if(isMute)
        {
            volumeCtrl.setImageResource(R.drawable.ic_volume_off_black_24dp);
        }
        else
        {
            volumeCtrl.setImageResource(R.drawable.ic_volume_up_black_24dp);
        }
        volumeCtrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMute = !isMute;
                if(isMute)
                {
                    volumeCtrl.setImageResource(R.drawable.ic_volume_off_black_24dp);
                    onPause();
                }
                else
                {
                    volumeCtrl.setImageResource(R.drawable.ic_volume_up_black_24dp);
                    onResume();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }
}
