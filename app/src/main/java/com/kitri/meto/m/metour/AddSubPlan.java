package com.kitri.meto.m.metour;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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
import java.util.List;

/**
 * Created by Administrator on 2016-08-08.
 */
public class AddSubPlan extends Activity implements View.OnClickListener {
    EditText title, memo, place;
    Spinner start_time, end_time, mission;
    Button ok, cancel, place_select;

    String[] time;
    List<Integer> index;
    int main_num;

    Double latitude, longitude;


    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.addsubplan);

       StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
       StrictMode.setThreadPolicy(policy);

       title = (EditText) findViewById(R.id.add_subplan_title);
       memo = (EditText) findViewById(R.id.add_subplan_memo);
        place = (EditText) findViewById(R.id.add_subplan_place_text);

       start_time = (Spinner) findViewById(R.id.add_subplan_starttime);
       end_time = (Spinner) findViewById(R.id.add_subplan_endtime);
       mission = (Spinner) findViewById(R.id.add_subplan_mission);

       ok = (Button) findViewById(R.id.add_subplan);
       cancel = (Button) findViewById(R.id.add_subplan_cancel);
        place_select = (Button) findViewById(R.id.add_subplan_place);
        ok.setOnClickListener(this);
       cancel.setOnClickListener(this);
        place_select.setOnClickListener(this);
       time = getResources().getStringArray(R.array.time);

       Intent intent = getIntent();
        main_num = intent.getExtras().getInt("main_num");

       String requestURL = "http://192.168.14.45:8805/meto/and/subplan/add.do";

       try{
           HttpClient client = new DefaultHttpClient();
           HttpPost post = new HttpPost(requestURL);
           List<NameValuePair> paramList = new ArrayList<>();
           paramList.add(new BasicNameValuePair("main_num", Integer.toString(main_num)));

           post.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));

           HttpResponse response = client.execute(post);
           HttpEntity entity = response.getEntity();
           InputStream is = entity.getContent();

           index = getXML(is);

           ArrayList<String> entries = new ArrayList<>();
           for(int i = 0; i < index.size(); i++){
               if(index.get(i) == 1){
                   entries.add(time[i]);
               }
           }

           ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, entries);
           start_time.setAdapter(adapter);
           end_time.setAdapter(adapter);

       }catch (Exception e){
           Log.d("sendPost == >", e.toString());
       }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_subplan) {
            String requestURL = "http://192.168.14.45:8805/meto/and/subplan/addok.do";

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(requestURL);
            List<NameValuePair> paramList = new ArrayList<>();

            String tmp = mission.getSelectedItem().toString();
            String mission_chk = null;
            if (tmp.equals("미션 선택안함")) {
                mission_chk = "n";
            } else if (tmp.equals("명소 찾아가기")) {
                mission_chk = "g";
            } else if (tmp.equals("명소 사진찍기")) {
                mission_chk = "p";
            }

            paramList.add(new BasicNameValuePair("main_num", Integer.toString(main_num)));
            paramList.add(new BasicNameValuePair("sub_title", title.getText().toString()));
            paramList.add(new BasicNameValuePair("start_time", start_time.getSelectedItem().toString()));
            paramList.add(new BasicNameValuePair("end_time", end_time.getSelectedItem().toString()));
            paramList.add(new BasicNameValuePair("place", place.getText().toString()));
            paramList.add(new BasicNameValuePair("mission", mission_chk));
            paramList.add(new BasicNameValuePair("memo", memo.getText().toString()));
            paramList.add(new BasicNameValuePair("llh_x", String.valueOf(latitude)));
            paramList.add(new BasicNameValuePair("llh_y", String.valueOf(longitude)));


            try {
                post.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
                HttpResponse response = client.execute(post);
            } catch (Exception e) {
                Log.d("sendPost == >", e.toString());
            }

            finish();

        } else if (v.getId() == R.id.add_subplan_cancel) {
            finish();
        } else if(v.getId() == R.id.add_subplan_place){
            Intent intent = new Intent(getApplicationContext(), SelectLocation.class);
            startActivityForResult(intent, 100);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        latitude = data.getDoubleExtra("latitude",0);
        longitude = data.getDoubleExtra("longitude",0);
        place.setText(data.getStringExtra("place"));

        Log.d("위도 경도==>",""+latitude+"/"+longitude);
    }

    public List<Integer> getXML(InputStream is){
        List<Integer> list = new ArrayList<>();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(is, "UTF-8");

            int tmp = 0;
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT){
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        String startTag = parser.getName();
                        if(startTag.equals("flag")){
                            tmp = Integer.parseInt(parser.nextText());
                            list.add(tmp);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        String endTag = parser.getName();
                        if(endTag.equals("flag")){
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
