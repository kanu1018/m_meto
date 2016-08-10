package com.kitri.meto.m.metour;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class BottomBtn extends Activity {
    Button organize, metoday, sharelist;
    LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_button);

        organize = (Button)findViewById(R.id.organize);
        metoday = (Button)findViewById(R.id.me_today);
        sharelist = (Button)findViewById(R.id.share_list);

        container = (LinearLayout)findViewById(R.id.container);


        organize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        metoday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        sharelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
}
