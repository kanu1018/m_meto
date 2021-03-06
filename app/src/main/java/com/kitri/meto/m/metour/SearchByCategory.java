package com.kitri.meto.m.metour;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016-08-09.
 */
public class SearchByCategory extends AppCompatActivity implements View.OnClickListener{
    LinearLayout listsel;
    PopupWindow pwindo;
    RadioButton rb1,rb2,rb3,rb4,rb5,rb6;
    Button popfin,popsearch;
    EditText location;
    Button btnSearchBest,btnSearchLocation,btnSearchGender,btnSearchAge;
    Intent intent;
    int mem_num;
    int mWidthPixels, mHeightPixels;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_shareplan);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        btnSearchBest = (Button)findViewById(R.id.btnSearchBest);
        btnSearchLocation = (Button)findViewById(R.id.btnSearchLocation);
        btnSearchGender = (Button)findViewById(R.id.btnSearchGender);
        btnSearchAge = (Button)findViewById(R.id.btnSearchAge);
        listsel = (LinearLayout)findViewById(R.id.layoutSel);
        btnSearchBest.setOnClickListener(this);
        btnSearchLocation.setOnClickListener(this);
        btnSearchGender.setOnClickListener(this);
        btnSearchAge.setOnClickListener(this);

        WindowManager w = getWindowManager();
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
        // since SDK_INT = 1;
        mWidthPixels = metrics.widthPixels;
        mHeightPixels = metrics.heightPixels;

        // 상태바와 메뉴바의 크기를 포함해서 재계산
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
            try {
                mWidthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
                mHeightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
            } catch (Exception ignored) {
            }
        // 상태바와 메뉴바의 크기를 포함
        if (Build.VERSION.SDK_INT >= 17)
            try {
                Point realSize = new Point();
                Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
                mWidthPixels = realSize.x;
                mHeightPixels = realSize.y;
            } catch (Exception ignored) {

            }
        intent = getIntent();
        mem_num = intent.getExtras().getInt("mem_num",0);

        Button btnMoveCalendar = (Button) findViewById(R.id.moveCalendar);
        btnMoveCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_new = new Intent(getApplicationContext(), CreateCalendar.class);
                intent_new.putExtra("main_writer", mem_num);
                startActivity(intent_new);
            }
        });

        Button btnTodayPlan = (Button)findViewById(R.id.moveTodayPlan);
        btnTodayPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int main_num = 0;
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                //db정보 읽어오기 시작

                Date date = new Date();
                SimpleDateFormat transFormat = new SimpleDateFormat("yyyy/MM/dd");
                String to = transFormat.format(date);
                String requestURL =  "http://192.168.14.45:8805/meto/and/schedule/getMainNum.do?main_writer="+mem_num+"&main_date="+to;


                //HttpClient client   = new DefaultHttpClient();
                HttpClient client   = SessionControl.getHttpclient();
                HttpPost post    = new HttpPost(requestURL);
                List<NameValuePair> paramList = new ArrayList<>();

                try {
                    post.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
                    HttpResponse response = client.execute(post);

                    HttpEntity entity = response.getEntity();
                    InputStream is = entity.getContent();
                    main_num = getXMLNum(is);
                    Log.d("FFFFFFFFFF",Integer.toString(main_num));

                    //Toast.makeText(getApplicationContext(), "flag 확인 "+flag,Toast.LENGTH_SHORT).show();
                } catch(Exception e) {
                    Log.d("sendPost===> ", e.toString());
                }
                if(main_num!=0) {
                    Intent intent_new = new Intent(getApplicationContext(), ToDayListSubplan.class);
                    intent_new.putExtra("main_num", main_num);
                    startActivity(intent_new);
                }else{
                    Toast.makeText(getApplicationContext(), "오늘 일정이 없습니다.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnMoveShareList = (Button)findViewById(R.id.moveShareList);
        assert btnMoveShareList != null;
        btnMoveShareList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(),SearchByCategory.class);
                in.putExtra("mem_num",mem_num);
                startActivity(in);

            }
        });

        String requestURL = "http://192.168.14.45:8805/meto/and/share/list.do";
        ArrayList<WebView> weblist = new ArrayList<WebView>();


        HttpClient client   = new DefaultHttpClient();
        HttpPost post    = new HttpPost(requestURL);
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        SharePlanDTO sDto;
        List<SharePlanDTO> slist;

        try {
            post.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            slist = getXML(is);
            LinearLayout laysub[] = new LinearLayout[slist.size()];


            for(int i = 0;i<slist.size();i++){
                laysub[i] = new LinearLayout(this);
                laysub[i].setOrientation(LinearLayout.VERTICAL);
                sDto = slist.get(i);
                TextView text1 = new TextView(this);
                text1.setText(sDto.getId());
                WebView wv1 = new WebView(this);
                weblist.add(wv1);
                wv1.loadUrl(sDto.getPhoto());
                wv1.setWebViewClient(new webClient());
                WebSettings set = wv1.getSettings();
                final SharePlanDTO finalSDto = sDto;
                wv1.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()){
                            case MotionEvent.ACTION_DOWN:
                                Intent in = new Intent(getApplicationContext(),SharePlanActivity.class);
                                in.putExtra("share_num",String.valueOf(finalSDto.getShare_num()));
                                startActivity(in);
                                break;
                        }
                        return false;
                    }
                });
                set.setJavaScriptEnabled(true);
                set.setBuiltInZoomControls(true);

                LinearLayout lay1 = new LinearLayout(this);
                lay1.setOrientation(LinearLayout.HORIZONTAL);
                TextView text2 = new TextView(this);
                text2.setText(sDto.getShare_title());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.MATCH_PARENT);
                params.weight = 9.0f;
                text2.setLayoutParams(params);
                TextView text3 = new TextView(this);
                params = new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.MATCH_PARENT);
                params.weight = 1.0f;
                params.bottomMargin=30;
                text3.setText("♥"+sDto.getMetoo());
                text3.setLayoutParams(params);
                text2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(getApplicationContext(),SharePlanActivity.class);
                        in.putExtra("share_num",String.valueOf(finalSDto.getShare_num()));
                        startActivity(in);
                    }
                });

                lay1.addView(text2);
                lay1.addView(text3);
                laysub[i].addView(text1);
                laysub[i].addView(wv1);
                laysub[i].addView(lay1);

                listsel.addView(laysub[i]);
            }
        } catch(Exception e) {
            Log.d("sendPost===> ", e.toString());
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnSearchBest){
            getBestArticle();
        }else if(v.getId() == R.id.btnSearchLocation){
            popupwindowLocation();
        }else if(v.getId() == R.id.btnSearchGender){
            popupwindowGender();
        }else if(v.getId() == R.id.btnSearchAge){
            popupwindowAge();
        }
    }
    public void popupwindowAge(){
        try {
            //  LayoutInflater 객체와 시킴
            LayoutInflater inflater = (LayoutInflater) SearchByCategory.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View layout = inflater.inflate(R.layout.popupage,
                    (ViewGroup) findViewById(R.id.popup_element));

            pwindo = new PopupWindow(layout,mWidthPixels-10, mHeightPixels-1550, true);

            pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);

            popsearch = (Button) layout.findViewById(R.id.popsearch);
            popsearch.setOnClickListener(pBtn04);

            popfin = (Button) layout.findViewById(R.id.popfin);
            popfin.setOnClickListener(pBtn02);

            rb1 = (RadioButton)layout.findViewById(R.id.age1);
            rb2 = (RadioButton)layout.findViewById(R.id.age2);
            rb3 = (RadioButton)layout.findViewById(R.id.age3);
            rb4 = (RadioButton)layout.findViewById(R.id.age4);
            rb5 = (RadioButton)layout.findViewById(R.id.age5);
            rb6 = (RadioButton)layout.findViewById(R.id.age6);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void popupwindowGender(){
        try {
            //  LayoutInflater 객체와 시킴
            LayoutInflater inflater = (LayoutInflater) SearchByCategory.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View layout = inflater.inflate(R.layout.popupgender,
                    (ViewGroup) findViewById(R.id.popup_element));

            pwindo = new PopupWindow(layout,mWidthPixels-400, mHeightPixels-1550, true);
            pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);

            popsearch = (Button) layout.findViewById(R.id.popsearch);
            popsearch.setOnClickListener(pBtn03);

            popfin = (Button) layout.findViewById(R.id.popfin);
            popfin.setOnClickListener(pBtn02);

            rb1 = (RadioButton)layout.findViewById(R.id.m);
            rb2 = (RadioButton)layout.findViewById(R.id.f);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public View.OnClickListener pBtn01 =
            new View.OnClickListener() {
                public void onClick(View v) {
                    //팝업창 끄기
                    getArticleByLocation(location.getText().toString());
                    location.setText("");
                    pwindo.dismiss();
                }
            };
    //팝업창 끄기
    public View.OnClickListener pBtn02 =
            new View.OnClickListener() {
                public void onClick(View v) {
                    pwindo.dismiss();
                }
            };

    public View.OnClickListener pBtn03 =
            new View.OnClickListener() {
                public void onClick(View v) {
                    if(rb1.isChecked()){
                        getArticleByGender("m");
                    }else{
                        getArticleByGender("f");
                    }
                    pwindo.dismiss();
                }
            };
    public View.OnClickListener pBtn04 =
            new View.OnClickListener() {
                public void onClick(View v) {
                    if(rb1.isChecked()){
                        getArticleByAge(10);
                    }else if(rb2.isChecked()){
                        getArticleByAge(20);
                    }else if(rb3.isChecked()){
                        getArticleByAge(30);
                    }else if(rb4.isChecked()){
                        getArticleByAge(40);
                    }else if(rb5.isChecked()){
                        getArticleByAge(50);
                    }else if(rb6.isChecked()){
                        getArticleByAge(60);
                    }
                    pwindo.dismiss();
                }
            };



    public void popupwindowLocation(){
        try {
            //  LayoutInflater 객체와 시킴
            LayoutInflater inflater = (LayoutInflater) SearchByCategory.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View layout = inflater.inflate(R.layout.popuplocation,
                    (ViewGroup) findViewById(R.id.popup_element));

            pwindo = new PopupWindow(layout, mWidthPixels-400, mHeightPixels-1550, true);
            pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);

            popsearch = (Button) layout.findViewById(R.id.popsearch);
            popsearch.setOnClickListener(pBtn01);

            location = (EditText) layout.findViewById(R.id.location);

            popfin = (Button) layout.findViewById(R.id.popfin);
            popfin.setOnClickListener(pBtn02);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getArticleByAge(int s){
        listsel.removeAllViews();
        String requestURL =  "http://192.168.14.45:8805/meto/and/share/agelist.do?age="+s;

        ArrayList<WebView> weblist = new ArrayList<WebView>();

        HttpClient client   = new DefaultHttpClient();
        HttpPost post    = new HttpPost(requestURL);
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        SharePlanDTO sDto;
        List<SharePlanDTO> slist;

        try {
            post.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            slist = getXML(is);
            LinearLayout laysub[] = new LinearLayout[slist.size()];


            for(int i = 0;i<slist.size();i++){
                laysub[i] = new LinearLayout(this);
                laysub[i].setOrientation(LinearLayout.VERTICAL);
                sDto = slist.get(i);
                TextView text1 = new TextView(this);
                text1.setText(sDto.getId());
                WebView wv1 = new WebView(this);
                weblist.add(wv1);
                wv1.loadUrl(sDto.getPhoto());
                wv1.setWebViewClient(new webClient());
                WebSettings set = wv1.getSettings();
                final SharePlanDTO finalSDto = sDto;
                wv1.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()){
                            case MotionEvent.ACTION_DOWN:
                                Intent in = new Intent(getApplicationContext(),SharePlanActivity.class);
                                in.putExtra("share_num",String.valueOf(finalSDto.getShare_num()));
                                startActivity(in);
                                break;
                        }
                        return false;
                    }
                });
                set.setJavaScriptEnabled(true);
                set.setBuiltInZoomControls(true);

                LinearLayout lay1 = new LinearLayout(this);
                lay1.setOrientation(LinearLayout.HORIZONTAL);
                TextView text2 = new TextView(this);
                text2.setText(sDto.getShare_title());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.MATCH_PARENT);
                params.weight = 9.0f;
                text2.setLayoutParams(params);
                TextView text3 = new TextView(this);
                params = new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.MATCH_PARENT);
                params.weight = 1.0f;
                params.bottomMargin=30;
                text3.setText("♥"+sDto.getMetoo());
                text3.setLayoutParams(params);
                text2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(getApplicationContext(),SharePlanActivity.class);
                        in.putExtra("share_num",String.valueOf(finalSDto.getShare_num()));
                        startActivity(in);
                    }
                });

                lay1.addView(text2);
                lay1.addView(text3);
                laysub[i].addView(text1);
                laysub[i].addView(wv1);
                laysub[i].addView(lay1);

                listsel.addView(laysub[i]);
            }
        } catch(Exception e) {
            Log.d("sendPost===> ", e.toString());
        }

    }



    public void getArticleByLocation(String s){
        listsel.removeAllViews();
        String requestURL =  "http://192.168.14.45:8805/meto/and/share/placelist.do?place="+s;

        ArrayList<WebView> weblist = new ArrayList<WebView>();

        HttpClient client   = new DefaultHttpClient();
        HttpPost post    = new HttpPost(requestURL);
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        SharePlanDTO sDto;
        List<SharePlanDTO> slist;

        try {
            post.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            slist = getXML(is);
            LinearLayout laysub[] = new LinearLayout[slist.size()];


            for(int i = 0;i<slist.size();i++){
                laysub[i] = new LinearLayout(this);
                laysub[i].setOrientation(LinearLayout.VERTICAL);
                sDto = slist.get(i);
                TextView text1 = new TextView(this);
                text1.setText(sDto.getId());
                WebView wv1 = new WebView(this);
                weblist.add(wv1);
                wv1.loadUrl(sDto.getPhoto());
                wv1.setWebViewClient(new webClient());
                WebSettings set = wv1.getSettings();
                final SharePlanDTO finalSDto = sDto;
                wv1.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()){
                            case MotionEvent.ACTION_DOWN:
                                Intent in = new Intent(getApplicationContext(),SharePlanActivity.class);
                                in.putExtra("share_num",String.valueOf(finalSDto.getShare_num()));
                                startActivity(in);
                                break;
                        }
                        return false;
                    }
                });
                set.setJavaScriptEnabled(true);
                set.setBuiltInZoomControls(true);

                LinearLayout lay1 = new LinearLayout(this);
                lay1.setOrientation(LinearLayout.HORIZONTAL);
                TextView text2 = new TextView(this);
                text2.setText(sDto.getShare_title());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.MATCH_PARENT);
                params.weight = 9.0f;
                text2.setLayoutParams(params);
                TextView text3 = new TextView(this);
                params = new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.MATCH_PARENT);
                params.weight = 1.0f;
                params.bottomMargin=30;
                text3.setText("♥"+sDto.getMetoo());
                text3.setLayoutParams(params);
                text2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(getApplicationContext(),SharePlanActivity.class);
                        in.putExtra("share_num",String.valueOf(finalSDto.getShare_num()));
                        startActivity(in);
                    }
                });

                lay1.addView(text2);
                lay1.addView(text3);
                laysub[i].addView(text1);
                laysub[i].addView(wv1);
                laysub[i].addView(lay1);

                listsel.addView(laysub[i]);
            }
        } catch(Exception e) {
            Log.d("sendPost===> ", e.toString());
        }

    }

    public void getBestArticle(){
        listsel.removeAllViews();
        String requestURL =  "http://192.168.14.45:8805/meto/and/share/best.do";
        ArrayList<WebView> weblist = new ArrayList<WebView>();


        HttpClient client   = new DefaultHttpClient();
        HttpPost post    = new HttpPost(requestURL);
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        SharePlanDTO sDto;
        List<SharePlanDTO> slist;

        try {
            post.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            slist = getXML(is);
            LinearLayout laysub[] = new LinearLayout[slist.size()];


            for(int i = 0;i<slist.size();i++){
                laysub[i] = new LinearLayout(this);
                laysub[i].setOrientation(LinearLayout.VERTICAL);
                sDto = slist.get(i);
                TextView text1 = new TextView(this);
                text1.setText(sDto.getId());
                WebView wv1 = new WebView(this);
                weblist.add(wv1);
                wv1.loadUrl(sDto.getPhoto());
                wv1.setWebViewClient(new webClient());
                WebSettings set = wv1.getSettings();
                final SharePlanDTO finalSDto = sDto;
                wv1.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()){
                            case MotionEvent.ACTION_DOWN:
                                Intent in = new Intent(getApplicationContext(),SharePlanActivity.class);
                                in.putExtra("share_num",String.valueOf(finalSDto.getShare_num()));
                                startActivity(in);
                                break;
                        }
                        return false;
                    }
                });
                set.setJavaScriptEnabled(true);
                set.setBuiltInZoomControls(true);

                LinearLayout lay1 = new LinearLayout(this);
                lay1.setOrientation(LinearLayout.HORIZONTAL);
                TextView text2 = new TextView(this);
                text2.setText(sDto.getShare_title());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.MATCH_PARENT);
                params.weight = 9.0f;
                text2.setLayoutParams(params);
                TextView text3 = new TextView(this);
                params = new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.MATCH_PARENT);
                params.weight = 1.0f;
                params.bottomMargin=30;
                text3.setText("♥"+sDto.getMetoo());
                text3.setLayoutParams(params);
                text2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(getApplicationContext(),SharePlanActivity.class);
                        in.putExtra("share_num",String.valueOf(finalSDto.getShare_num()));
                        startActivity(in);
                    }
                });

                lay1.addView(text2);
                lay1.addView(text3);
                laysub[i].addView(text1);
                laysub[i].addView(wv1);
                laysub[i].addView(lay1);

                listsel.addView(laysub[i]);
            }
        } catch(Exception e) {
            Log.d("sendPost===> ", e.toString());
        }

    }

    private void getArticleByGender(String s) {
        listsel.removeAllViews();
        String requestURL =  "http://192.168.14.45:8805/meto/and/share/genderlist.do?gender="+s;
        ArrayList<WebView> weblist = new ArrayList<WebView>();


        HttpClient client   = new DefaultHttpClient();
        HttpPost post    = new HttpPost(requestURL);
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        SharePlanDTO sDto;
        List<SharePlanDTO> slist;

        try {
            post.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            slist = getXML(is);
            LinearLayout laysub[] = new LinearLayout[slist.size()];


            for(int i = 0;i<slist.size();i++){
                laysub[i] = new LinearLayout(this);
                laysub[i].setOrientation(LinearLayout.VERTICAL);
                sDto = slist.get(i);
                TextView text1 = new TextView(this);
                text1.setText(sDto.getId());
                WebView wv1 = new WebView(this);
                weblist.add(wv1);
                wv1.loadUrl(sDto.getPhoto());
                wv1.setWebViewClient(new webClient());
                WebSettings set = wv1.getSettings();
                final SharePlanDTO finalSDto = sDto;
                wv1.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()){
                            case MotionEvent.ACTION_DOWN:
                                Intent in = new Intent(getApplicationContext(),SharePlanActivity.class);
                                in.putExtra("share_num",String.valueOf(finalSDto.getShare_num()));
                                startActivity(in);
                                break;
                        }
                        return false;
                    }
                });
                set.setJavaScriptEnabled(true);
                set.setBuiltInZoomControls(true);

                LinearLayout lay1 = new LinearLayout(this);
                lay1.setOrientation(LinearLayout.HORIZONTAL);
                TextView text2 = new TextView(this);
                text2.setText(sDto.getShare_title());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.MATCH_PARENT);
                params.weight = 9.0f;
                text2.setLayoutParams(params);
                TextView text3 = new TextView(this);
                params = new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.MATCH_PARENT);
                params.weight = 1.0f;
                params.bottomMargin=30;
                text3.setText("♥"+sDto.getMetoo());
                text3.setLayoutParams(params);
                text2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(getApplicationContext(),SharePlanActivity.class);
                        in.putExtra("share_num",String.valueOf(finalSDto.getShare_num()));
                        startActivity(in);
                    }
                });

                lay1.addView(text2);
                lay1.addView(text3);
                laysub[i].addView(text1);
                laysub[i].addView(wv1);
                laysub[i].addView(lay1);

                listsel.addView(laysub[i]);
            }
        } catch(Exception e) {
            Log.d("sendPost===> ", e.toString());
        }
    }
    class webClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    public List<SharePlanDTO> getXML(InputStream is) {
        List<SharePlanDTO> list = new ArrayList<SharePlanDTO>();

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(is, "UTF-8");

            int eventType = parser.getEventType();

            SharePlanDTO dto = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {

                switch (eventType) {
                    case XmlPullParser.START_TAG:

                        String startTag = parser.getName();
                        if (startTag.equals("shareplan")) {
                            dto = new SharePlanDTO();
                        }
                        if (dto != null) {
                            if (startTag.equals("sNum")) {
                                dto.setShare_num(Integer.parseInt(parser.nextText()));
                            } else if (startTag.equals("sTitle")) {
                                dto.setShare_title(parser.nextText());
                            } else if (startTag.equals("sWriter")) {
                                dto.setWriter(Integer.parseInt(parser.nextText()));
                            } else if (startTag.equals("sLoc")) {
                                dto.setLocation(parser.nextText());
                            } else if (startTag.equals("sMetoo")) {
                                dto.setMetoo(Integer.parseInt(parser.nextText()));
                            } else if (startTag.equals("sPoint")) {
                                dto.setPoint_num(Integer.parseInt(parser.nextText()));
                            } else if(startTag.equals("sId")){
                                dto.setId(parser.nextText());
                            } else if(startTag.equals("sPhoto")){
                                dto.setPhoto(parser.nextText());
                            }
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        String endTag = parser.getName();
                        if (endTag.equals("shareplan")) {
                            list.add(dto);
                        }
                        break;
                } // end
                eventType = parser.next();
            } // end of while
        } catch(Exception e) {
            Log.d("SharePlan getXML=====>",e.toString());
        } // end of try
        return list;
    }

    public int getXMLNum(InputStream is) {
        int num =0;

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(is, "UTF-8");

            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {

                switch (eventType) {
                    case XmlPullParser.START_TAG:

                        String startTag = parser.getName();
                        Log.d("STAG",startTag);
                        if  (startTag.equals("number")) {
                            num = Integer.parseInt(parser.nextText());
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        String endTag = parser.getName();
                        Log.d("ETAG",endTag);
                        if (endTag.equals("start")) {
                        }
                        break;
                } // end
                eventType = parser.next();
            } // end of while
        } catch(Exception e) {
            Log.d("SelectActivityError",e.toString());
        } // end of try

        return num;
    }
}