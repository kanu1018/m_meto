package com.kitri.meto.m.metour;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016-08-08.
 */
public class CreateCalendar extends Activity implements View.OnClickListener {

    List<ScheduleDTO> list;

    int calendar_box[] = {R.id.calendar00, R.id.calendar01, R.id.calendar02, R.id.calendar03, R.id.calendar04, R.id.calendar05, R.id.calendar06,
            R.id.calendar10, R.id.calendar11, R.id.calendar12, R.id.calendar13, R.id.calendar14, R.id.calendar15, R.id.calendar16,
            R.id.calendar20, R.id.calendar21, R.id.calendar22, R.id.calendar23, R.id.calendar24, R.id.calendar25, R.id.calendar26,
            R.id.calendar30, R.id.calendar31, R.id.calendar32, R.id.calendar33, R.id.calendar34, R.id.calendar35, R.id.calendar36,
            R.id.calendar40, R.id.calendar41, R.id.calendar42, R.id.calendar43, R.id.calendar44, R.id.calendar45, R.id.calendar46,
            R.id.calendar50, R.id.calendar51, R.id.calendar52, R.id.calendar53, R.id.calendar54, R.id.calendar55, R.id.calendar56};

    TextView calView[] = new TextView[calendar_box.length];
    TextView MonthView;

    Calendar now = Calendar.getInstance();
    Calendar cal = Calendar.getInstance();

    TextView DelBtn;
    TextView ListBtn;
    TextView ShareBtn;

    int now_y;
    int now_m;
    int now_d;

    int year;
    int month;
    int dow;

    int week_num;
    int day;

    int main_writer;

    Intent intent_get;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
    }

    @Override
    protected void onResume() {
        super.onResume();

        intent_get = getIntent();
        main_writer = intent_get.getExtras().getInt("main_writer",0);


        MonthView = (TextView) findViewById(R.id.calendarMonth);
        for (int i = 0; i < calendar_box.length; i++) {
            calView[i] = (TextView) findViewById(calendar_box[i]);
        }

        TextView LastMonth = (TextView) findViewById(R.id.calLastMonth);
        LastMonth.setOnClickListener(this);
        TextView NextMonth = (TextView) findViewById(R.id.calNextMonth);
        NextMonth.setOnClickListener(this);

        ImageView closeBtn = (ImageView) findViewById(R.id.calendarCloseBtn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        ListBtn = (TextView) findViewById(R.id.calendarListBtn);
        ListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateCalendar.this, MainPlanList.class);
                intent.putExtra("main_writer", main_writer);
                intent.putExtra("flag",2);
                startActivity(intent);
            }
        });


        now_y = now.get(Calendar.YEAR);
        now_m = now.get(Calendar.MONTH);
        now_d = now.get(Calendar.DAY_OF_MONTH);
        cal.set(now_y, now_m, 1);
        dow = cal.get(Calendar.DAY_OF_WEEK);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        CalCalendar(now_y, now_m, dow);

    }



    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.calLastMonth) {
            cal.add(Calendar.MONTH, -1);
            month = cal.get(Calendar.MONTH);
            year = cal.get(Calendar.YEAR);
            dow = cal.get(Calendar.DAY_OF_WEEK);
            CalCalendar(year, month, dow);
        } else if (v.getId() == R.id.calNextMonth) {
            cal.add(Calendar.MONTH, 1);
            month = cal.get(Calendar.MONTH);
            year = cal.get(Calendar.YEAR);
            dow = cal.get(Calendar.DAY_OF_WEEK);
            CalCalendar(year, month, dow);
        }
    }

    public void CalCalendar(int Y, int M, int dow) {
        String msg = Y + "년 " + (M + 1) + "월";

        MonthView.setText(msg);

        week_num = 1;
        day = 1;

        boolean flag_reserved =false;
        boolean flag_today =false;

        try {

            String requestURL = "http://192.168.14.21:8805/meto/and/schedule/getList.do?main_writer="+main_writer;
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(requestURL);
            List<NameValuePair> paramList = new ArrayList<>();

            post.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));

            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();

            list = getXML(is);

            for (int index = 0; index < calendar_box.length; index++) {
                if (week_num < dow) {
                    week_num += 1;
                    calView[index].setBackgroundColor(Color.rgb(223, 220, 216));
                    calView[index].setText("");
                } else {
                    if (isDate(Y, M, day)) {

                        //날짜 존재
                        if (Y == now_y && M == now_m && day == now_d) {
                            calView[index].setBackgroundColor(Color.rgb(242, 76, 39));
                            flag_today = true;
                            calView[index].setTag(0);
                            calView[index].setTextColor(Color.rgb(255, 255, 255));
                            // Today
                        } else {
                            // Not Today
                            calView[index].setBackgroundColor(Color.rgb(255, 255, 249));
                            calView[index].setTextColor(Color.rgb(40, 40, 50));
                            flag_today =false;
                            calView[index].setTag(0);
                        }
                        calView[index].setText(Integer.toString(day));

                        for (int k = 0; k < list.size(); k++) {
                            if (Y == list.get(k).getYear() && (M + 1) == list.get(k).getMonth() && day == list.get(k).getDay()) {
                                calView[index].setTag(list.get(k).getMain_num());
                                flag_reserved = true;
                                break;
                            } else {
                                flag_reserved = false;
                            }

                        }

                        //Tag 값 지정 및 색깔 확정
                        if(flag_today&&flag_reserved){
                            //미투데이
                            calView[index].setBackgroundColor(Color.rgb(51,48,140));
                            calView[index].setTextColor(Color.rgb(255, 255, 255));

                        }else if(flag_reserved&&!flag_today){
                            //미투
                            calView[index].setBackgroundColor(Color.rgb(26, 188, 156));
                            calView[index].setTextColor(Color.rgb(255, 255, 255));
                        }


                        calView[index].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                TextView Daytxt = (TextView) findViewById(v.getId());
                                int Year = cal.get(Calendar.YEAR);
                                int Month = cal.get(Calendar.MONTH) + 1;
                                int Day = Integer.parseInt(Daytxt.getText().toString());
                                int main_num = Integer.parseInt(v.getTag().toString());

                                Intent intent = new Intent(CreateCalendar.this, MainPlanAnd.class);

                                intent.putExtra("year", Year);
                                intent.putExtra("month", Month);
                                intent.putExtra("day", Day);
                                intent.putExtra("main_num", main_num);
                                intent.putExtra("main_writer", main_writer);

                                startActivity(intent);

                            }
                        });
                        day += 1;
                    } else {
                        //날짜 존재x
                        calView[index].setBackgroundColor(Color.rgb(223, 220, 216));
                        calView[index].setText("");
                    }
                }
            }




        }catch (Exception e) {
          Log.d("sendPost===> ", e.toString());
        }


    }

 /*   private void initiatePopupWindow(View v) {
        try {

            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = inflater.inflate(R.layout.dateplan, null);

            PopupWindow popupWindow = new PopupWindow(v, 800, ViewGroup.LayoutParams.WRAP_CONTENT,true);
            popupWindow.setContentView(view);
            popupWindow.showAtLocation(v,Gravity.CENTER,0,0);

            TextView datePlanDate = (TextView) findViewById(R.id.datePlanDate);
            ImageView datePlanImgBtn = (ImageView) findViewById(R.id.datePlanAddBtn);
            datePlanImgBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(),"xxxxx",Toast.LENGTH_SHORT).show();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    private boolean isDate(int y, int m, int d) {
        Calendar c = Calendar.getInstance();
        c.setLenient(false);
        try {
            c.set(y, m, d);
            Date dt = c.getTime();
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }


    public List<ScheduleDTO> getXML(InputStream is) {
        List<ScheduleDTO> list = new ArrayList<>();

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(is, "UTF-8");

            int eventType = parser.getEventType();

            ScheduleDTO dto = null;
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
                            }else if (startTag.equals("year")) {
                                dto.setYear(Integer.parseInt(parser.nextText()));
                            } else if (startTag.equals("month")) {
                                dto.setMonth(Integer.parseInt(parser.nextText()));
                            } else if (startTag.equals("day")) {
                                dto.setDay(Integer.parseInt(parser.nextText()));
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
        } catch(Exception e) {
            Log.d("SelectActivityError",e.toString());
        } // end of try
        return list;
    }

}
