package com.ahnsafety.ex64servicebindtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    MusicService musicService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    //액티비티가 화면에 보여질 때 자동으로 실행되는 라이프사이클 콜백 메소드
    @Override
    protected void onResume() {
        super.onResume();

        //서비스객체와 연결작업 수행
        if(musicService==null){
            Intent intent= new Intent(this,MusicService.class);
            startService(intent);// Service객체가 없다면 create를 하고 onStartCommand()메소드를 호출하며, 있다면 onStartComm뭉()만 실행함.
            //서비스객체의 참조값을 얻어오기 위해
            //서비스객체와 연결(bind)하는 메소드를 호출
            bindService(intent,conn,0); //flags:0 일때, bind
        }
    }

    //MusicService객체와 연결상태를 관리하는 객체 생성[터널 객체]
    ServiceConnection conn= new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //연결되었을 때 실행 되는 콜백메소드

            //연결된 터널을 통해 서비스객체의 참조값 얻어오기
            //서비스로 부터 넘어온 외교관객체(ibinder)에게 서비스객체의 참조값 얻어오기
            MusicService.ServiceBinder binder= (MusicService.ServiceBinder) iBinder;
            musicService =binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    public void clickStart(View view) {
        if (musicService != null) musicService.playMusic();
    }

    public void clickPause(View view) {
        if (musicService != null) musicService.pauseMusic();
    }

    public void clickStop(View view) {
        if (musicService != null) {
            musicService.stopMusic();
            unbindService(conn);
            musicService=null;
        }
        Intent intent=new Intent(this, MusicService.class);
        stopService(intent);
        finish();

    }
}


