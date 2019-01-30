package com.akshanshgusain.androidservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button startService, stopService, mBind, mUnbind, mGetRandomNumber;
    private Intent serviceIntent;
    private TextView mRandomNumber;

    private MyService myService;
    private boolean isServiceBound;
    private ServiceConnection serviceConnection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("lolo", "MainActivity: "+ Thread.currentThread().getId());
        startService = findViewById(R.id.button_start_service);
        stopService = findViewById(R.id.button_stop_service);
        mBind = findViewById(R.id.button_bind_service);
        mUnbind = findViewById(R.id.button_unbind_service);
        mGetRandomNumber  = findViewById(R.id.button_get_random_number);
        mRandomNumber  = findViewById(R.id.textView_random);

        serviceIntent = new Intent(this, MyService.class);

        startService.setOnClickListener(this);
        stopService.setOnClickListener(this);
        mBind.setOnClickListener(this);
        mUnbind.setOnClickListener(this);
        mGetRandomNumber.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.button_start_service:
                startService(serviceIntent);
                break;
            case R.id.button_stop_service:
                stopService(serviceIntent);
                break;
            case R.id.button_bind_service:
                bindeService();
                break;
            case R.id.button_unbind_service:
                unBindService();
                break;
            case R.id.button_get_random_number:
                setRandomNUmber();
                break;

        }

    }

    private void setRandomNUmber() {
        if(isServiceBound)
        {
            mRandomNumber.setText("Random Number : "+ myService.getmRandomNumber());
        }
        else
        {
            mRandomNumber.setText("Service not Bound");
        }

    }

    private void unBindService() {
        if(isServiceBound)
        {
            unbindService(serviceConnection);
            isServiceBound=false;
        }
    }

    private void bindeService() {
        //we have to initilize the service connection
        if(serviceConnection==null)
        {
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    MyService.MyServiceBinder myServiceBinder = (MyService.MyServiceBinder)service;
                    myService = myServiceBinder.getService();//Initilizing the myService object;
                        isServiceBound=true;
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                        isServiceBound=false;
                }
            };

        }
        bindService(serviceIntent,serviceConnection,Context.BIND_AUTO_CREATE);
    }
}
