package com.kitri.meto.m.metour;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-08-09.
 */
public class MainPlanAnd extends Activity {

    int IMGS[] = {R.drawable.adda, R.drawable.addb};
    ImageView imgBtn[] = new ImageView[IMGS.length];
    int main_num;
    int main_writer;
    int year;
    int month;
    int day;
    int point_num;
    String main_title;
    EditText editText;

    AlertDialog.Builder alert_delete;
    AlertDialog.Builder alert_add;
    AlertDialog.Builder alert_move;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dateplan);

        final Intent intent = getIntent();
        year = intent.getExtras().getInt("year",0);
        month = intent.getExtras().getInt("month",0);
        day = intent.getExtras().getInt("day",0);
        main_num = intent.getExtras().getInt("main_num",0);
        int AddFlag = intent.getExtras().getInt("AddFlag",0);
        Log.d("AAAAAAAAAAAAAAAA",Integer.toString(AddFlag));

        String MSG = year + "년 " + month + "월" + day +"일";

        TextView txtView = (TextView) findViewById(R.id.datePlanDate);
        txtView.setText(MSG);
        LinearLayout datePlanLayer = (LinearLayout) findViewById(R.id.datePlanLayer);

        ImageView closeBtn = (ImageView) findViewById(R.id.datePlanCloseBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        if (main_num==0){
            if(AddFlag==0){
                FrameLayout frameLayout = new FrameLayout(this);
                FrameLayout.LayoutParams imgbtn_pos = new FrameLayout.LayoutParams(300,300);
                imgbtn_pos.gravity = Gravity.CENTER;
                FrameLayout.LayoutParams frame_pos = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                for (int i=0;i<IMGS.length;i++){
                    imgBtn[i] = new ImageView(this);
                    imgBtn[i].setImageResource(IMGS[i]);
                    frameLayout.addView(imgBtn[i],imgbtn_pos);
                }
                imgBtn[0].setVisibility(View.INVISIBLE);

                datePlanLayer.addView(frameLayout, frame_pos);

                imgBtn[1].setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if(event.getAction() == MotionEvent.ACTION_DOWN){
                            imgBtn[1].setVisibility(View.INVISIBLE);
                            imgBtn[0].setVisibility(View.VISIBLE);
                        }else if(event.getAction() == MotionEvent.ACTION_UP){
                            imgBtn[1].setVisibility(View.VISIBLE);
                            imgBtn[0].setVisibility(View.INVISIBLE);
                            intent.putExtra("AddFlag",1);
                            onResume();
                        }
                        return true;
                    }
                });
            }

        }else{
            LinearLayout Layout_Main = new LinearLayout(this);
            LinearLayout.LayoutParams main_pos = new LinearLayout.LayoutParams(800,500);
            Layout_Main.setOrientation(LinearLayout.VERTICAL);
            Layout_Main.setBackgroundColor(Color.rgb(26, 188, 156));

            LinearLayout Layout_Head = new LinearLayout(this);
            LinearLayout.LayoutParams head_pos = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,1);
            Layout_Head.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout layout_head_1 = new LinearLayout(this);
            LinearLayout.LayoutParams head_pos1 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1);
            ImageView head_img = new ImageView(this);
            LinearLayout.LayoutParams img_pos = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            img_pos.gravity=Gravity.RIGHT;
            head_img.setImageResource(R.drawable.logo_ws);
            head_img.setPadding(5,0,0,0);


            layout_head_1.addView(head_img,img_pos);

            LinearLayout layout_head_2 = new LinearLayout(this);
            LinearLayout.LayoutParams head_pos2 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,4);
            head_pos2.gravity=Gravity.CENTER_VERTICAL;

            TextView head_txt = new TextView(this);
            LinearLayout.LayoutParams head_txt_pos = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            head_txt.setText("Plan");
            head_txt.setPadding(5,0,0,0);
            head_txt.setTextColor(Color.rgb(255,255,255));
            head_txt.setTextSize(30);
            layout_head_2.addView(head_txt,head_txt_pos);



            TextView point_txt = new TextView(this);
            LinearLayout.LayoutParams head_pos3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            head_pos3.gravity=Gravity.RIGHT|Gravity.CENTER_VERTICAL;
            point_txt.setTextColor(Color.rgb(255,255,255));
            point_txt.setTextSize(40);
            point_txt.setPadding(0,0,10,0);


            Layout_Head.addView(layout_head_1,head_pos1);
            Layout_Head.addView(layout_head_2,head_pos2);
            Layout_Head.addView(point_txt,head_pos3);

            Layout_Main.addView(Layout_Head,head_pos);
            datePlanLayer.addView(Layout_Main, main_pos);

            TextView title_text = new TextView(this);
            LinearLayout.LayoutParams title_pos = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0,2);
            title_text.setGravity(Gravity.CENTER);
            title_text.setTextColor(Color.rgb(40,40,50));
            title_text.setBackgroundColor(Color.rgb(255,255,255));
            title_pos.setMargins(10,0,10,0);
            title_text.setTextSize(40);
            Layout_Main.addView(title_text,title_pos);




            Button btnDelete = new Button(this);
            Button btnDetail = new Button(this);

            LinearLayout btnsLayout = new LinearLayout(this);
            LinearLayout.LayoutParams btnsLayout_pos = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0,1);
            btnsLayout.setOrientation(LinearLayout.HORIZONTAL);
            btnsLayout.setBackgroundColor(Color.rgb(255,255,255));
            btnsLayout_pos.setMargins(10,0,10,10);

            LinearLayout.LayoutParams btns_pos1 = new LinearLayout.LayoutParams(0,  ViewGroup.LayoutParams.MATCH_PARENT,1);
            LinearLayout.LayoutParams btns_pos2 = new LinearLayout.LayoutParams(0,  ViewGroup.LayoutParams.MATCH_PARENT,1);


            alert_delete = new AlertDialog.Builder(this);
            alert_delete.setTitle("삭제 확인");
            alert_delete.setMessage("삭제 하시겠습니까?");
            alert_delete.setPositiveButton("삭제",confirm_delete);
            alert_delete.setNegativeButton("취소",null);



            btnDelete.setText("계획삭제");
            btnDetail.setText("세부계획");

            btnDelete.setBackgroundColor(Color.rgb(26, 188, 156));
            btnDetail.setBackgroundColor(Color.rgb(26, 188, 156));
            btnDelete.setTextColor(Color.rgb(255,255,255));
            btnDetail.setTextColor(Color.rgb(255,255,255));
            btns_pos1.setMargins(6,6,3,6);
            btns_pos2.setMargins(3,6,6,6);

            Layout_Main.addView(btnsLayout,btnsLayout_pos);
            btnsLayout.addView(btnDetail,btns_pos1);
            btnsLayout.addView(btnDelete,btns_pos2);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert_delete.show();
                }
            });

            try {

                ScheduleDTO scheduleDTO;
                String requestURL = "http://192.168.14.21:8805/meto/and/schedule/getMainSchedule.do?main_num="+main_num;
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(requestURL);
                List<NameValuePair> paramList = new ArrayList<>();

                post.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));

                HttpResponse response = client.execute(post);
                HttpEntity entity = response.getEntity();
                InputStream is = entity.getContent();

                scheduleDTO = getXML(is);
                title_text.setText(scheduleDTO.getMain_title());
                point_num = scheduleDTO.getPoint_num();
                point_txt.setText(Integer.toString(point_num));

            }catch (Exception e) {
                Log.d("sendPost===> ", e.toString());
            }




        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        final Intent intent = getIntent();

        int AddFlag = intent.getExtras().getInt("AddFlag",0);
        main_writer = intent.getExtras().getInt("main_writer", 0);

        if (AddFlag==1){
            final LinearLayout datePlanLayer = (LinearLayout) findViewById(R.id.datePlanLayer);
            datePlanLayer.removeAllViewsInLayout();

            LinearLayout Layout_Main = new LinearLayout(this);
            LinearLayout.LayoutParams main_pos = new LinearLayout.LayoutParams(800,500);
            Layout_Main.setOrientation(LinearLayout.VERTICAL);
            Layout_Main.setBackgroundColor(Color.rgb(26, 188, 156));


            LinearLayout Layout_Head = new LinearLayout(this);
            LinearLayout.LayoutParams head_pos = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0,1);
            Layout_Head.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout layout_head_1 = new LinearLayout(this);
            LinearLayout.LayoutParams head_pos1 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1);
            ImageView head_img = new ImageView(this);
            LinearLayout.LayoutParams img_pos = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            img_pos.gravity=Gravity.RIGHT;
            head_img.setImageResource(R.drawable.logo_ws);
            head_img.setPadding(5,0,0,0);

            layout_head_1.addView(head_img,img_pos);

            LinearLayout layout_head_2 = new LinearLayout(this);
            LinearLayout.LayoutParams head_pos2 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,5);
            head_pos2.gravity=Gravity.CENTER_VERTICAL;

            TextView head_txt = new TextView(this);
            LinearLayout.LayoutParams head_txt_pos = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            head_txt.setText("Plan");
            head_txt.setPadding(5,0,0,0);
            head_txt.setTextColor(Color.rgb(255,255,255));
            head_txt.setTextSize(30);
            layout_head_2.addView(head_txt,head_txt_pos);

            Layout_Head.addView(layout_head_1,head_pos1);
            Layout_Head.addView(layout_head_2,head_pos2);

            Layout_Main.addView(Layout_Head,head_pos);
            datePlanLayer.addView(Layout_Main, main_pos);


            editText = new EditText(this);
            LinearLayout.LayoutParams title_pos = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0,2);
            editText.setGravity(Gravity.CENTER);
            editText.setTextColor(Color.rgb(40,40,50));
            editText.setBackgroundColor(Color.rgb(255,255,255));
            title_pos.setMargins(10,0,10,0);
            editText.setTextSize(25);
            editText.setHint("MeTo 제목을 입력해주세요");
            Layout_Main.addView(editText,title_pos);


            Button btnAdd = new Button(this);

            LinearLayout btnsLayout = new LinearLayout(this);
            LinearLayout.LayoutParams btnsLayout_pos = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0,1);
            btnsLayout.setOrientation(LinearLayout.HORIZONTAL);
            btnsLayout.setBackgroundColor(Color.rgb(255,255,255));
            btnsLayout_pos.setMargins(10,0,10,10);

            LinearLayout.LayoutParams btns_pos = new LinearLayout.LayoutParams(0,  ViewGroup.LayoutParams.MATCH_PARENT,1);

            alert_add = new AlertDialog.Builder(this);
            alert_add.setTitle("등록 확인");
            alert_add.setMessage("등록 하시겠습니까?");
            alert_add.setNegativeButton("취소",null);
            alert_add.setPositiveButton("등록",confirm_add);

            alert_move = new AlertDialog.Builder(this);
            alert_move.setTitle("세부 계획으로 이동");
            alert_move.setMessage("세부계획으로 이동하시겠습니까?");
            alert_move.setNegativeButton("이동x",confim_not_move);
            alert_move.setPositiveButton("이동",null);


            btnAdd.setText("계획등록");

            btnAdd.setBackgroundColor(Color.rgb(26, 188, 156));

            btnAdd.setTextColor(Color.rgb(255,255,255));
            btns_pos.setMargins(6,6,6,6);

            Layout_Main.addView(btnsLayout,btnsLayout_pos);
            btnsLayout.addView(btnAdd,btns_pos);



            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert_add.show();
                    intent.putExtra("AddFlag",0);
                }
            });

        }
        Log.d("AAAAAAAAAAAAAAAA",Integer.toString(AddFlag));
    }

    public void DeleteSchedule(int main_num){
        try{
            String requestURL = "http://192.168.14.21:8805/meto/and/schedule/deleteMainSchedule.do?main_num="+main_num;
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(requestURL);
            List<NameValuePair> paramList = new ArrayList<>();

            post.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));


        }catch (Exception e) {
            Log.d("sendPost===> ", e.toString());
        }
    }

    public void InsertSchedule(int main_writer, int year, int month,int day, String main_title){
        try{
            String requestURL = "http://192.168.14.21:8805/meto/and/schedule/insertMainSchedule.do?main_writer="+main_writer+"&year="+year+"&month="+month+"&day="+day+"&main_title="+main_title;
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(requestURL);
            List<NameValuePair> paramList = new ArrayList<>();

            post.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));


        }catch (Exception e) {
            Log.d("sendPost===> ", e.toString());
        }
    }





    public ScheduleDTO getXML(InputStream is) {
        ScheduleDTO dto = null;

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(is, "UTF-8");

            int eventType = parser.getEventType();

            while(eventType != XmlPullParser.END_DOCUMENT) {

                switch (eventType) {
                    case XmlPullParser.START_TAG :
                        String startTag = parser.getName();
                        if (startTag.equals("schedule")) {
                            dto = new ScheduleDTO();
                        }
                        if (dto != null) {
                            if (startTag.equals("main_num")) {
                                dto.setMain_num(Integer.parseInt(parser.nextText()));
                            }else if (startTag.equals("point_num")) {
                                dto.setPoint_num(Integer.parseInt(parser.nextText()));
                            }else if (startTag.equals("main_title")){
                                dto.setMain_title(parser.nextText());
                            }
                        }
                        break;
                }
                eventType = parser.next();
            } // end of while
        } catch(Exception e) {
            Log.d("SelectActivityError",e.toString());
        } // end of try
        return dto;
    }

    DialogInterface.OnClickListener confirm_delete = new DialogInterface.OnClickListener(){
        @Override
        public void onClick(DialogInterface dialog, int which) {
            DeleteSchedule(main_num);
            finish();
        }
    };
    DialogInterface.OnClickListener confirm_add = new DialogInterface.OnClickListener(){
        @Override
        public void onClick(DialogInterface dialog, int which) {
            main_title = editText.getText().toString();
            Toast.makeText(getApplication(),main_title,Toast.LENGTH_SHORT).show();
            InsertSchedule(main_writer,year,month,day,main_title);
            alert_move.show();
        }
    };

    DialogInterface.OnClickListener confirm_move = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

        }
    };
    DialogInterface.OnClickListener confim_not_move = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            finish();
        }
    };

}
