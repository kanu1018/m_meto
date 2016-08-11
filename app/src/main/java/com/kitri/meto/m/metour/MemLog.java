package com.kitri.meto.m.metour;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-08-09.
 */
public class MemLog extends Activity{
    EditText edtId,edtPwd;
    Button btnLogin,btnJoin;
    String vId,vPwd;
    boolean flag;
    public CookieManager cookieManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        edtId = (EditText) findViewById(R.id.mem_id);
        edtPwd = (EditText)findViewById(R.id.mem_pwd);

        btnLogin = (Button)findViewById(R.id.mem_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vId = edtId.getText().toString();
                vPwd = edtPwd.getText().toString();
                String requestURL =  "http://192.168.14.19:8805/meto/and/member/login.do";

                //HttpClient client   = new DefaultHttpClient();
                HttpClient client   = SessionControl.getHttpclient();
                HttpPost post    = new HttpPost(requestURL);
                List<NameValuePair> paramList = new ArrayList<>();

                paramList.add(new BasicNameValuePair("id"   , vId));
                paramList.add(new BasicNameValuePair("pwd"  , vPwd));

                try {
                    post.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
                    HttpResponse response = client.execute(post);

                    ///////////////////////
                    SessionControl.cookies = SessionControl.httpclient.getCookieStore().getCookies();
                    //////////////////////
                    HttpEntity entity = response.getEntity();
                    InputStream is = entity.getContent();
                    flag = getXMLFlag(is);

                    //Toast.makeText(getApplicationContext(), "flag 확인 "+flag,Toast.LENGTH_SHORT).show();
                } catch(Exception e) {
                    Log.d("sendPost===> ", e.toString());
                }

                if(flag==true){
                    Intent intent = new Intent(getApplicationContext(),Login_ok.class);
                    startActivity(intent);
                } else{
                    Toast.makeText(getApplicationContext(), "로그인 실패 id / password를 확인하세요.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnJoin = (Button)findViewById(R.id.mem_join);
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtId.setText("");
                edtPwd.setText("");
                Intent intent = new Intent(getApplicationContext(),MemIns.class);
                startActivity(intent);
            }
        });
    }

    public boolean getXMLFlag(InputStream is) {
        boolean flag1=false;

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(is, "UTF-8");

            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {

                switch (eventType) {
                    case XmlPullParser.START_TAG:

                        String startTag = parser.getName();
                        if (startTag.equals("flag")) {
                            flag1 = Boolean.parseBoolean(parser.nextText());
                        }

                        break;

                    case XmlPullParser.END_TAG:
                        String endTag = parser.getName();
                        break;
                } // end
                eventType = parser.next();
            } // end of while
        } catch(Exception e) {
            Log.d("SelectActivityError",e.toString());
        } // end of try
        return flag1;
    }
}
