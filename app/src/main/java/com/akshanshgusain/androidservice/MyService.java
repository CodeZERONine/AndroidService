package com.akshanshgusain.androidservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Random;

public class MyService extends Service {

    private int mRandomNumber;
    private boolean mIsRandomGeneratorOn;

    private final int MIN=0;
    private final int MAX=100;



    //You need to define a method "getService()" that returns a service reference
    public class MyServiceBinder extends Binder{
        public MyService getService()
        {
            return MyService.this;
        }
    }
    private IBinder mBinder = new MyServiceBinder() ;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRandomNumberGenerator();
        Log.d("lolo", "onDestroy: The Service is Destroyed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("lolo", "onStartCommand: "+ Thread.currentThread().getId());
//        stopSelf();
        mIsRandomGeneratorOn = true;
           new Thread(new Runnable() {
               @Override
               public void run() {
                      startRandomNumberGenerator();
               }
           }).start();
        return START_STICKY;
    }




    private void startRandomNumberGenerator()
    {
          while(mIsRandomGeneratorOn)
          {
              try {
                  Thread.sleep(1000);
                  mRandomNumber = new Random().nextInt(MAX)+MIN;
                  Log.d("lolo", "startRandomNumberGenerator: "+ Thread.currentThread().getId()+" Randome Number: "+ mRandomNumber);
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
          }
    }
    private void stopRandomNumberGenerator()
    {
          mIsRandomGeneratorOn=false;
    }


    public int getmRandomNumber()
    {
        return mRandomNumber;
    }
}
