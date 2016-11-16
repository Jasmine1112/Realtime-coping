package com.example.jasmine.realtimecoping2;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Jasmine on 4/30/16.
 */
public class MyService extends IntentService {
    public MyService(){
        super("My_Worker_Thread");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Toast.makeText(this,"Service Started...",Toast.LENGTH_LONG).show();
        return super.onStartCommand(intent, flags,startId);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Toast.makeText(this,"Service Stopped...", Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onHandleIntent(Intent intent){
        synchronized (this){
            int count = 0;
            while (count < 10){
                try{
                    wait(1500);
                    count++;
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
