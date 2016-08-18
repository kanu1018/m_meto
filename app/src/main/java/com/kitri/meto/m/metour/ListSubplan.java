package com.kitri.meto.m.metour;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class ListSubplan extends Activity {
    List<SubPlanListDTO> list;
    LinearLayout listSubplan;
    LinearLayout listSubplan_total;

    LinearLayout layoutSub[];

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listsubplan);
    }

    @Override
    protected void onResume() {
        super.onResume();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String requestURL = "http://192.168.14.45:8805/meto/and/subplan/list.do";

        listSubplan = (LinearLayout)findViewById(R.id.list_subplan);
        listSubplan.removeAllViewsInLayout();
        listSubplan_total = (LinearLayout)findViewById(R.id.list_subplan_total);

        intent = getIntent();
        final int mainNum = intent.getExtras().getInt("main_num");

        try{
            HttpClient client = new DefaultHttpClient();
            //// TODO: 세션유지
            //HttpClient client = SessionControl.getHttpclient();
            HttpPost post = new HttpPost(requestURL);
            List<NameValuePair> paramList = new ArrayList<>();
            paramList.add(new BasicNameValuePair("main_num", Integer.toString(mainNum)));

            post.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));

            HttpResponse response = client.execute(post);
            final HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            list = getXML(is);

            layoutSub = new LinearLayout[list.size()];

            SubPlanListDTO dto;

            for(int i = 0 ; i < list.size(); i++){
                dto = list.get(i);
                layoutSub[i] = new LinearLayout(this);
                layoutSub[i].setOrientation(LinearLayout.HORIZONTAL);

                final TextView txtName[] = new TextView[5];
                for(int j = 0; j < txtName.length; j++){
                    txtName[j] = new TextView(this);
                    txtName[j].setTextColor(Color.BLACK);
                    txtName[j].setGravity(Gravity.CENTER);
                }

                txtName[0].setText(dto.getTime());
                txtName[1].setText(dto.getTitle());
                txtName[2].setText(dto.getPlace());
                String m = "";
                if(dto.getMission().equals("n")){
                    m = "미션\n선택안함";
                }else if(dto.getMission().equals("g")){
                    m = "명소\n찾아가기";
                }else if(dto.getMission().equals("p")){
                    m = "명소\n사진찍기";
                }
                txtName[3].setText(m);

                if(dto.getMission_yn().equals("0")){
                    m = "";
                }else if(dto.getMission().equals("1")){
                    m = "사진 완료\nGPS 실패";
                }else if(dto.getMission().equals("2") || dto.getMission().equals("3")){
                    m = "성공";
                }
                txtName[4].setText(m);

                txtName[0].setWidth(130);
                txtName[0].setPadding(0,0,0,0); //(left, top, right, bottom);
                txtName[1].setWidth(200);
                txtName[2].setWidth(200);
                txtName[3].setWidth(200);
                txtName[4].setWidth(200);


                for(int j = 0; j < txtName.length; j++){
                    layoutSub[i].addView(txtName[j]);
                }

                layoutSub[i].setTag(dto.getSub_num());
                layoutSub[i].setMinimumHeight(150);

                layoutSub[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int subNum = Integer.parseInt(v.getTag().toString());
                        if(subNum == 0){
                            Intent intent = new Intent(getApplicationContext(), AddSubPlan.class);
                            intent.putExtra("main_num",mainNum);
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(getApplicationContext(), EditSubPlan.class);
                            intent.putExtra("subNum", subNum);
                            intent.putExtra("main_num",mainNum);
                            startActivity(intent);
                        }
                    }
                });

                listSubplan.addView(layoutSub[i]);

            }
        } catch (Exception e){
            Log.d("sendPost===> ", e.toString());
        }


        for(int i = 0; i < layoutSub.length; i++){
            final int subNum = Integer.parseInt(layoutSub[i].getTag().toString());
            if(subNum != 0){
                layoutSub[i].setBackgroundResource(R.drawable.line);
                Button photo = new Button(this);
                photo.setText("사진");
                photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getApplicationContext(),CameraActivity.class);
                        intent.putExtra("sub_num",String.valueOf(subNum));
                        startActivity(intent);
                    }
                });
                layoutSub[i].addView(photo);
            }
        }
    }

    public List<SubPlanListDTO> getXML(InputStream is){
        List<SubPlanListDTO> list= new ArrayList<>();
        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(is, "UTF-8");

            int eventType = parser.getEventType();

            SubPlanListDTO dto = null;

            while(eventType != XmlPullParser.END_DOCUMENT){
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        String startTag = parser.getName();
                        if(startTag.equals("subplan")){
                            dto = new SubPlanListDTO();
                        }
                        if(dto != null){
                            if(startTag.equals("time")){
                                dto.setTime(parser.nextText());
                            }else if(startTag.equals("sub_num")){
                                dto.setSub_num(Integer.parseInt(parser.nextText()));
                            }else if(startTag.equals("title")){
                                dto.setTitle(parser.nextText());
                            }else if(startTag.equals("place")){
                                dto.setPlace(parser.nextText());
                            }else if(startTag.equals("mission")){
                                dto.setMission(parser.nextText());
                            }else if(startTag.equals("mission_yn")){
                                dto.setMission_yn(parser.nextText());
                            }else if(startTag.equals("row")){
                                dto.setRow(Integer.parseInt(parser.nextText()));
                            }
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        String endTag = parser.getName();
                        if(endTag.equals("subplan")){
                            list.add(dto);
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
