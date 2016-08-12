package com.kitri.meto.m.metour;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2016-08-11.
 */

/* manifest에 추가할것
* <uses-permission android:name="android.permission.VIBRATE"></uses-permission>
* <uses-permission android:name="android.permission.WAKE_LOCK"></uses-permission>
* <receiver android:name=".BroadcastD"></receiver>
* */
public class AlarmMETO {
    private Context context;

    public AlarmMETO(Context context){
        this.context = context;
    }

    public void Alarm(List<Calendar> calendar, List<ScheduleDTO> days){
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        PendingIntent sender[] = new PendingIntent[calendar.size()];

        for(int i = 0 ; i < calendar.size(); i++) {
            Intent intent = new Intent(context, BroadcastD.class);
            intent.putExtra("title", days.get(i).getMain_title());
            intent.putExtra("main_num", days.get(i).getMain_num());
            sender[i] = PendingIntent.getBroadcast(context, days.get(i).getMain_num(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
            /*
            * MainActivity.this => PendingIntent를 부르려는 컨텍스트
              int requestCode =>Private request code 인데 현재는 사용하지 않아서 0으로
              Intent intent => 앞으로 불려질 Intent
              int flags => Intent에 대한 조건설정 플래그
                FLAG_ONE_SHOT : 한번만 사용하고 다음에 이 PendingIntent가 불려지면 fail 하겠다.
                FLAG_NO_CREATE : PendingIntent를 생성하지 않는다. PendingIntent가 실행중인것을 체크하기위함
                FLAG_CANCEL_CURRENT : 이미 실행중인 PendingIntent가 있다면 이를 취소하고 새로 만듬
                FLAG_UPDATE_CURRENT : 이미 실행중인 PendingIntent가 있다면 새로 만들지않고  extra data 만 교체하겠다.*/
        //Calendar calendar = Calendar.getInstance();
        //알람시간 calendar에 set해주기
        //calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 23, 12, 0);
        //calendar.set(2016,7,11,11,25,0);


        for(int i = 0; i < calendar.size(); i++) {
            am.cancel(sender[i]);
            am.set(AlarmManager.RTC_WAKEUP, calendar.get(i).getTimeInMillis(), sender[i]);
        }
        //알람 예약

            /*
            * RTC_WAKEUP과 RTC가 있는데요.
              AlarmManager.RTC - 실제 시간을 기준으로 합니다.
              AlarmManager.RTC_WAKEUP - RTC와 동일하며, 대기 상태일 경우 단말기를 활성 상태로 전환한 후 작업을 수행합니다.*/
    }

    public void releaseAlarm(List<Calendar> calendar){
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, BroadcastD.class);
        PendingIntent sender[] = new PendingIntent[calendar.size()];
        for(int i = 0 ; i < calendar.size(); i++) {
            sender[i] = PendingIntent.getBroadcast(context, i, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        for(int i = 0; i < calendar.size(); i++) {
            am.cancel(sender[i]);
        }
    }
}

