package com.kitri.meto.m.metour;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016-08-08.
 */
public class SharePlanActivity extends Activity implements View.OnClickListener{
    Context context;

    // MemberDto memDto
    SharePlanDTO sDto;
    MetooDTO tDto;
    RepDTO rDto;
    SingoDTO gDto;

    //List<MemberDTO> mlist;
    List<SharePlanDTO> slist;
    List<MetooDTO> tlist;
    List<RepDTO> rlist;
    List<SingoDTO> glist;

    LinearLayout sview;
    WebView webView;
    TextView sWriter, meto;
    ImageButton like;

    EditText rEdit;
    Button rAdd;

    //share_plan
    int mem_num, share_num, writer, metoo, point_num;
    String share_title, content, location;

    //rep
    String rep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shareplan);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //share_num 받아오기
        share_num = 9;
        String requestURL =  "http://192.168.14.30:8805/meto/and/share/view2.do?share_num="+share_num;  // url 변경

        sview = (LinearLayout)findViewById(R.id.sview);

        try {
            Log.d("접속성공", Integer.toString(share_num));
            HttpClient client   = new DefaultHttpClient();
            HttpPost post    = new HttpPost(requestURL);
            List<NameValuePair> paramList = new ArrayList<>();
            paramList.add(new BasicNameValuePair("share_num", Integer.toString(share_num))); // 컨트롤러에서 int로 변환

            post.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));

            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            slist = getXML(is);

            sDto = slist.get(0);
            share_num = sDto.getShare_num();
            share_title = sDto.getShare_title();
            content = sDto.getContent();
            writer = sDto.getWriter();
            location = sDto.getLocation();
            metoo = sDto.getMetoo();
            point_num = sDto.getPoint_num();

            Log.d("share_title>>>>", share_title);
        } catch(Exception e) {
            Log.d("shareplan select===> ", e.toString());
        }

        //세션받아오기, mem_num 검색
        // 공유글 share_num 받아오기
        // 공유글 상세보기
        webView = (WebView)findViewById(R.id.webview);
        webView.loadUrl("http://192.168.14.30:8805/meto/and/share/content.do?share_num="+share_num);
        webView.setWebViewClient(new webClient());
        WebSettings set = webView.getSettings();
        set.setJavaScriptEnabled(true);
        set.setBuiltInZoomControls(true);


        //sWriter = (TextView)findViewById(R.id.sWriter);
        //meto = (TextView)findViewById(R.id.meto);

        //좋아요
        //좋아요 상태에 따라 버튼 다르게 띄우기
        //like = (ImageButton)findViewById(R.id.like01);

        //댓글작성
        //rEdit = (EditText) findViewById(R.id.rEdit);
        //rAdd = (Button) findViewById(R.id.rAdd);

        //댓글 리스트

    }

    // webView class
    class webClient extends WebViewClient{
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    public List<SharePlanDTO> getXML(InputStream is) {
        List<SharePlanDTO> list = new ArrayList<>();

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(is, "UTF-8");

            int eventType = parser.getEventType();

            SharePlanDTO dto = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {

                switch (eventType) {
                    case XmlPullParser.START_TAG:

                        String startTag = parser.getName();
                        if (startTag.equals("shareplan")) {
                            dto = new SharePlanDTO();
                        }
                        if (dto != null) {
                            if (startTag.equals("sNum")) {
                                dto.setShare_num(Integer.parseInt(parser.nextText()));
                            } else if (startTag.equals("stitle")) {
                                dto.setShare_title(parser.nextText());
                            } else if (startTag.equals("sCont")) {
                                dto.setContent(parser.nextText());
                            } else if (startTag.equals("sWriter")) {
                                dto.setWriter(Integer.parseInt(parser.nextText()));
                            } else if (startTag.equals("sLoc")) {
                                dto.setLocation(parser.nextText());
                            } else if (startTag.equals("sMetoo")) {
                                dto.setMetoo(Integer.parseInt(parser.nextText()));
                            } else if (startTag.equals("sPoint")) {
                                dto.setPoint_num(Integer.parseInt(parser.nextText()));
                            }
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        String endTag = parser.getName();
                        if (endTag.equals("shareplan")) {
                            list.add(dto);
                        }
                        break;
                } // end
                eventType = parser.next();

            } // end of while
        } catch(Exception e) {
            Log.d("SharePlan getXML=====>",e.toString());
        } // end of try
        return list;
    }

    @Override
    public void onClick(View v) {
       // if(v.getId() == R.id.like01){
            //좋아요
            // y 일때 metoo--;
            // n 일때 metoo++;
/*
            String requestURL =  "http://192.168.14.30:8805/"; // url 변경

            HttpClient client   = new DefaultHttpClient();
            HttpPost post    = new HttpPost(requestURL);
            List<NameValuePair> paramList = new ArrayList<>();


            try {
                post.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
                HttpResponse response = client.execute(post);
                //Toast.makeText(getApplicationContext(), "mem DB  저장 확인 ",Toast.LENGTH_SHORT).show();
            } catch(Exception e) {
                Log.d("sendPost===> ", e.toString());
            }*/

       // } else if (v.getId() == R.id.rAdd) {
            // 댓글 작성
           /* rep = rEdit.getText().toString();

            String requestURL =  "http://192.168.14.30:8805/"; // url 변경

            HttpClient client   = new DefaultHttpClient();
            HttpPost post    = new HttpPost(requestURL);
            List<NameValuePair> paramList = new ArrayList<>();
            paramList.add(new BasicNameValuePair("rep_content", rep));
            //paramList.add(new BasicNameValuePair("rep_writer", mem_num));
            //paramList.add(new BasicNameValuePair("share_num", share_num));

            try {
                post.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
                HttpResponse response = client.execute(post);
                //Toast.makeText(getApplicationContext(), "mem DB  저장 확인 ",Toast.LENGTH_SHORT).show();
            } catch(Exception e) {
                Log.d("sendPost===> ", e.toString());
            }

            rEdit.setText("");*/
       // }

    }// onclick

}// activity
