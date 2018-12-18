package com.hlub.dev.testmediaplayer;

import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TextView tvNameMusic;
    private TextView tvStart;
    private TextView tvStop;
    private SeekBar seebarMusic;
    private ImageView imgPrev;
    private ImageView imgPlay;
    private ImageView imgStop;
    private ImageView imgNext;

    private ArrayList<Song> songArrayList;
    int position = 0;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        anhxa();
        songArrayList = new ArrayList<>();
        addSong();
        //khởi tạo media player -> gán các giá trị file, title
        khoiTaoMedia();


        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //play: nghe nhac - tạm dừng
                if (mediaPlayer.isPlaying()) {
                    //đang phát nhạc : khi ấn nút ->pause
                    mediaPlayer.pause();
                    imgPlay.setImageResource(R.drawable.play);
                } else {
                    //k phát nhạc :khi ấn ->play
                    mediaPlayer.start();
                    imgPlay.setImageResource(R.drawable.pause);
                }

                setTimeTotal();
                updateTimeSong();
            }
        });

        imgStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dừng nhạc -> đổi hình imgplay-> khởi tạo nhạc tại vị trí vừa nghe
                mediaPlayer.stop();
                mediaPlayer.release();//giải phóng
                imgPlay.setImageResource(R.drawable.play);
                khoiTaoMedia();
                setTimeTotal();
            }
        });

        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextSong();
            }
        });
        imgPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position--;
                if (position < 0) {
                    position = songArrayList.size() - 1;
                }
                //đang phát -> ngừng lại (mở bài mới)
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                khoiTaoMedia();
                mediaPlayer.start();
                imgPlay.setImageResource(R.drawable.pause);
                setTimeTotal();
                updateTimeSong();
            }
        });
        seebarMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

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

    private void nextSong() {
        position++;
        if (position > songArrayList.size() - 1) {
            position = 0;
        }
        //đang phát -> ngừng lại (mở bài mới)
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        khoiTaoMedia();
        mediaPlayer.start();
        imgPlay.setImageResource(R.drawable.pause);
        setTimeTotal();
        updateTimeSong();
    }

    public void addSong() {
        songArrayList.add(new Song("Âm thầm bên em", R.raw.amthambenem));
        songArrayList.add(new Song("Đưa em đi khắp thế gian", R.raw.duaemdikhapthegian));
        songArrayList.add(new Song("Tình yêu màu nắng", R.raw.tinhyeumaunang));
        songArrayList.add(new Song("Tội cho cô gái đó", R.raw.toichocogaido));
    }

    public void khoiTaoMedia() {
        mediaPlayer = MediaPlayer.create(MainActivity.this, songArrayList.get(position).getFile());
        tvNameMusic.setText(songArrayList.get(position).getTitle());
    }

    public void setTimeTotal() {
        SimpleDateFormat format = new SimpleDateFormat("mm:ss");
        tvStop.setText(format.format(mediaPlayer.getDuration()));

        //gán max seebar=mediaPlayer.getDuration()
        seebarMusic.setMax(mediaPlayer.getDuration());
    }

    public void updateTimeSong(){
        final Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat format = new SimpleDateFormat("mm:ss");
                tvStart.setText(format.format(mediaPlayer.getCurrentPosition()));
                //update trên seebar
                seebarMusic.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this,500);
                if(mediaPlayer.getCurrentPosition()==mediaPlayer.getDuration()){
                    Log.e("123","positon");
                }
            }
        },1000);
    }
    public void anhxa() {
        tvNameMusic = (TextView) findViewById(R.id.tvNameMusic);
        tvStart = (TextView) findViewById(R.id.tvStart);
        tvStop = (TextView) findViewById(R.id.tvStop);
        seebarMusic = (SeekBar) findViewById(R.id.seebarMusic);
        imgPrev = (ImageView) findViewById(R.id.imgPrev);
        imgPlay = (ImageView) findViewById(R.id.imgPlay);
        imgStop = (ImageView) findViewById(R.id.imgStop);
        imgNext = (ImageView) findViewById(R.id.imgNext);
    }
}
