package com.kitri.meto.m.metour;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-08-09.
 */
public class SearchByCategory extends Activity {
    Context context;
    LinearLayout sview;
    WebView webView;
    List<SharePlanDTO> slist;
    SharePlanDTO sDto;
    int mem_num, share_num, writer, metoo, point_num;
    String share_title, content, location;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_shareplan);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String requestURL = "http://192.168.14.47:8805/meto/and/share/list.do";
        sview = (LinearLayout)findViewById(R.id.sview);

        try {
            HttpClient client   = new DefaultHttpClient();
            HttpPost post    = new HttpPost(requestURL);
            List<NameValuePair> paramList = new ArrayList<>();

            post.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            slist = getXML(is);

            for(int i=0;i<slist.size();i++){
                sDto = slist.get(i);
                share_num = sDto.getShare_num();
                share_title = sDto.getShare_title();
                content = sDto.getContent();
                writer = sDto.getWriter();
                location = sDto.getLocation();
                metoo = sDto.getMetoo();
                point_num = sDto.getPoint_num();
            }


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        webView = (WebView)findViewById(R.id.webview);
        webView.loadUrl("http://192.168.14.47:8805/meto/and/share/list.do");
        webView.setWebViewClient(new webClient());
        WebSettings set = webView.getSettings();
        set.setJavaScriptEnabled(true);
        set.setBuiltInZoomControls(true);
    }


    class webClient extends WebViewClient {
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
}