package com.kitri.meto.m.metour;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

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

    //DTO
    Member mDto;
    //SharePlanDTO sDto;
    MetooDTO tDto;
    RepDTO repDto;
    //SingoDTO gDto;
    JoinDTO sDto, rDto;

    //List
    List<Member> mlist;
    //List<SharePlanDTO> slist;
    List<MetooDTO> tlist;
    List<RepDTO> rAll;
    //List<SingoDTO> glist;
    List<JoinDTO> slist;
    List<JoinDTO> rlist;

    //lay01: 첫화면, lay02: 댓글전체보기, lay03: 등록가능
    LinearLayout lay01, lay02, lay03;

    // sview: shareplan, repList: 댓글 목록, repMain: 댓글 10개
    LinearLayout sview, repList, repMain;

    WebView webView;
    TextView sTitle, sWriter, meto, repCnt01, repCnt02;
    ImageButton like;

    //share_plan
    int mem_num, share_num, writer, metoo, point_num;
    String share_title, content, location, wId;

    //버튼
    Button all, add;
    ImageButton repfin, bubble, repAdd;

    //등록
    String msg;
    int btnType = 1;

    //rep
    int rep_num, rCnt;

    EditText rEdit;
    Button rAdd;

    //metoo
    String yn;

    //singo
    ImageButton sSingo;
    String sType;

    // 댓글 창 전환
    boolean flag = true;

    //팝업
    PopupWindow pwindo;
    int mWidthPixels, mHeightPixels;
    Button popfin;
    TextView pEdit, pDel, pSingo;
    int num;
    String con;

    //point
    TextView sPoint;
    int point;

    //oncreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shareplan);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //share_num 받아오기
        Intent in = getIntent();

        share_num = Integer.parseInt(in.getStringExtra("share_num"));
        /*share_num = 9;*/

        sview = (LinearLayout)findViewById(R.id.sview);
        shareplan(share_num);

        //세션받아오기, mem_num 검색
        String id = "a@naver.com";

        String requestURL =  "http://192.168.14.30:8805/meto/and/member/select.do?id="+id;

        try {
            HttpClient client   = new DefaultHttpClient();
            HttpPost post    = new HttpPost(requestURL);
            List<NameValuePair> paramList = new ArrayList<>();
            paramList.add(new BasicNameValuePair("share_num", Integer.toString(share_num)));

            post.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));

            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            mlist = getMember(is);

            mDto = mlist.get(0);
            mem_num = mDto.getMem_num();

            Log.d("mem_num >>>>", ""+mem_num);
        } catch(Exception e) {
            Log.d("mem_num select===> ", e.toString());
        }

        // 공유글 상세보기
        webView = (WebView)findViewById(R.id.webview);
        webView.loadUrl("http://192.168.14.30:8805/meto/and/share/content.do?share_num="+share_num);
        webView.setWebViewClient(new webClient());
        WebSettings set = webView.getSettings();
        set.setLoadWithOverviewMode(true);
        set.setUseWideViewPort(true);
        set.setJavaScriptEnabled(true);
        set.setBuiltInZoomControls(true);

        sTitle = (TextView)findViewById(R.id.sTitle);
        sTitle.setText("[공유글] "+share_title);
        sWriter = (TextView)findViewById(R.id.sWriter);
        sWriter.setText(wId);
        sPoint = (TextView)findViewById(R.id.sPoint);
        sPoint.setText("Point: "+point);

        //작성자 joinDto
        //sWriter.setText(writer);
        meto = (TextView)findViewById(R.id.meto);
        meto.setText(""+metoo);

        //좋아요
        like = (ImageButton)findViewById(R.id.like01);
        metooYn(mem_num, share_num);

        //전체댓글, 댓글쓰기
        all = (Button)findViewById(R.id.repAll);
        all.setOnClickListener(this);
        add = (Button)findViewById(R.id.rWrite);
        add.setOnClickListener(this);
        repfin = (ImageButton) findViewById(R.id.repfin);
        repfin.setOnClickListener(this);
        repAdd = (ImageButton) findViewById(R.id.repAdd);
        repAdd.setOnClickListener(this);


        //댓글작성
        rEdit = (EditText) findViewById(R.id.rEdit);
        rEdit.setOnClickListener(this);
        rAdd = (Button) findViewById(R.id.rAdd);
        rAdd.setOnClickListener(this);

        //댓글 10개만 출력
        repMain = (LinearLayout)findViewById(R.id.repMain);
        repMain(share_num);
        //댓글 전체 출력
        repList = (LinearLayout)findViewById(R.id.repList);
        repList(share_num);

        //댓글 갯수
        bubble = (ImageButton)findViewById(R.id.bubble01);
        bubble.setOnClickListener(this);
        repCnt01 = (TextView)findViewById(R.id.repCnt01);
        repCnt01.setText(""+rCnt);
        repCnt02 = (TextView)findViewById(R.id.repCnt02);
        repCnt02.setText("댓글 "+rCnt);

        //글 신고
        sSingo = (ImageButton)findViewById(R.id.sSingo);
        sSingo.setOnClickListener(this);

        //
        lay01 = (LinearLayout)findViewById(R.id.lay01);
        lay02 = (LinearLayout)findViewById(R.id.lay02);
        lay03 = (LinearLayout)findViewById(R.id.lay03);

        //팝업창
        WindowManager w = getWindowManager();
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);
        // since SDK_INT = 1;
        mWidthPixels = metrics.widthPixels;
        mHeightPixels = metrics.heightPixels;

        // 상태바와 메뉴바의 크기를 포함해서 재계산
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
            try {
                mWidthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
                mHeightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
            } catch (Exception ignored) {
            }
        // 상태바와 메뉴바의 크기를 포함
        if (Build.VERSION.SDK_INT >= 17)
            try {
                Point realSize = new Point();
                Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
                mWidthPixels = realSize.x;
                mHeightPixels = realSize.y;
            } catch (Exception ignored) {

            }

        //
    }

    // webView class
    class webClient extends WebViewClient{
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    //share
    public List<JoinDTO> getShare(InputStream is) {
        List<JoinDTO> list = new ArrayList<>();

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(is, "UTF-8");

            int eventType = parser.getEventType();

            JoinDTO dto = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {

                switch (eventType) {
                    case XmlPullParser.START_TAG:

                        String startTag = parser.getName();
                        if (startTag.equals("shareplan")) {
                            dto = new JoinDTO();
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
                            } else if (startTag.equals("sPointNum")) {
                                dto.setPoint_num(Integer.parseInt(parser.nextText()));
                            } else if (startTag.equals("sId")) {
                                dto.setId(parser.nextText());
                            } else if (startTag.equals("sPoint")) {
                                dto.setPoint(Integer.parseInt(parser.nextText()));
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
            Log.d("SharePlan =====>",e.toString());
        } // end of try
        return list;
    }

    //rep
    public List<JoinDTO> getRep(InputStream is) {
        List<JoinDTO> list = new ArrayList<>();

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(is, "UTF-8");

            int eventType = parser.getEventType();

            JoinDTO dto = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {

                switch (eventType) {
                    case XmlPullParser.START_TAG:

                        String startTag = parser.getName();
                        if (startTag.equals("rep")) {
                            dto = new JoinDTO();
                        }
                        if (dto != null) {
                            if (startTag.equals("rNum")) {
                                dto.setRep_num(Integer.parseInt(parser.nextText()));
                            } else if (startTag.equals("rContent")) {
                                dto.setRep_content(parser.nextText());
                            } else if (startTag.equals("rWriter")) {
                                dto.setRep_writer(Integer.parseInt(parser.nextText()));
                            } else if (startTag.equals("rShare")) {
                                dto.setShare_num(Integer.parseInt(parser.nextText()));
                            } else if (startTag.equals("rId")) {
                                dto.setId(parser.nextText());
                            }
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        String endTag = parser.getName();
                        if (endTag.equals("rep")) {
                            list.add(dto);
                        }
                        break;
                } // end
                eventType = parser.next();

            } // end of while
        } catch(Exception e) {
            Log.d("rep =====>",e.toString());
        } // end of try
        return list;
    }

    //member
    public List<Member> getMember(InputStream is) {
        List<Member> list = new ArrayList<>();

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(is, "UTF-8");

            int eventType = parser.getEventType();

            Member dto = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {

                switch (eventType) {
                    case XmlPullParser.START_TAG:

                        String startTag = parser.getName();
                        if (startTag.equals("member")) {
                            dto = new Member();
                        }
                        if (dto != null) {
                            if (startTag.equals("mNum")) {
                                dto.setMem_num(Integer.parseInt(parser.nextText()));
                            } else if (startTag.equals("mId")) {
                                dto.setId(parser.nextText());
                            } else if (startTag.equals("mName")) {
                                dto.setName(parser.nextText());
                            } else if (startTag.equals("mPhone")) {
                                dto.setPhone(parser.nextText());
                            } else if (startTag.equals("mGender")) {
                                dto.setGender(parser.nextText());
                            } else if (startTag.equals("mBirth")) {
                                dto.setBirth(parser.nextText());
                            } else if (startTag.equals("mStatus")) {
                                dto.setMem_status(parser.nextText());
                            }
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        String endTag = parser.getName();
                        if (endTag.equals("member")) {
                            list.add(dto);
                        }
                        break;
                } // end
                eventType = parser.next();

            } // end of while
        } catch(Exception e) {
            Log.d("member=====>",e.toString());
        } // end of try
        return list;
    }

    //metoo
    public List<MetooDTO> getMetoo(InputStream is) {
        List<MetooDTO> list = new ArrayList<>();

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(is, "UTF-8");

            int eventType = parser.getEventType();

            MetooDTO dto = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {

                switch (eventType) {
                    case XmlPullParser.START_TAG:

                        String startTag = parser.getName();
                        if (startTag.equals("metoo")) {
                            dto = new MetooDTO();
                        }
                        if (dto != null) {
                            if (startTag.equals("meNum")) {
                                dto.setMetoo_num(Integer.parseInt(parser.nextText()));
                            } else if (startTag.equals("meShare")) {
                                dto.setShare_num(Integer.parseInt(parser.nextText()));
                            } else if (startTag.equals("meMem")) {
                                dto.setMem_num(Integer.parseInt(parser.nextText()));
                            } else if (startTag.equals("meYn")) {
                                dto.setMetoo_yn(parser.nextText());
                            }
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        String endTag = parser.getName();
                        if (endTag.equals("metoo")) {
                            list.add(dto);
                        }
                        break;
                } // end
                eventType = parser.next();

            } // end of while
        } catch(Exception e) {
            Log.d("metoo=====>",e.toString());
        } // end of try
        return list;
    }

    //공유글 상세보기
    public void shareplan(int share_num){
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
            slist = getShare(is);

            sDto = slist.get(0);
            share_num = sDto.getShare_num();
            share_title = sDto.getShare_title();
            content = sDto.getContent();
            writer = sDto.getWriter();
            location = sDto.getLocation();
            metoo = sDto.getMetoo();
            point_num = sDto.getPoint_num();
            wId = sDto.getId();
            point = sDto.getPoint();

            Log.d("share_title>>>>", share_title);
        } catch(Exception e) {
            Log.d("shareplan select===> ", e.toString());
        }
    }

    //좋아요 yn 불러오기
    public String metooYn(int mem_num, int share_num){
        //좋아요 상태에 따라 버튼 다르게 띄우기

        String requestURL =  "http://192.168.14.30:8805/meto/and/metoo/yn.do?mem_num"+mem_num+"&share_num="+share_num;

        try {
            HttpClient client   = new DefaultHttpClient();
            HttpPost post    = new HttpPost(requestURL);
            List<NameValuePair> paramList = new ArrayList<>();
            paramList.add(new BasicNameValuePair("mem_num", Integer.toString(mem_num)));
            paramList.add(new BasicNameValuePair("share_num", Integer.toString(share_num)));

            post.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));

            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            tlist = getMetoo(is);

            tDto = tlist.get(0);
            yn = tDto.getMetoo_yn();

            if(yn.equals("y")){
                like.setImageResource(R.drawable.like01);
            } else if (yn.equals("n")){
                like.setImageResource(R.drawable.like02);
            }

            like.setOnClickListener(this);

            Log.d("metoo_yn >>>>", yn);
        } catch(Exception e) {
            Log.d("metoo select===> ", e.toString());
        }
        return yn;
    }

    //댓글 전체 목록
    public void repList(int share_num){
        String requestURL =  "http://192.168.14.30:8805/meto/and/rep/list.do?share_num="+share_num;  // url 변경

        try {
            Log.d("댓글목록", Integer.toString(share_num));
            HttpClient client   = new DefaultHttpClient();
            HttpPost post    = new HttpPost(requestURL);
            List<NameValuePair> paramList = new ArrayList<>();
            paramList.add(new BasicNameValuePair("share_num", Integer.toString(share_num))); // 컨트롤러에서 int로 변환

            post.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));

            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            rlist = getRep(is);
            LinearLayout layoutSub[] = new LinearLayout[rlist.size()];

            rCnt = rlist.size();
            for(int i=0; i<rlist.size(); i++){
                rDto = rlist.get(i);
                layoutSub[i] = new LinearLayout(this);
                //layoutSub[i].setOrientation(LinearLayout.HORIZONTAL);
                layoutSub[i].setOrientation(LinearLayout.VERTICAL);

                TextView txt[] = new TextView[2];
                for(int j = 0; j < txt.length; j++) {
                    txt[j] = new TextView(this);
                    txt[j].setTextColor(Color.BLACK);
                }

                //댓글 작성자 joinDto
                txt[0].setText(""+rDto.getId());
                txt[0].setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                txt[0].setTextSize(13);
                txt[0].setTextColor(Color.rgb(128,128,128));
                txt[0].setPadding(15, 0, 0, 0);

                txt[1].setText(rDto.getRep_content());
                txt[1].setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                txt[1].setTextSize(18);
                txt[1].setPadding(15, 0, 0, 0);

                for(int j = 0; j < txt.length; j++) {
                    layoutSub[i].addView(txt[j]);
                    layoutSub[i].setTag(rDto.getRep_num()+"/"+rDto.getRep_content());
                    layoutSub[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String tmp = v.getTag().toString();
                            initiatePopupWindow(tmp);
                        }
                    });
                }

                repList.addView(layoutSub[i]);
            }

            Log.d("rep >>>>", rDto.getRep_content());
        } catch(Exception e) {
            Log.d("rep select===> ", e.toString());
        }
    }

    //댓글 최신 10개
    public void repMain(int share_num){
        String requestURL =  "http://192.168.14.30:8805/meto/and/rep/main.do?share_num="+share_num;  // url 변경

        try {
            Log.d("댓글목록 10개", Integer.toString(share_num));
            HttpClient client   = new DefaultHttpClient();
            HttpPost post    = new HttpPost(requestURL);
            List<NameValuePair> paramList = new ArrayList<>();
            paramList.add(new BasicNameValuePair("share_num", Integer.toString(share_num))); // 컨트롤러에서 int로 변환

            post.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));

            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            rlist = getRep(is);
            LinearLayout layoutSub[] = new LinearLayout[rlist.size()];

            int size = rlist.size()-1;


            for(int i = size; i >= 0; i--){
                rDto = rlist.get(i);
                layoutSub[i] = new LinearLayout(this);
                //layoutSub[i].setOrientation(LinearLayout.HORIZONTAL);
                layoutSub[i].setOrientation(LinearLayout.VERTICAL);

                TextView txt[] = new TextView[2];
                for(int j = 0; j < txt.length; j++) {
                    txt[j] = new TextView(this);
                    txt[j].setTextColor(Color.BLACK);
                }

                //댓글 작성자 joinDto
                txt[0].setText(""+rDto.getId());
                txt[0].setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                txt[0].setTextSize(13);
                txt[0].setTextColor(Color.rgb(128,128,128));
                txt[0].setPadding(15, 0, 0, 0);
                //txt[0].setGravity(Gravity.CENTER);
                //txt[0].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.2f));

                txt[1].setText(rDto.getRep_content());
                txt[1].setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                txt[1].setTextSize(18);
                txt[1].setPadding(15, 0, 0, 0);
                //txt[1].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0.1f));


                for(int j = 0; j < txt.length; j++) {
                    layoutSub[i].addView(txt[j]);
                    /*layoutSub[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PopupMenu pop = new PopupMenu(getApplicationContext(), v);
                            pop.getMenuInflater().inflate(R.menu.popup_rep01, pop.getMenu());
                            pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    if(item.getTitle().equals("댓글 수정")){
                                        Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                                    } else if(item.getTitle().equals("댓글 삭제")){
                                        Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                                    } else if(item.getTitle().equals("댓글 신고")){
                                        Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                                    } else if(item.getTitle().equals("취소")){
                                        Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                                    }
                                    return true;
                                }
                            });
                            pop.show();
                        }
                    });*/
                }
                repMain.addView(layoutSub[i]);
            }

            Log.d("rep >>>>", rDto.getRep_content());
        } catch(Exception e) {
            Log.d("rep select===> ", e.toString());
        }
    }

    //댓글 등록
    public void repAdd(){
        msg = rEdit.getText().toString();
        Log.d("댓글 내용===> ", msg);
        Toast.makeText(getApplicationContext(), "댓글등록", Toast.LENGTH_SHORT).show();
        String requestURL =  "http://192.168.14.30:8805/meto/and/rep/add.do";

        HttpClient client   = new DefaultHttpClient();
        HttpPost post    = new HttpPost(requestURL);
        List<NameValuePair> paramList = new ArrayList<>();
        paramList.add(new BasicNameValuePair("rep_content", msg));
        paramList.add(new BasicNameValuePair("rep_writer", Integer.toString(mem_num)));
        paramList.add(new BasicNameValuePair("share_num", Integer.toString(share_num)));

        try {
            post.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
            HttpResponse response = client.execute(post);
        } catch(Exception e) {
            Log.d("sendPost===> ", e.toString());
        }
    }

    //댓글 수정
    public void repEdit(int rep_num){
        msg = rEdit.getText().toString();
        Log.d("댓글 수정 번호===> ", ""+rep_num);
        Log.d("댓글 수정 내용===> ", msg);
        Toast.makeText(getApplicationContext(), "댓글수정", Toast.LENGTH_SHORT).show();
        String requestURL =  "http://192.168.14.30:8805/meto/and/rep/edit.do";

        HttpClient client   = new DefaultHttpClient();
        HttpPost post    = new HttpPost(requestURL);
        List<NameValuePair> paramList = new ArrayList<>();
        paramList.add(new BasicNameValuePair("rep_content", msg));
        paramList.add(new BasicNameValuePair("rep_writer", Integer.toString(mem_num)));
        paramList.add(new BasicNameValuePair("share_num", Integer.toString(share_num)));
        paramList.add(new BasicNameValuePair("rep_num", Integer.toString(rep_num)));

        try {
            post.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
            HttpResponse response = client.execute(post);
        } catch(Exception e) {
            Log.d("sendPost===> ", e.toString());
        }
    }

    //댓글 삭제
    public void repDel(int rep_num){
        String requestURL =  "http://192.168.14.30:8805/meto/and/rep/del.do";

        HttpClient client   = new DefaultHttpClient();
        HttpPost post    = new HttpPost(requestURL);
        List<NameValuePair> paramList = new ArrayList<>();
        paramList.add(new BasicNameValuePair("share_num", Integer.toString(share_num)));
        paramList.add(new BasicNameValuePair("rep_num", Integer.toString(rep_num)));

        try {
            post.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
            HttpResponse response = client.execute(post);
        } catch(Exception e) {
            Log.d("sendPost===> ", e.toString());
        }
    }

    //신고 등록
    public void shareSingo(){
        msg = rEdit.getText().toString();
        Log.d("신고 내용===> ", msg);
        Toast.makeText(getApplicationContext(), "신고 등록", Toast.LENGTH_SHORT).show();
        String requestURL =  "http://192.168.14.30:8805/meto/and/singo/add.do";

        HttpClient client   = new DefaultHttpClient();
        HttpPost post    = new HttpPost(requestURL);
        List<NameValuePair> paramList = new ArrayList<>();
        paramList.add(new BasicNameValuePair("singo_kind", sType));
        paramList.add(new BasicNameValuePair("singo_content", msg));
        paramList.add(new BasicNameValuePair("mem_num", Integer.toString(mem_num)));
        paramList.add(new BasicNameValuePair("share_num", Integer.toString(share_num)));

        try {
            post.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
            HttpResponse response = client.execute(post);
        } catch(Exception e) {
            Log.d("sendPost===> ", e.toString());
        }
    }

    //댓글 팝업창
    public void initiatePopupWindow(String s){
        Log.d("tag", s);

        String tmp[] = s.split("/");
        num = Integer.parseInt(tmp[0]);
        con = tmp[1];

        Log.d("num", tmp[0]);
        Log.d("con", tmp[1]);

        try {
            //  LayoutInflater 객체와 시킴
            LayoutInflater inflater = (LayoutInflater) SharePlanActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View layout = inflater.inflate(R.layout.popup,
                    (ViewGroup) findViewById(R.id.popup_element));

            pwindo = new PopupWindow(layout, mWidthPixels-400, mHeightPixels-1300, true);
            pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);

            //댓글 수정
            pEdit = (TextView)layout.findViewById(R.id.pEdit);
            pEdit.setOnClickListener(pBtn01);

            pDel = (TextView)layout.findViewById(R.id.pDel);
            pDel.setOnClickListener(pBtn02);

            pSingo = (TextView)layout.findViewById(R.id.pSingo);
            pSingo.setOnClickListener(pBtn03);

            popfin = (Button) layout.findViewById(R.id.popfin);
            popfin.setOnClickListener(pBtn04);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //댓글 수정
    public View.OnClickListener pBtn01 =
            new View.OnClickListener() {
                public void onClick(View v) {
                    //팝업창 끄기
                    pwindo.dismiss();
                    //레이아웃 invisible
                    lay01.setVisibility(View.VISIBLE);
                    lay02.setVisibility(View.INVISIBLE);
                    //댓글창 열기
                    lay03.setVisibility(View.VISIBLE);
                    rEdit.setText(con);
                    //수정
                    btnType = 2;
                }
            };

    //댓글 삭제
    public View.OnClickListener pBtn02 =
            new View.OnClickListener() {
                public void onClick(View v) {
                    //팝업창 끄기
                    pwindo.dismiss();
                    //댓글삭제
                    repDel(num);
                    repList.removeAllViews();
                    repMain.removeAllViews();
                    repMain(share_num);
                    repList(share_num);
                }
            };

    //댓글 신고
    public View.OnClickListener pBtn03 =
            new View.OnClickListener() {
                public void onClick(View v) {
                    //팝업창 끄기
                    pwindo.dismiss();
                    //레이아웃 invisible
                    lay01.setVisibility(View.VISIBLE);
                    lay02.setVisibility(View.INVISIBLE);
                    //댓글창 열기
                    lay03.setVisibility(View.VISIBLE);
                    sType = "r";
                    rEdit.setHint("신고사유를 작성하세요.");
                    //신고
                    btnType = 3;
                }
            };



    //팝업창 끄기
    public View.OnClickListener pBtn04 =
            new View.OnClickListener() {
                public void onClick(View v) {
                    pwindo.dismiss();
                }
            };

    //onclick
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.like01){
            //좋아요
            String requestURL =  "http://192.168.14.30:8805/meto/and/metoo/update.do?mem_num="+mem_num+"&share_num="+share_num;

            int type;
            if(yn.equals("n")){
                // n 일때 metoo++;
                Toast.makeText(getApplicationContext(), "metoo++", Toast.LENGTH_SHORT).show();
                type = 1;
                requestURL+= "&type="+type;
                like.setImageResource(R.drawable.like01);
                Log.d("metoo++ ===> ", requestURL);
            } else if (yn.equals("y")){
                // y 일때 metoo--;
                Toast.makeText(getApplicationContext(), "metoo--", Toast.LENGTH_SHORT).show();
                type = 2;
                requestURL+= "&type="+type;
                like.setImageResource(R.drawable.like02);
                Log.d("metoo-- ===> ", requestURL);
            }

            HttpClient client   = new DefaultHttpClient();
            HttpPost post    = new HttpPost(requestURL);
            List<NameValuePair> paramList = new ArrayList<>();
            paramList.add(new BasicNameValuePair("mem_name", Integer.toString(mem_num)));
            paramList.add(new BasicNameValuePair("share_num", Integer.toString(share_num)));

            try {
                post.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
                HttpResponse response = client.execute(post);
            } catch(Exception e) {
                Log.d("sendPost===> ", e.toString());
            }

            if(yn.equals("n")){
                like.setImageResource(R.drawable.like01);
            } else if (yn.equals("y")){
                like.setImageResource(R.drawable.like02);
            }

            //yn 다시 setting
            metooYn(mem_num, share_num);
            //좋아요 수 다시 setting
            shareplan(share_num);
            meto.setText(""+metoo);
            //point 불러오기
            shareplan(share_num);
            sPoint.setText("Point: "+point);

        } else if (v.getId() == R.id.rAdd) {
            // 댓글 작성
            if(btnType == 1){
                //등록
                repAdd();
                Toast.makeText(getApplicationContext(), "댓글이 등록 되었습니다.", Toast.LENGTH_SHORT).show();
            } else if(btnType == 2){
                //수정
                repEdit(num);
                Toast.makeText(getApplicationContext(), "댓글이 수정 되었습니다.", Toast.LENGTH_SHORT).show();
                btnType = 1;
            } else if(btnType == 3){
                //신고
                Toast.makeText(getApplicationContext(), "신고가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                shareSingo();
                lay03.setVisibility(View.INVISIBLE);
                btnType = 1;
            }

            rEdit.setText("");
            repList.removeAllViews();
            repMain.removeAllViews();
            repMain(share_num);
            repList(share_num);
            repCnt01.setText(""+rCnt);
            repCnt02.setText("댓글 "+rCnt);
            lay03.setVisibility(View.INVISIBLE);
        } else if(v.getId() == R.id.repAll){
            // 댓글 전체보기
            lay01.setVisibility(View.INVISIBLE);
            lay02.setVisibility(View.VISIBLE);
        } else if(v.getId() == R.id.rWrite){
            if (flag == true) {
                // 댓글 쓰기
                lay03.setVisibility(View.VISIBLE);
                rEdit.setHint("댓글을 작성하세요.");
                flag = false;
            } else if (flag == false) {
                lay03.setVisibility(View.INVISIBLE);
                flag = true;
            }
        } else if(v.getId() == R.id.repfin){
            // 댓글 전체보기 창 닫기
            lay01.setVisibility(View.VISIBLE);
            lay02.setVisibility(View.INVISIBLE);
        } else if(v.getId() == R.id.sSingo){
            if (flag == true) {
                btnType = 3;
                lay03.setVisibility(View.VISIBLE);
                sType = "s";
                rEdit.setHint("신고사유를 작성하세요.");
                flag = false;
            } else if (flag == false) {
                lay03.setVisibility(View.INVISIBLE);
                flag = true;
            }
        } else if(v.getId() == R.id.bubble01){
            // 전체댓글보기
            lay01.setVisibility(View.INVISIBLE);
            lay02.setVisibility(View.VISIBLE);
        } else if(v.getId() == R.id.repAdd){
            // 댓글 전체보기 창 닫기
            lay01.setVisibility(View.VISIBLE);
            lay02.setVisibility(View.INVISIBLE);

            if (flag == true) {
                // 댓글 쓰기
                lay03.setVisibility(View.VISIBLE);
                rEdit.setHint("댓글을 작성하세요.");
                flag = false;
            } else if (flag == false) {
                lay03.setVisibility(View.INVISIBLE);
                flag = true;
            }
        }

    }// onclick

}// activity
