package com.example.anamelesscur;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import androidx.databinding.ObservableList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.example.anamelesscur.Signedin.RequestedContent;

public class Funcs {

    public static String Userid="20170103040117";
    private static String PassWord="Masyumaro103";
    public static String vcodestr="";
    public static Bitmap vcode=null;
    public static String VIEWSTATE="";
    public static List<Curriculum> curriculums=new ArrayList<>();
    public static   String cookies="safedog-flow-item=; ASP.NET_SessionId=u5u1v545lo14uk55tv4nw3rp";
    private static String user_agent="Mozilla/5.0 (iPhone; CPU iPhone OS 13_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.1 Mobile/15E148 Safari/604.1";

    /**
     * 将map转换成key1=value1&key2=value2的形式
     * @param，map
     * @return
     * @throwsUnsupportedEncodingException
     */

    public static void initCurriculumSchedule(){
        Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("xh", Funcs.Userid);
        params.put("xm", "李旭初");
        params.put("gnmkdm", "N121602");
        //to do Http Things
        RequestedContent = Funcs.sendHttpRequest("GET", "http://jw.whcibe.com/xskbcx.aspx?xh=20170103040117&xm=李旭初&gnmkdm=N121602", params);
        //Jsoup解析课表
        Document document = Jsoup.parse(RequestedContent);
        Element table = document.getElementById("Table1");
        Elements trs = table.select("tr");
        for (int i = 0; i < trs.size(); ++i) {
            // 获取一个tr
            Element tr = trs.get(i);
            // 获取该行的所有td节点
            Elements tds = tr.select("td");
            // 选择某一个td节点
            //String temp=new String();
            for (int j = 0; j < tds.size(); ++j) {
                Element td = tds.get(j);
                //正则表达式筛选课程信息
                String pattern=".*\\{第.*";
                if(Pattern.matches(pattern, td.text()))
                {
                    //Do StrtoCurriculum Process
                    //curriculums.add(toCurriculum(td.text().split(" "),0));
                    toCurriculumRecurse(td.text().split(" "));
                    Log.e("Cs",td.text());
                    //temp+=td.text();
                    Signedin.CurriculumSchedule+=(td.text()+"\n");
                }
            }
            Log.e("Cs","");
            //CurriculumSchedule+=temp;
        }

    }

    private static ScheduleArragement toScheduleArragement(String str){
        ScheduleArragement sa=new ScheduleArragement();
        try{
            Log.e("Arragement",str);
            //get Which day
            char[] originCharArry=str.toCharArray();
            sa.day =String.valueOf(originCharArry[0])+String.valueOf(originCharArry[1]);
            String regex = "[^0-9]";
            str=str.replaceAll(regex," ");
            String[] strArrysplited=str.split(" ");

            String[] finalArray=new String[4];
            int flag=0;
            for (String s:strArrysplited) {
                if(!s.equals(""))
                {
                    Log.e("String s:strArrysplited",s+" flag: "+flag);
                    finalArray[flag++]=s;
                }
            }
            sa.dailyClassBegin=Integer.parseInt(finalArray[0]);
            sa.dailyClassEnd=Integer.parseInt(finalArray[1]);
            sa.weekBegin=Integer.parseInt(finalArray[2]);
            sa.weekEnd=Integer.parseInt(finalArray[3]);
        }
        catch (Exception e)
        {
            Log.e("toScheduleArragementError",e.toString());
        }

        return sa;
    }

    private static Curriculum toCurriculum_Onedata(String[] strArray){
        Log.e("Curriculum_Onedata",String.join(" ",strArray));
        Curriculum cur=new Curriculum();
        cur.cur_Name=strArray[0];
        //GetScheduleArragement
        cur.Schedule= toScheduleArragement(strArray[1]);
        cur.teacherName=strArray[2];
        cur.Location=strArray[3];
        return cur;
    }

    private static void toCurriculumRecurse(String[] strArraysplited){

        try{
            Curriculum cur=new Curriculum();
            //String [] strArraysplited=str.split(" ");
            if(strArraysplited.length>4)
            {
                //Process one str[],then recurse;
                //Do sth;
                List<String> temp=new ArrayList<>();
                temp.add(strArraysplited[0]);
                temp.add(strArraysplited[1]);
                temp.add(strArraysplited[2]);
                temp.add(strArraysplited[3]);
                cur=toCurriculum_Onedata(temp.toArray(new String[temp.size()]));
                curriculums.add(cur);
                //temp.clear() 重置temp
                temp.clear();
                for(int i=4;i<strArraysplited.length;i++)
                {
                    temp.add(strArraysplited[i]);
                }
                toCurriculumRecurse(temp.toArray(new String[temp.size()]));
            }
            else if (strArraysplited.length==4)
            {
                //Process one str[];
                cur=toCurriculum_Onedata(strArraysplited);
                curriculums.add(cur);
            }
            else {
                throw new Exception("Error :toCurriculumError strArraysplited.length!=4");
            }
        }
        catch (Exception e)
        {
            //Toast.makeText(getApplicationContext,e.toString(),0);
            Log.e("toCurriculumRecurse",e.toString());
        }




    }

    private static String convertMaptoOutputString(Map<String,String> map) {
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        try{
            for(Map.Entry<String,String> entry:map.entrySet()){
                if(isFirst)
                    isFirst = false;
                else
                    sb.append("&");

                sb.append(URLEncoder.encode(entry.getKey(),"UTF-8"));
                sb.append("=");
                sb.append(URLEncoder.encode(entry.getValue(),"UTF-8"));
            }

        }
        catch (Exception e)
        {
            Log.e("convertMaptoOutputString",e.toString());


        }
        return sb.toString();
    }

    //发送请求
    public static String sendHttpRequest(String Method,String url_str,Map<String, String> params){
        String webContent = new String();


        try{
            //定义一个URL对象
            URL url = new URL(url_str);
            //获取一个URLConnection链接
            java.net.HttpURLConnection conn=(HttpURLConnection)url.openConnection();
            conn.setRequestMethod(Method);
            conn.setDoInput(true);//表示从服务器获取数据
            conn.setRequestProperty("Cookie", cookies);
            conn.setRequestProperty("User-agent",user_agent);
            conn.setRequestProperty("Connection","keep-alive");
            conn.setRequestProperty("referer","http://jw.whcibe.com/xskbcx.aspx?xh=20170103040117&xm=李旭初&gnmkdm=N121602");
            conn.setInstanceFollowRedirects(false);

            if(params!=null)
            {
                conn.setDoOutput(true);//表示向服务器写数据
                OutputStream outputStream = conn.getOutputStream();//获取输出流对象
                BufferedWriter BW = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                BW.write(convertMaptoOutputString(params));
                BW.flush();
                BW.close();
                outputStream.close();
            }


            InputStream in = conn.getInputStream();
            int ResponseCode =conn.getResponseCode();
            Log.e("ResponseCode",ResponseCode+"");




            if(ResponseCode == 200)
            {
                BufferedReader br = new BufferedReader(new InputStreamReader(in,"GBK"));
                StringBuilder sb = new StringBuilder();
                //if(conn)


                String s;
                while ((s = br.readLine()) != null) {
                    sb.append(s);
                    Log.e("sendHttpRequest",s);

                    String pattern = ".*CheckCode.aspx.*";
                    //boolean isMatch = ;
                    if(Pattern.matches(pattern, s)) {
                        webContent="ValidationError";
                        return webContent;
                    }
                    Log.e("content",s);
                }

                webContent = sb.toString();
                //Log.e("content",webContent);

            }
            else if (ResponseCode==302)
            {
                String next_url=conn.getHeaderField("Location");
                Log.e("302next_url",next_url+"");
            }
            else
            {
                webContent="noResponse";
            }
            conn.disconnect();
        }
        catch (Exception e){

            Log.e("sendHttpRequestError",e.toString());
        }




        return webContent;
    }



    //登陆验证sendPost
    public static String sendPost(String url_str) {
        Log.e("vcodestr",vcodestr);
        //cookies="safedog-flow-item=; ASP.NET_SessionId=1vcn40uhrsub1n45hyylxuae";

        Log.e("cookies",cookies);
        //String  = new String();
        String webContent = new String();
        try {
            //init params
            //params.put("__VIEWSTATE","dDwyODE2NTM0OTg7Oz4YFQSIx7vdgUlYYWB/5wpKQx5/fg==");
            Map<String, String> params = new LinkedHashMap<String, String>();
            params.put("__VIEWSTATE",VIEWSTATE);
            params.put("txtUserName", Userid);
            params.put("TextBox2", PassWord);
            params.put("txtSecretCode",vcodestr);
            params.put("RadioButtonList1","%D1%A7%C9%FA");
            params.put("Button1","");
            params.put("lbLanguage","");
            params.put("hidPdrs","");
            params.put("hidsc","");




            //定义一个URL对象
            URL url = new URL(url_str);
            //获取一个URLConnection链接

            java.net.HttpURLConnection conn=(HttpURLConnection)url.openConnection();
            //URLConnection conn = url.openConnection();

            //设置URLConnection链接
            conn.setRequestMethod("POST");
            conn.setDoInput(true);//表示从服务器获取数据
            conn.setDoOutput(true);//表示向服务器写数据
            //conn.setRequestProperty("contentType", "GBK");
            conn.setRequestProperty("Cookie", cookies);
            conn.setRequestProperty("User-agent",user_agent);
            conn.setRequestProperty("Connection","keep-alive");
            //conn.setInstanceFollowRedirects(true);
            OutputStream outputStream = conn.getOutputStream();//获取输出流对象
            BufferedWriter BW = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            BW.write(convertMaptoOutputString(params));
            BW.flush();
            BW.close();
            outputStream.close();

//            cookies = conn.getHeaderField("Set-Cookie");// 取到所用的Cookie
//
//            if(cookies!=null)
//                Log.e("responseCookie",cookies);
//            else
//                Log.e("responseCookie","Noresponse");

            Log.e("outputstr",convertMaptoOutputString(params));
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(8000);



            InputStream in = conn.getInputStream();


            if(conn.getResponseCode() == 200)
            {
                BufferedReader br = new BufferedReader(new InputStreamReader(in,"GBK"));
                StringBuilder sb = new StringBuilder();
                //if(conn)


                String s;
                while ((s = br.readLine()) != null) {
                    sb.append(s);

                    String pattern = ".*CheckCode.aspx.*";
                    //boolean isMatch = ;
                    if(Pattern.matches(pattern, s)) {
                        webContent="ValidationError";
                        return webContent;
                    }
                    Log.e("content",s);
                }

                webContent = sb.toString();
                //Log.e("content",webContent);

            }
            else
            {
                webContent="noResponse";
            }
            conn.disconnect();




        } catch (Exception e) {
            Log.e("Error", e.toString());
        }
        //Log.e("content",webContent);




        return webContent;
    }

    public static void initSigninParams(){
        vcode=getBitmapFromURL("http://jw.whcibe.com/CheckCode.aspx");
        VIEWSTATE=getVIEWSTATEFromURL("http://jw.whcibe.com/");
        Log.e("VIEWSTATE",VIEWSTATE);

    }

    public static String getVIEWSTATEFromURL(String url_str){
        String vs=new String("VIEWSTATE_Error");

        try{
            URL url = new URL(url_str);
            //获取一个URLConnection链接

            java.net.HttpURLConnection conn=(HttpURLConnection)url.openConnection();
            //URLConnection conn = url.openConnection();

            //设置URLConnection链接
            conn.setRequestMethod("GET");
            conn.setDoInput(true);//表示从服务器获取数据
            //conn.setDoOutput(true);//表示向服务器写数据
            conn.setRequestProperty("Cookie", cookies);
            conn.setRequestProperty("User-agent",user_agent);
            conn.setRequestProperty("Connection","keep-alive");
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(8000);
            InputStream in = conn.getInputStream();


            if(conn.getResponseCode() == 200)
            {
                BufferedReader br = new BufferedReader(new InputStreamReader(in,"GBK"));
                //正则表达式串匹配剔除
                String s;
                while ((s = br.readLine()) != null) {
                    String pattern = ".*__VIEWSTATE.*";
                    if(Pattern.matches(pattern, s)) {
                        String REGEX = "<input type=\"hidden\" name=\"__VIEWSTATE\" value=\"";
                        Pattern p = Pattern.compile(REGEX);
                        vs=p.matcher(s).replaceAll("");
                        REGEX="\" />";
                        p = Pattern.compile(REGEX);
                        vs=p.matcher(vs).replaceAll("");

                    }

                }
            }

        }
        catch (Exception e){
            Log.e("ViewStateError",e.toString());

        }
        return vs;


    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            connection.connect();
            //cookies = conn.getHeaderField("ASP.NET_SessionId");
            //MainActivity
            cookies = connection.getHeaderField("Set-Cookie");// 取到所用的Cookie


            if(cookies!=null)
                Log.e("responseCookie",cookies);
            else
                Log.e("responseCookie","CookieNoResponse");


            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            connection.disconnect();
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }
}
