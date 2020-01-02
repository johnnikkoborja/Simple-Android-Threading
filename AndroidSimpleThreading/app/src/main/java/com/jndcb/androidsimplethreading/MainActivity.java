package com.jndcb.androidsimplethreading;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView Progress;
    int ProgressValue;
    Button Button1;
    //for threading and communication,
    protected Handler handler;
    //for the thread
    Thread myThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Progress = findViewById(R.id.text1);
        Button1 = findViewById(R.id.button1);

        Button1.setOnClickListener(new Button.OnClickListener() {
            /*
             * starts the Thread.
             */
            @Override
            public void onClick(View view) {
                myThread = new Thread(new CountingThread(0));
                myThread.start();
            }
        });

        //message handler for the animation.
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if (msg.what == 0) { // update progress.
                    Progress.setText("Progress: " + ProgressValue + "%");
                } else if (msg.what == 1) { //finished.
                    Progress.setText("Completed: " + ProgressValue + "%");
                }
                return true;
            }
        });
    }

    class CountingThread implements Runnable {
        int i = 0;  //default value of zero.
        CountingThread(int start) {
            i = start; //starting value for the count.
        }
        @Override
        public void run() {
            while (i < 100) {
                SystemClock.sleep(250);
                i++;
                if (i % 5 == 0) {
                    //update UI
                    ProgressValue = i;
                    //send message to update the screen.
                    handler.sendEmptyMessage(0);
                }
            }
            ProgressValue = i;
            handler.sendEmptyMessage(1);  //we are done!
        }
    }
}