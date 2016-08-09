package com.kitri.meto.m.metour;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2016-08-08.
 */
public class CreateCalendar extends Activity implements View.OnClickListener {

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

    int now_y;
    int now_m;
    int now_d;

    int year;
    int month;
    int dow;

    int week_num;
    int day;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);


        MonthView = (TextView) findViewById(R.id.calendarMonth);
        for (int i = 0; i < calendar_box.length; i++) {
            calView[i] = (TextView) findViewById(calendar_box[i]);
        }

        TextView LastMonth = (TextView) findViewById(R.id.calLastMonth);
        LastMonth.setOnClickListener(this);
        TextView NextMonth = (TextView) findViewById(R.id.calNextMonth);
        NextMonth.setOnClickListener(this);




        now_y = now.get(Calendar.YEAR);
        now_m = now.get(Calendar.MONTH);
        now_d = now.get(Calendar.DAY_OF_MONTH);

        cal.set(now_y, now_m, 1);
        dow = cal.get(Calendar.DAY_OF_WEEK);

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

        for (int index = 0; index < calendar_box.length; index++) {
            if (week_num < dow) {
                week_num += 1;
                calView[index].setBackgroundColor(Color.rgb(223, 220, 216));
                calView[index].setText("");
            } else {
                if (isDate(Y, M, day)) {
                    //날짜 존재
                    if (Y == now_y && M == now_m && day == now_d) {
                        calView[index].setText(Integer.toString(day));
                        calView[index].setBackgroundColor(Color.rgb(242, 76, 39));
                        calView[index].setTag(1);
                        calView[index].setTextColor(Color.rgb(255, 255, 255));
                        // Today
                    } else {
                        calView[index].setText(Integer.toString(day));
                        calView[index].setBackgroundColor(Color.rgb(255, 255, 249));
                        calView[index].setTextColor(Color.rgb(40, 40, 50));
                        calView[index].setTag(0);
                        // Not Today
                    }

                    calView[index].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            TextView Daytxt = (TextView) findViewById(v.getId());
                            int Year = cal.get(Calendar.YEAR);
                            int Month = cal.get(Calendar.MONTH)+1;
                            int Day =  Integer.parseInt(Daytxt.getText().toString());
                            int flag = Integer.parseInt(v.getTag().toString());

                            String MSG = Year + "년 " + Month + "월" + Day +"일";

                            Toast.makeText(getApplicationContext(),MSG, Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(CreateCalendar.this, MainPlanAnd.class);

                            intent.putExtra("year",Year);
                            intent.putExtra("month",Month);
                            intent.putExtra("day",Day);
                            intent.putExtra("flag",flag);

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


}
