package com.kitri.meto.m.metour;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2016-08-11.
 */
public class BroadcastD extends BroadcastReceiver{
    String INTENT_ACTION = Intent.ACTION_BOOT_COMPLETED;

    @Override
    public void onReceive(Context context, Intent intent) {//알람 시간이 되었을때 onReceive를 호출
        //NotificationManager 안드로이드 상태바에 메세지를 던지기 위한 서비스를 불러오고
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, intent.getIntExtra("main_num", 0), new Intent(context,BottomBtn.class ), PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.drawable.large_logo).setTicker("ME TOUR").setWhen(System.currentTimeMillis())
                .setNumber(1).setContentTitle("Me,tour"+" "+intent.getStringExtra("title")).setContentText("여행 시작하십니까?") //contentText는 알림마다 메세지를 다르게 줘야함
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE).setContentIntent(pendingIntent).setAutoCancel(true);

        //// TODO: 2016-08-12 알림 와서 버튼 누를때 액션 수정해야할곳
        Intent intent_ok = new Intent(context,ListSubplan.class);
        intent_ok.putExtra("main_num", intent.getIntExtra("main_num", 0));
        PendingIntent OKpendingIntent = PendingIntent.getActivity(context, intent.getIntExtra("main_num", 0), intent_ok, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent NOpendingIntent = PendingIntent.getActivity(context, intent.getIntExtra("main_num", 0), new Intent(context,MemLog.class ), PendingIntent.FLAG_UPDATE_CURRENT);

        builder.addAction(R.mipmap.ic_launcher, "Yes!", OKpendingIntent);
        builder.addAction(R.mipmap.ic_launcher, "No!", NOpendingIntent);

        notificationManager.notify(1, builder.build());
        /*
        * setTicker : 알림이 뜰때 잠깐 표시되는 Text
        * setNumber : 미확인 알림의 개수
        * addAction : 알림에서 바로 어떤 활동을 할지 선택하는 것
        * builder.addAction(R.mipmap.ic_launcher, "Show", pendingIntent);
        * builder.addAction(R.mipmap.ic_launcher, "Hide", pendingIntent);
        * builder.addAction(R.mipmap.ic_launcher, "Remove", pendingIntent);*/
        /*Notification.BigTextStyle style = new Notification.BigTextStyle(mBuilder);
            style.setSummaryText("and More +");
            style.setBigContentTitle("BigText Expanded Title");
            style.bigText("Mir's IT Blog adress is \"itmir.tistory.com\"," +
                    "Welcome to the Mir's Blog!! Nice to Meet you, this is Example JellyBean Notification");

            mBuilder.setStyle(style);*/
    }
}
