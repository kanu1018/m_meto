package com.kitri.meto.m.metour;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ImageView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2016-08-11.
 */
public class AlarmByDate extends Activity {
    private  static int ONE_MINUTE = 5626;

    List<ScheduleDTO> days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);

        ////////////////////////////////////////////////////
        Intent before = getIntent();
        final int mem_num = before.getIntExtra("mem_num",0);

        ImageView imageView = (ImageView)findViewById(R.id.loading_image);
        AnimationDrawable animationDrawable = (AnimationDrawable)imageView.getBackground();
        animationDrawable.start();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String requestURL = "http://192.168.14.19:8805/meto/and/schedule/getDay.do";

        String ID = "1";
        try{
            HttpClient client = new DefaultHttpClient();
            //// TODO: 세션유지
            //HttpClient client = SessionControl.getHttpclient();
            HttpPost post = new HttpPost(requestURL);
            List<NameValuePair> paramList = new ArrayList<>();

            //// TODO: 세션유지
            //String ID = SessionControl.id;
            paramList.add(new BasicNameValuePair("id", ID));
            post.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));

            HttpResponse response = client.execute(post);
            final HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            days = getXML(is);

        }catch (Exception e){
            Log.d("sendPost===> ", e.toString());
        }

        //알람 등록하기
        List<Calendar> calendar = new ArrayList<>();
        for(int i = 0; i < days.size(); i++){
            Calendar tmp = Calendar.getInstance();
            //알람시간 set해주기
            String day[] = days.get(i).getMain_date().split("/");
            //tmp.set(Integer.parseInt(day[0]),Integer.parseInt(day[1])-1,Integer.parseInt(day[2]),9,0,0); //year, month(1빼줘야해), day, hour(24시간), minute, second
            tmp.set(2016,7,17,9,0,0); //year, month(1빼줘야해), day, hour(24시간), minute, second
            Log.d("시간",tmp.toString());
            calendar.add(tmp);
        }
        new AlarmMETO(getApplicationContext()).Alarm(calendar, days);


        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(AlarmByDate.this, SearchByCategory.class);
                i.putExtra("mem_num", mem_num);
                startActivity(i);
                finish();
            }
        }, 3500);

    }

    public List<ScheduleDTO> getXML(InputStream is){
        List<ScheduleDTO> list = new ArrayList<>();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(is, "UTF-8");

            ScheduleDTO tmp = null;
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT){
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        String startTag = parser.getName();
                        if(startTag.equals("time")){
                            tmp = new ScheduleDTO();
                        }

                        if(tmp != null){
                            if(startTag.equals("main_date")){
                                tmp.setMain_date(parser.nextText());
                            }else if(startTag.equals("main_num")){
                                tmp.setMain_num(Integer.parseInt(parser.nextText()));
                            }else if(startTag.equals("main_title")){
                                tmp.setMain_title(parser.nextText());
                            }else if(startTag.equals("main_writer")){
                                tmp.setMain_writer(Integer.parseInt(parser.nextText()));
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        String endTag = parser.getName();
                        if(endTag.equals("time")){
                            list.add(tmp);
                        }
                        break;
                }
                eventType = parser.next();
            }
        }catch (XmlPullParserException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return list;
    }
}
