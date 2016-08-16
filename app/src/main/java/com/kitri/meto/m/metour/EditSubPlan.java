package com.kitri.meto.m.metour;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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
public class EditSubPlan extends Activity implements View.OnClickListener {
    EditText title,  memo, photo, place;
    Spinner start_time, end_time, mission;
    Button ok, cancel, del, place_select;
    int subNum;
    int main_num;

    SubPlanDTO dto;

    Double latitude, longitude;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editsubplan);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        title = (EditText) findViewById(R.id.edit_subplan_title);
        memo = (EditText) findViewById(R.id.edit_subplan_memo);
        place = (EditText) findViewById(R.id.edit_subplan_place_text);

        start_time = (Spinner) findViewById(R.id.edit_subplan_starttime);
        end_time = (Spinner) findViewById(R.id.edit_subplan_endtime);
        mission = (Spinner) findViewById(R.id.edit_subplan_mission);
        photo = (EditText)findViewById(R.id.edit_subplan_photo);

        ok = (Button) findViewById(R.id.edit_subplan);
        cancel = (Button) findViewById(R.id.edit_subplan_cancel);
        del = (Button)findViewById(R.id.edit_subplan_del);
        place_select = (Button) findViewById(R.id.edit_subplan_place);

        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
        del.setOnClickListener(this);
        place_select.setOnClickListener(this);

        Intent intent = getIntent();
        subNum = intent.getIntExtra("subNum", subNum);
        main_num = intent.getIntExtra("main_num", main_num);

        Log.d("subNum == >", String.valueOf(subNum));

        String requestURL = "http://192.168.14.21:8805/meto/and/subplan/listview.do";
        try{
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(requestURL);
            List<NameValuePair> paramList = new ArrayList<>();
            paramList.add(new BasicNameValuePair("sub_num", String.valueOf(subNum)));

            post.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));

            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();

            dto = getXML(is);

            title.setText(dto.getSub_title());
            String tmp = dto.getStart_time();
            int start = getIndex(tmp.substring(0,2), tmp.substring(3,5));
            start_time.setSelection(start);
            tmp = dto.getEnd_time();
            int end = getIndex(tmp.substring(0,2), tmp.substring(3,5));
            end_time.setSelection(end);
            place.setText(dto.getPlace());
            memo.setText(dto.getMemo());
            int index = 0;
            tmp = dto.getMission();
            if(tmp.equals("g")){
                index = 1;
            }else if(tmp.equals("p")){
                index = 2;
            }
            mission.setSelection(index);
            photo.setText(dto.getPhoto());
        }catch (Exception e){
            Log.d("sendPost == >", e.toString());

        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.edit_subplan) {
            String requestURL = "http://192.168.14.21:8805/meto/and/subplan/edit.do";

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
            Toast.makeText(getApplicationContext(),title.getText().toString(),Toast.LENGTH_SHORT).show();

            paramList.add(new BasicNameValuePair("sub_title", title.getText().toString()));
            paramList.add(new BasicNameValuePair("start_time", start_time.getSelectedItem().toString()));
            paramList.add(new BasicNameValuePair("end_time", end_time.getSelectedItem().toString()));
            paramList.add(new BasicNameValuePair("place", place.getText().toString()));
            paramList.add(new BasicNameValuePair("mission", mission_chk));
            paramList.add(new BasicNameValuePair("memo", memo.getText().toString()));
            paramList.add(new BasicNameValuePair("photo", photo.getText().toString()));
            paramList.add(new BasicNameValuePair("sub_num", String.valueOf(subNum)));
            paramList.add(new BasicNameValuePair("main_num", Integer.toString(main_num)));
            paramList.add(new BasicNameValuePair("llh_x", String.valueOf(latitude)));
            paramList.add(new BasicNameValuePair("llh_y", String.valueOf(longitude)));

            try {
                post.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
                HttpResponse response = client.execute(post);
            } catch (Exception e) {
                Log.d("sendPost == >", e.toString());
            }
            finish();

        } else if (v.getId() == R.id.edit_subplan_cancel) {
            finish();

        } else if(v.getId() == R.id.edit_subplan_del){
            String requestURL = "http://192.168.14.21:8805/meto/and/subplan/del.do";

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(requestURL);
            List<NameValuePair> paramList = new ArrayList<>();
            paramList.add(new BasicNameValuePair("subnum", String.valueOf(subNum)));
            paramList.add(new BasicNameValuePair("mainnum", Integer.toString(main_num)));

            try {
                post.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
                HttpResponse response = client.execute(post);
            } catch (Exception e) {
                Log.d("sendPost == >", e.toString());
            }

            finish();
        }else if(v.getId() == R.id.edit_subplan_place){
            Intent intent = new Intent(getApplicationContext(), SelectLocation.class);
            startActivityForResult(intent, 100);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        latitude = data.getDoubleExtra("latitude",0);
        longitude = data.getDoubleExtra("longitude",0);
        place.setText(data.getStringExtra("place"));

        Log.d("위도 경도==>",""+latitude+"/"+longitude);
    }

    public SubPlanDTO getXML(InputStream is){
        SubPlanDTO dto = new SubPlanDTO();
        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(is, "UTF-8");

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT){
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        String startTag = parser.getName();
                            if (startTag.equals("sub_num")) {
                                dto.setSub_num(Integer.parseInt(parser.nextText()));
                            } else if (startTag.equals("sub_title")) {
                                dto.setSub_title(parser.nextText());
                            } else if (startTag.equals("start_time")) {
                                dto.setStart_time(parser.nextText());
                            } else if (startTag.equals("end_time")) {
                                dto.setEnd_time(parser.nextText());
                            } else if (startTag.equals("place")) {
                                dto.setPlace(parser.nextText());
                            } else if (startTag.equals("llh_x")) {
                                dto.setLlh_x(parser.nextText());
                            } else if (startTag.equals("llh_y")) {
                                dto.setLlh_y(parser.nextText());
                            } else if (startTag.equals("mission")) {
                                dto.setMission(parser.nextText());
                            } else if (startTag.equals("memo")) {
                                dto.setMemo(parser.nextText());
                            } else if (startTag.equals("mission_yn")) {
                                dto.setMission_yn(parser.nextText());
                            } else if (startTag.equals("photo")) {
                                dto.setPhoto(parser.nextText());
                            } else if (startTag.equals("main_num")) {
                                dto.setMain_num(Integer.parseInt(parser.nextText()));
                            }
                        break;
                    case XmlPullParser.END_TAG:
                        String endTag = parser.getName();
                        break;
                }

                eventType = parser.next();
            }
        }catch (XmlPullParserException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return dto;
    }

    public int getIndex(String hour, String min){
        int index = Integer.parseInt(hour) * 2;

        if(min.equals("30")){
            index++;
        }
        return index;

    }

}
