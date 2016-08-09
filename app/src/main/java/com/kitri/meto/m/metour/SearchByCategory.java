package com.kitri.meto.m.metour;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016-08-09.
 */
public class SearchByCategory extends LinearLayout{
    Context context;
    LinearLayout listsel;


    public SearchByCategory(Context context) {
        super(context);
        execute(context);
    }

    private void execute(Context context) {
        this.context = context;
        LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inf.inflate(R.layout.main_shareplan, this, true);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        listsel = (LinearLayout)findViewById(R.id.layoutSel);

        ArrayList<SharePlanDTO> list = new ArrayList<SharePlanDTO>();
        String requestURL =  "http://192.168.14.47:8805/meto/and/share/list.do";

        HttpClient client   = new DefaultHttpClient();
        HttpPost post    = new HttpPost(requestURL);

        HttpResponse response = null;
        try {
            response = client.execute(post);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            list = getXML(is);

            LinearLayout laysub[] = new LinearLayout[list.size()];
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public ArrayList<SharePlanDTO> getXML(InputStream is) {
        ArrayList<SharePlanDTO> list = new ArrayList<SharePlanDTO>();

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
                        if (startTag.equals("share")) {
                            dto = new SharePlanDTO();
                        }
                        if (dto != null) {
                            if (startTag.equals("name")) {
                                dto.setMem_name(parser.nextText());
                            } else if (startTag.equals("id")) {
                                dto.setMem_id(parser.nextText());
                            } else if (startTag.equals("pwd")) {
                                dto.setMem_pwd(parser.nextText());
                            } else if (startTag.equals("email")) {
                                dto.setMem_email(parser.nextText());
                            } else if (startTag.equals("phone")) {
                                dto.setMem_phone(parser.nextText());
                            } else if (startTag.equals("addr")) {
                                dto.setMem_addr(parser.nextText());
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        String endTag = parser.getName();
                        if (endTag.equals("share")) {

                            list.add(dto);
                        }
                        break;
                }
                eventType = parser.next();
            } // end of while
        } catch(Exception e) {
            Log.d("SelectActivityError",e.toString());
        } // end of try
        return list;

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

    }
}