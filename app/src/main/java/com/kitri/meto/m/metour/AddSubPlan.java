package com.kitri.meto.m.metour;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-08-08.
 */
public class AddSubPlan extends Activity implements View.OnClickListener {
    EditText title, place, memo;
    Spinner start_time, end_time, mission;
    Button ok, cancel;

   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.addsubplan);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        title = (EditText) findViewById(R.id.add_subplan_title);
        place = (EditText) findViewById(R.id.add_subplan_place);
        memo = (EditText) findViewById(R.id.add_subplan_memo);
        start_time = (Spinner) findViewById(R.id.add_subplan_starttime);
        end_time = (Spinner) findViewById(R.id.add_subplan_endtime);
        mission = (Spinner) findViewById(R.id.add_subplan_mission);

        ok = (Button) findViewById(R.id.add_subplan);
        cancel = (Button) findViewById(R.id.add_subplan_cancel);
        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_subplan) {
            String main_num = "77";
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

            paramList.add(new BasicNameValuePair("main_num", main_num));
            paramList.add(new BasicNameValuePair("sub_title", title.getText().toString()));
            paramList.add(new BasicNameValuePair("start_time", start_time.getSelectedItem().toString()));
            paramList.add(new BasicNameValuePair("end_time", end_time.getSelectedItem().toString()));
            paramList.add(new BasicNameValuePair("place", place.getText().toString()));
            paramList.add(new BasicNameValuePair("mission", mission_chk));
            paramList.add(new BasicNameValuePair("memo", memo.getText().toString()));

            try {
                post.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
                HttpResponse response = client.execute(post);
            } catch (Exception e) {
                Log.d("sendPost == >", e.toString());
            }

        } else if (v.getId() == R.id.add_subplan_cancel) {
            finish();
        }
    }
}
