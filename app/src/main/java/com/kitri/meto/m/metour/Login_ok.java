package com.kitri.meto.m.metour;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2016-08-09.
 */
public class Login_ok extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_ok);
        Log.d("id가 유지되나?",SessionControl.id);

    }

    public void editform(View v){
        Intent intent = new Intent(this,MemEdit.class);
        startActivityForResult(intent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*String msg = "request :"+requestCode + "/ result : "+resultCode;
        msg += "/ data : "+data.getStringExtra("textCode");*/
        //Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
        if(data.getStringExtra("textCode").equals("delete")){
            finish();
        }
    }
}
