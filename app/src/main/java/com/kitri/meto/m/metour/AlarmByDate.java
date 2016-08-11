package com.kitri.meto.m.metour;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2016-08-11.
 */
public class AlarmByDate extends Activity {
    private  static int ONE_MINUTE = 5626;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        /*Calendar calendar = Calendar.getInstance();
        //알람시간 set해주기
        calendar.set(2016,7,11,14,35,0); //year, month(1빼줘야해), day, hour(24시간), minute, second
        new AlarmMETO(getApplicationContext()).Alarm(calendar);

        */
        /*Calendar calendar[] = new Calendar[3];
        int second = 54;
        for(int i = 0; i < 3; i++){
            calendar[i] = Calendar.getInstance();
            //알람시간 set해주기
            calendar[i].set(2016,7,11,14,second,0); //year, month(1빼줘야해), day, hour(24시간), minute, second
            second+=1;
        }*/


        List<Calendar> calendar = new ArrayList<>();
        int second = 27;
        for(int i = 0; i < 3; i++){
            Calendar tmp = Calendar.getInstance();
            //알람시간 set해주기
            tmp.set(2016,7,11,16,second,0); //year, month(1빼줘야해), day, hour(24시간), minute, second
            second+=1;

            calendar.add(tmp);
        }

        new AlarmMETO(getApplicationContext()).Alarm(calendar);
    }
}
