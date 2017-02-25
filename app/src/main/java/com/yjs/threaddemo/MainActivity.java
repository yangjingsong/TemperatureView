package com.yjs.threaddemo;

import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private String TAG = "ThreadDemo";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final PasswordEditText passwordEditText = (PasswordEditText) findViewById(R.id.password);

        passwordEditText.setVisibility(View.VISIBLE);
       passwordEditText.setOnPasswordFilledListener(new PasswordEditText.OnPasswordFilledListener() {
           @Override
           public void onPasswordFilled(String password) {
               Toast.makeText(MainActivity.this,password,Toast.LENGTH_LONG).show();
               if(!password.equals("123456")){
                   passwordEditText.clearPassword();
               }
           }
       });

        TemperatureView temperatureView = (TemperatureView) findViewById(R.id.temp);
        temperatureView.setCurrentTemp(21);



    }





}
