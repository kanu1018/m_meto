package com.kitri.meto.m.metour;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2016-08-09.
 */
public class MainPlanAnd extends Activity {

    int IMGS[] = {R.drawable.adda, R.drawable.addb};
    ImageView imgBtn[] = new ImageView[IMGS.length];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dateplan);

        Intent intent = getIntent();
        int year = intent.getExtras().getInt("year",0);
        int month = intent.getExtras().getInt("month",0);
        int day = intent.getExtras().getInt("day",0);
        int flag = intent.getExtras().getInt("flag",0);


        String MSG = year + "년 " + month + "월" + day +"일";

        TextView txtView = (TextView) findViewById(R.id.datePlanDate);
        txtView.setText(MSG);

        LinearLayout datePlanLayer = (LinearLayout) findViewById(R.id.datePlanLayer);



        if (flag==0){
            FrameLayout frameLayout = new FrameLayout(this);

            for (int i=0;i<IMGS.length;i++){
                imgBtn[i] = new ImageView(this);
                imgBtn[i].setImageResource(IMGS[i]);
                imgBtn[i].setMaxWidth(30);
                imgBtn[i].setMaxHeight(30);
                frameLayout.addView(imgBtn[i]);
            }

            datePlanLayer.addView(frameLayout);

        /*    imgBtn[1].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });*/


          /*  imgBtn[1].setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_UP){
                        imgBtn[1].setVisibility(View.VISIBLE);
                    }else if(event.getAction() == MotionEvent.ACTION_DOWN){
                        imgBtn[1].setVisibility(View.INVISIBLE);
                        finish();
                    }
                    return true;
                }
            });*/
        }else if(flag==1){
            datePlanLayer.setGravity(Gravity.CENTER);
            TextView txtTitle = new TextView(this);
            txtTitle.setText("타이틀 타이틀");
            txtTitle.setPadding(10,50,10,0);
            LinearLayout.LayoutParams title_pos = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            Button btnDelete = new Button(this);
            Button btnDetail = new Button(this);

            LinearLayout btnsLayout = new LinearLayout(this);
            LinearLayout.LayoutParams btnsLayout_pos = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,  ViewGroup.LayoutParams.WRAP_CONTENT);
            btnsLayout.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams btns_pos = new LinearLayout.LayoutParams(0,  ViewGroup.LayoutParams.MATCH_PARENT,1);

            btnDelete.setText("계획삭제");
            btnDetail.setText("세부계획");
            datePlanLayer.addView(txtTitle,title_pos);
            datePlanLayer.addView(btnsLayout,btnsLayout_pos);
            btnsLayout.addView(btnDelete,btns_pos);
            btnsLayout.addView(btnDetail,btns_pos);

        }



    }
}
