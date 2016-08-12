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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
 * Created by Administrator on 2016-08-11.
 */
public class MainPlanList extends Activity{




    int main_num;
    int year;
    int month;
    int day;
    int point_num;
    String title;

    int number_plan;

    CheckBox chkbox[];
    Button btn_C[];

    AlertDialog.Builder alert_chk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_plan_list);
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView Title = (TextView) findViewById(R.id.ListTitle);
        ImageView CloseBtn = (ImageView) findViewById(R.id.ListCloseBtn);
        CloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayout MiddleLayout = (LinearLayout) findViewById(R.id.ListMiddleLayout);
        LinearLayout BottomLayout = (LinearLayout) findViewById(R.id.ListBottomLayout);
        MiddleLayout.removeAllViewsInLayout();

        List<ScheduleDTO> list = new ArrayList<>();
        final Intent intent = getIntent();
        int main_writer = intent.getExtras().getInt("main_writer");
        int flag = intent.getExtras().getInt("flag");

        alert_chk = new AlertDialog.Builder(this);
        Button DelBtn = (Button) findViewById(R.id.ListDelBtn);
        Button ShareBtn = (Button) findViewById(R.id.ListShareBtn);
        DelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert_chk.setTitle("삭제확인");
                alert_chk.setMessage("선택하신 항목을 삭제하시겠습니까?");
                alert_chk.setPositiveButton("Yes",cofirm_delete);
                alert_chk.setNegativeButton("No",null);
                alert_chk.show();
            }
        });



        Title.setText("MeTo 목록");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try{
            String requestURL = "http://192.168.14.21:8805/meto/and/schedule/getList.do?main_writer="+main_writer;
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(requestURL);
            List<NameValuePair> paramList = new ArrayList<>();

            post.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));

            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();

            list = getXML(is);
            LinearLayout layouts[] = new LinearLayout[list.size()];
            chkbox = new CheckBox[list.size()];
            LinearLayout layout_A[] = new LinearLayout[list.size()];
            LinearLayout layout_B[] = new LinearLayout[list.size()];
                TextView txt_B_Top[] = new TextView[list.size()];
                LinearLayout layout_B_bottom[] = new LinearLayout[list.size()];
                    TextView txt_B_bottom_a[] = new TextView[list.size()];
                    TextView txt_B_bottom_b[] = new TextView[list.size()];
            btn_C =new Button[list.size()];


            LinearLayout.LayoutParams layouts_pos = new LinearLayout.LayoutParams(900,200);
            LinearLayout.LayoutParams layout_A_pos = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1);
            LinearLayout.LayoutParams layout_B_pos = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,6);
            LinearLayout.LayoutParams layout_B_in_pos = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0,1);
            // LinearLayout.LayoutParams txt_B_Top_pos = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0,1);
           // LinearLayout.LayoutParams lay_B_bottom_pos = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0,1);
            LinearLayout.LayoutParams txt_B_bottom_a_pos = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,3);
            LinearLayout.LayoutParams txt_B_bottom_b_pos = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,2);
            LinearLayout.LayoutParams btn_C_pos = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1);

            layouts_pos.setMargins(30,30,30,0);
            layout_A_pos.setMargins(6,6,3,6);
            layout_B_pos.setMargins(3,6,3,6);
            btn_C_pos.setMargins(3,6,6,6);

            number_plan = list.size();


           for (int i=0;i<list.size();i++){
               layouts[i] = new LinearLayout(this);
               layouts[i].setOrientation(LinearLayout.HORIZONTAL);
               chkbox[i]=new CheckBox(this);
               chkbox[i].setBackgroundColor(Color.rgb(255,255,255));
               chkbox[i].setTag(main_num);

               layout_A[i] = new LinearLayout(this);
               layout_A[i].setGravity(Gravity.CENTER);
               layout_B[i] = new LinearLayout(this);
                    layout_B[i].setOrientation(LinearLayout.VERTICAL);
                    txt_B_Top[i]=new TextView(this);
                    layout_B_bottom[i]=new LinearLayout(this);
                        layout_B_bottom[i].setOrientation(LinearLayout.HORIZONTAL);
                        txt_B_bottom_a[i]=new TextView(this);
                        txt_B_bottom_b[i]=new TextView(this);
               btn_C[i]=new Button(this);
               btn_C[i].setText("Go");

               layouts[i].setBackgroundColor(Color.rgb(26, 188, 156));
               layout_A[i].setBackgroundColor(Color.rgb(255,255,255));
               layout_B[i].setBackgroundColor(Color.rgb(255,255,255));
               btn_C[i].setBackgroundColor(Color.rgb(255,255,255));


               main_num = list.get(i).getMain_num();
               year = list.get(i).getYear();
               month = list.get(i).getMonth();
               day = list.get(i).getDay();
               point_num = list.get(i).getPoint_num();
               title = list.get(i).getMain_title();
               btn_C[i].setTag(main_num);

               txt_B_Top[i].setText(title);
               txt_B_bottom_a[i].setText(year+"/"+month+"/"+day);

               txt_B_bottom_b[i].setText("Point: "+ point_num);

               layout_B_bottom[i].addView(txt_B_bottom_a[i],txt_B_bottom_a_pos);
               layout_B_bottom[i].addView(txt_B_bottom_b[i],txt_B_bottom_b_pos);
               layout_B[i].addView(txt_B_Top[i],layout_B_in_pos);
               layout_B[i].addView(layout_B_bottom[i],layout_B_in_pos);

               layouts[i].addView(chkbox[i],layout_A_pos);
               layouts[i].addView(layout_B[i],layout_B_pos);
               layouts[i].addView(btn_C[i],btn_C_pos);

               btn_C[i].setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Intent intent_new = new Intent(MainPlanList.this,ListSubplan.class);
                       intent_new.putExtra("main_num",Integer.parseInt(v.getTag().toString()));
                       startActivity(intent_new);

                   }
               });

               MiddleLayout.addView(layouts[i],layouts_pos);
            }


        }catch (Exception e) {
            Log.d("sendPost===> ", e.toString());
        }
    }

    public List<ScheduleDTO> getXML(InputStream is) {
        List<ScheduleDTO> list = new ArrayList<>();

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(is, "UTF-8");

            int eventType = parser.getEventType();

            ScheduleDTO dto = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {

                switch (eventType) {
                    case XmlPullParser.START_TAG:

                        String startTag = parser.getName();

                        if (startTag.equals("schedule")) {
                            dto = new ScheduleDTO();
                        }
                        if (dto != null) {
                            if (startTag.equals("main_num")) {
                                dto.setMain_num(Integer.parseInt(parser.nextText()));
                            } else if (startTag.equals("year")) {
                                dto.setYear(Integer.parseInt(parser.nextText()));
                            } else if (startTag.equals("month")) {
                                dto.setMonth(Integer.parseInt(parser.nextText()));
                            } else if (startTag.equals("day")) {
                                dto.setDay(Integer.parseInt(parser.nextText()));
                            } else if (startTag.equals("main_title")) {
                                dto.setMain_title(parser.nextText());
                            } else if (startTag.equals("point_num")) {
                                dto.setPoint_num(Integer.parseInt(parser.nextText()));
                            }
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        String endTag = parser.getName();
                        if (endTag.equals("schedule")) {
                            list.add(dto);
                        }
                        break;
                }
                eventType = parser.next();
            } // end of while
        } catch (Exception e) {
            Log.d("SelectActivityError", e.toString());
        } // end of try
        return list;

    }



    DialogInterface.OnClickListener cofirm_delete = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            for(int i =0;i<number_plan;i++){
                if(chkbox[i].isChecked()){
                    int m_num = Integer.parseInt(chkbox[i].getTag().toString());
                    DeleteSchedule(m_num);

                }
            }
            finish();
        }
    };

    public void DeleteSchedule(int main_num){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try{
            String requestURL = "http://192.168.14.21:8805/meto/and/schedule/deleteMainSchedule.do?main_num="+main_num;
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(requestURL);
            List<NameValuePair> paramList = new ArrayList<>();

            post.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();


        }catch (Exception e) {
            Log.d("sendPost===> ", e.toString());
        }
    }
}
