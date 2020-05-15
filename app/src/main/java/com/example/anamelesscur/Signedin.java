package com.example.anamelesscur;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anamelesscur.databinding.ActivitySignedinBinding;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.example.anamelesscur.Funcs.curriculums;

public class Signedin extends AppCompatActivity {

    private TextView Signedin_tv = null;
    private TextView signedinContent_tv = null;
    public static String RequestedContent = "";
    public static String CurriculumSchedule = "";
    private Handler UIhander = new Handler();
    private List<Curriculum> csList=new ArrayList<>();
    private Context context_UI=this;
    //data Binding
    ActivitySignedinBinding dataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        initCurriculum();
        //setContentView(R.layout.activity_signedin);
        //Signedin_tv = findViewById(R.id.signedin_tv);
        //signedinContent_tv = findViewById(R.id.signedinContent_tv);
        //Curriculum c=new Curriculum();
        csList.add(new Curriculum());
        csList.add(new Curriculum());
        csList.add(new Curriculum());
        dataBinding= DataBindingUtil.setContentView(this,R.layout.activity_signedin);


    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent paramin = getIntent();
        //String data=paramin.getStringExtras("Session");

        //Signedin_tv.setText(paramin.getStringExtra("Session"));


    }

    public void initCurriculum() {
        Thread task = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Funcs.initCurriculumSchedule();
                    Runnable run_ = new Runnable() {
                        @Override
                        public void run() {
                            //update UI
                            MylistviewAdapter adapter = new MylistviewAdapter(context_UI, curriculums, R.layout.listview_layout, BR.Curr);
                            dataBinding.setAdapter(adapter);
                            //signedinContent_tv.setText(CurriculumSchedule);
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
                    //signedinContent_tv.setText("");
                }
            };
            UIhander.post(run_);

        }
    }
}
