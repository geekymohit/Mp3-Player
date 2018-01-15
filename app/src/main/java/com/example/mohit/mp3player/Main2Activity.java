package com.example.mohit.mp3player;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {

    static MediaPlayer mediaPlayer;
    ArrayList<File> mySongs;
    SeekBar seekBar;
    int position;
    Thread UpdateSeek;
    Uri uri;
    Button btplay ,btFF , btNxt , btPP, btPrev;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        btplay = (Button)findViewById(R.id.btplay);
        btFF= (Button)findViewById(R.id.btFF);
        btNxt = (Button)findViewById(R.id.btNxt);
        btPP = (Button)findViewById(R.id.btPP);
        btPrev = (Button)findViewById(R.id.btPrev);
        btplay.setOnClickListener(this);
        btFF.setOnClickListener(this);
        btNxt.setOnClickListener(this);
        btPP.setOnClickListener(this);
        btPrev.setOnClickListener(this);

        UpdateSeek = new Thread(){
            @Override
            public void run() {
                 int totalDuration = mediaPlayer.getDuration();
                int currentPosition = 0;
                seekBar.setMax(totalDuration);
                while (currentPosition < totalDuration)
                {
                    try {

                        sleep(500);
                        currentPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                  super.run();
            }
        };
        if(mediaPlayer !=null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        Intent intent =getIntent();
        Bundle bundle =intent.getExtras();
        mySongs = (ArrayList) bundle.getParcelableArrayList("songs");
        position = bundle.getInt("position",0);

        uri = Uri.parse(mySongs.get(position).toString());
        mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());
        UpdateSeek.start();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                    mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id =v.getId();
        switch (id)
        {
            case R.id.btplay :
                if(mediaPlayer.isPlaying())
                {
                    btplay.setText("Play");
                    mediaPlayer.pause();
                }
                else {
                    btplay.setText("Pause");
                    mediaPlayer.start();
                }
                break;
            case R.id.btFF :
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 5000);
                break;
            case R.id.btNxt :
                mediaPlayer.stop();
                mediaPlayer.release();
                position = (position+1)%mySongs.size();
                uri = Uri.parse(mySongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                break;
            case R.id.btPP:
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 5000);
                break;
            case R.id.btPrev:
                mediaPlayer.stop();
                mediaPlayer.release();
                position = (position-1 < 0)?mySongs.size()-1 : position-1;
                uri = Uri.parse(mySongs.get(position ).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                break;

        }
    }
}
