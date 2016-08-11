package com.kitri.meto.m.metour;

import org.apache.http.client.HttpClient;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.List;

/**
 * Created by Administrator on 2016-08-10.
 */
public class SessionControl {
    static public DefaultHttpClient httpclient = null;
    static public List<Cookie> cookies;

    public static HttpClient getHttpclient()
    {
        if( httpclient == null){
            SessionControl.setHttpclient(new DefaultHttpClient());
        }
        return httpclient;
    }

    public static void setHttpclient(DefaultHttpClient httpclient) {
        SessionControl.httpclient = httpclient;
    }
}
