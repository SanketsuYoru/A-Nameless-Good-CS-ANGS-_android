package com.example.anamelesscur;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import static com.example.anamelesscur.Funcs.cookies;
import static com.example.anamelesscur.Funcs.vcode;

public class MainActivity extends AppCompatActivity {
    public String result;
    private Handler mainActivityHander=new Handler();
    private TextView main_tv=null;
    private ImageView main_iv=null;
    private EditText main_et=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main_tv= (TextView)findViewById(R.id.main_Tv);
        main_et=(EditText) findViewById(R.id.main_editbox);
        main_iv=(ImageView)findViewById(R.id.main_vcode);

    }

    @Override
    protected void onResume() {
        super.onResume();
        VerificationThread task=new VerificationThread();
        task.start();

    }

    public void main_Button_Onclick(View view) {

        Funcs.vcodestr=main_et.getText().toString().trim();

        myThread task=new myThread();
        task.start();



    }

    public void refreshvcode(View view) {
        VerificationThread task=new VerificationThread();
        task.start();

    }

    //get sendvcode
    public class myThread extends Thread {
        @Override
        public void run() {

            result=Funcs.sendPost("http://jw.whcibe.com/default2.aspx");
            Log.e("result",result);



            Runnable run_= new Runnable(){
                @Override
                public void run() {

                    if(!result.equals("ValidationError"))
                    {
                    //main_iv.setImageBitmap(vcode);
                    main_tv.setText(result);
                    Intent nav=new Intent(MainActivity.this,Signedin.class);
                    nav.putExtra("Session", cookies);

                    //Intent nav=new Intent("android.intent.action.Signed_in");
                    //nav.setClass();
                    MainActivity.this.startActivity(nav);
                    }
                    else
                    {
                        Log.e("Error","验证错误/ValidationError");
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("验证错误/ValidationError");
                        builder.setMessage("请检查输入信息是否正确/你个哈皮又输错了？");
                        //builder.setIcon(R.drawable.ic_launcher);
                        builder.setView(new EditText(MainActivity.this));
                        builder.setPositiveButton("是" ,  null );
                        //builder.setNegativeButton("否", null);
                        builder.show();
                        VerificationThread task=new VerificationThread();
                        task.start();
                    }
                }

            };
            mainActivityHander.post(run_);
        }

    }


    //get vcodeimg
    public  class VerificationThread extends Thread {
        @Override
        public void run() {
            Funcs.initSigninParams();
            Runnable run_= new Runnable(){
                @Override
                public void run() {
                    main_iv.setImageBitmap(vcode);
                    //main_tv.setText(result);
                }

            };
            mainActivityHander.post(run_);
        }

    }




}


