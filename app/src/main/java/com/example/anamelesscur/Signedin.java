package com.example.anamelesscur;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Signedin extends AppCompatActivity {

    private TextView Signedin_tv = null;
    private TextView signedinContent_tv = null;
    public static String RequestedContent = "";
    public static String CurriculumSchedule = "";

    private Handler UIhander = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signedin);
        Signedin_tv = findViewById(R.id.signedin_tv);
        signedinContent_tv = findViewById(R.id.signedinContent_tv);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent paramin = getIntent();
        //String data=paramin.getStringExtras("Session");
        Signedin_tv.setText(paramin.getStringExtra("Session"));


    }

    public void showCS_btn_Click(View view) {
        Thread task = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Funcs.initCurriculumSchedule();
                    Runnable run_ = new Runnable() {
                        @Override
                        public void run() {
                            //update UI
                            signedinContent_tv.setText(CurriculumSchedule);
                        }
                    };
                    UIhander.post(run_);
                }
                catch (final Exception  e)
                {

                    //Code maybe useless
                    Runnable run_ = new Runnable() {
                        @Override
                        public void run() {
                            //Make Error Toast
                            if(e!=null)
                            Toast.makeText(Signedin.this, e.toString(),Toast.LENGTH_LONG);
                        }
                    };
                    UIhander.post(run_);
                }


            }
        });
        task.start();
    }
    private class HttpThread extends Thread {
        @Override
        public void run() {
            //todoHttpConn

            Runnable run_ = new Runnable() {
                @Override
                public void run() {
                    //updateUI
                    signedinContent_tv.setText("");
                }
            };
            UIhander.post(run_);

        }
    }
}
