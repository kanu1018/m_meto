package com.kitri.meto.m.metour;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
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
import java.util.List;

/**
 * Created by Administrator on 2016-08-09.
 */
public class SearchByCategory extends AppCompatActivity {
    LinearLayout listsel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_shareplan);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        listsel = (LinearLayout)findViewById(R.id.layoutSel);
        ArrayList<WebView> weblist = new ArrayList<WebView>();
        ArrayList<Integer> path = new ArrayList<Integer>();
        ArrayList<SharePlanDTO> list = new ArrayList<SharePlanDTO>();
        String requestURL = "http://192.168.14.45:8805/meto/and/share/list.do";

        HttpClient client   = new DefaultHttpClient();
        HttpPost post    = new HttpPost(requestURL);
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        SharePlanDTO sDto;
        List<SharePlanDTO> slist;

        try {
            post.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            slist = getXML(is);
            LinearLayout laysub[] = new LinearLayout[slist.size()];


            for(int i = 0;i<slist.size();i++){
                laysub[i] = new LinearLayout(this);
                laysub[i].setOrientation(LinearLayout.VERTICAL);
                sDto = slist.get(i);
                TextView text1 = new TextView(this);
                text1.setText(sDto.getId()+" "+sDto.getPhoto());
                WebView wv1 = new WebView(this);
                weblist.add(wv1);
                wv1.loadUrl(sDto.getPhoto());
                wv1.setWebViewClient(new webClient());
                WebSettings set = wv1.getSettings();
                set.setJavaScriptEnabled(true);
                set.setBuiltInZoomControls(true);
                /*wv1.loadUrl("http://192.168.14.47:8805/meto/and/share/webphoto.do?sub_num="+sDto.getSub_num());
                wv1.setWebViewClient(new webClient());
                WebSettings set = wv1.getSettings();
                set.setJavaScriptEnabled(true);
                set.setBuiltInZoomControls(true);*/

                LinearLayout lay1 = new LinearLayout(this);
                lay1.setOrientation(LinearLayout.HORIZONTAL);
                TextView text2 = new TextView(this);
                text2.setText(sDto.getShare_title());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.MATCH_PARENT);
                params.weight = 9.0f;
                text2.setLayoutParams(params);
                TextView text3 = new TextView(this);
                params = new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.MATCH_PARENT);
                params.weight = 1.0f;
                params.bottomMargin=30;
                text3.setText("♥"+sDto.getMetoo());
                text3.setLayoutParams(params);

                lay1.addView(text2);
                lay1.addView(text3);
                laysub[i].addView(text1);
                laysub[i].addView(wv1);
                laysub[i].addView(lay1);
                /*params = (LinearLayout.LayoutParams)laysub[i].getLayoutParams();
                params.bottomMargin = 10;
                laysub[i].setLayoutParams(params);*/



                /*String[] data = new String[6];
                data[0] =  String.valueOf(sDto.getShare_num());
                data[1] = sDto.getShare_title();
                data[2] = String.valueOf(sDto.getWriter());
                data[3] = sDto.getLocation();
                data[4] = String.valueOf(sDto.getMetoo());
                data[5] = String.valueOf(sDto.getPoint_num());
                for(int j=0;j<6;j++) {
                    txtName[j] = new TextView(this);
                    txtName[j].setText(data[j]);
                    txtName[j].setTextColor(Color.BLACK);
                    txtName[j].setTextSize(15f);
                    txtName[j].setWidth(130);
                    laysub[i].addView(txtName[j]);
                }*/









                listsel.addView(laysub[i]);
            }
        } catch(Exception e) {
            Log.d("sendPost===> ", e.toString());
        }

        /*wv1.loadUrl("http://192.168.14.47:8805/meto/and/share/webphoto.do?sub_num="+sDto.getSub_num());
                wv1.setWebViewClient(new webClient());
                WebSettings set = wv1.getSettings();
                set.setJavaScriptEnabled(true);
                set.setBuiltInZoomControls(true);*/

        /*requestURL = "http://192.168.14.47:8805/meto/and/share/list.do";

        client   = new DefaultHttpClient();
        post    = new HttpPost(requestURL);
        paramList = new ArrayList<NameValuePair>();


        weblist.get(0).loadUrl("");
        weblist.get(0).setWebViewClient(new webClient());
        WebSettings set = weblist.get(0).getSettings();
        set.setJavaScriptEnabled(true);
        set.setBuiltInZoomControls(true);

        weblist.get(1).loadUrl("http://192.168.14.47:8805/meto/and/share/webphoto.do?sub_num=1");
        weblist.get(1).setWebViewClient(new webClient());
        set = weblist.get(1).getSettings();
        set.setJavaScriptEnabled(true);
        set.setBuiltInZoomControls(true);*/
        /*for(int i=0;i<list.size();i++){
            Log.d("ㅁ",path.get(i)+"");
            if(path.get(i) != 0) {
                Log.d("ㅁ",path.get(i)+"");
                weblist.get(i).loadUrl("http://192.168.14.47:8805/meto/and/share/webphoto.do?sub_num=" + path.get(i));
                weblist.get(i).setWebViewClient(new webClient());
                WebSettings set = weblist.get(i).getSettings();
                set.setJavaScriptEnabled(true);
                set.setBuiltInZoomControls(true);
            }
        }*/

    }

    class webClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            /*view.loadUrl(url);
            return true;*/

            if(url.startsWith("c:")) {
                Intent call_phone = new Intent(Intent.ACTION_VIEW , Uri.parse(url)) ;
                startActivity(call_phone) ;
                return true ;
            }
            //그러면 예외처리를 위해 이 부분도 이전과 다르게 정상적으로 호출 해야하겠습니다.
            if(url.startsWith("http:") || url.startsWith("https:")) {
                view.loadUrl(url);
                return true;
            }
            return true;

        }
    }
    /*public void execute(Context context){
        this.context = context;
        LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inf.inflate(R.layout.main_shareplan, this, true);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        listsel = (LinearLayout)findViewById(R.id.layoutSel);

        ArrayList<SharePlanDTO> list = new ArrayList<SharePlanDTO>();
        String requestURL = "http://192.168.14.47:8805/meto/and/share/list.do";

        HttpClient client   = new DefaultHttpClient();
        HttpPost post    = new HttpPost(requestURL);
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();

        try {
            post.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            slist = getXML(is);

            LinearLayout laysub[] = new LinearLayout[list.size()];
            TextView txtName[] = new TextView[7];

            for(int i = 0;i<list.size();i++){
                sDto = list.get(i);
                laysub[i] = new LinearLayout(context);
                laysub[i].setOrientation(LinearLayout.HORIZONTAL);
                sDto = slist.get(i);
                share_num = sDto.getShare_num();
                share_title = sDto.getShare_title();
                content = sDto.getContent();
                writer = sDto.getWriter();
                location = sDto.getLocation();
                metoo = sDto.getMetoo();
                point_num = sDto.getPoint_num();
                for(int j=0;j<7;j++) {
                    txtName[j] = new TextView(context);
                    txtName[j].setText(share_num+" "+share_title+" "+content+" "+writer+" "+location+" "+metoo+" "+point_num);
                    txtName[j].setTextColor(Color.BLACK);
                    txtName[j].setTextSize(15f);
                    txtName[j].setWidth(130);
                    laysub[i].addView(txtName[j]);
                }
                listsel.addView(laysub[i]);
            }
        } catch(Exception e) {
            Log.d("sendPost===> ", e.toString());
        }
    }*/

    public List<SharePlanDTO> getXML(InputStream is) {
        List<SharePlanDTO> list = new ArrayList<SharePlanDTO>();

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
                            } else if (startTag.equals("sTitle")) {
                                dto.setShare_title(parser.nextText());
                            } else if (startTag.equals("sWriter")) {
                                dto.setWriter(Integer.parseInt(parser.nextText()));
                            } else if (startTag.equals("sLoc")) {
                                dto.setLocation(parser.nextText());
                            } else if (startTag.equals("sMetoo")) {
                                dto.setMetoo(Integer.parseInt(parser.nextText()));
                            } else if (startTag.equals("sPoint")) {
                                dto.setPoint_num(Integer.parseInt(parser.nextText()));
                            } else if(startTag.equals("sId")){
                                dto.setId(parser.nextText());
                            } else if(startTag.equals("sPhoto")){
                                dto.setPhoto(parser.nextText());
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
}