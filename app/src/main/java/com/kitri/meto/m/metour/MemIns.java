package com.kitri.meto.m.metour;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-07-13.
 */
public class MemIns extends Activity {
    EditText edtName,edtId,edtPwd,edtPhone,edtpwdCheck,edtBirth;
    RadioButton rg,rg1;
    TextView CheckText;
    Button JoinBtn,CancelBtn;
    String vName,vId,vPwd,vPhone,vBirth,vGender;
    Boolean flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_layout);

        edtId = (EditText)findViewById(R.id.join_id);
        edtPwd = (EditText)findViewById(R.id.join_pwd);
        edtName = (EditText)findViewById(R.id.join_name);
        edtPhone = (EditText)findViewById(R.id.join_phone);
        edtBirth = (EditText)findViewById(R.id.join_birth);
        rg = (RadioButton) findViewById(R.id.rbtn1);
        rg1 = (RadioButton)findViewById(R.id.rbtn2);
        edtpwdCheck = (EditText)findViewById(R.id.pwd_check);
        CheckText = (TextView)findViewById(R.id.join_check_textview);
        flag=false;
        edtpwdCheck.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(edtPwd.getText().toString().equals(edtpwdCheck.getText().toString())){
                    CheckText.setText("비밀번호 사용 가능");
                    flag = true;
                } else {
                    CheckText.setText("비밀번호가 일치하지 않습니다.");
                    flag = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        JoinBtn = (Button)findViewById(R.id.join_write_btn);
        JoinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                vName = edtName.getText().toString();
                vId = edtId.getText().toString();

                vPwd = edtPwd.getText().toString();
                vPhone = edtPhone.getText().toString();
                vBirth = edtBirth.getText().toString();
                //아이디 이메일 형식 체크
                if(!Util.validateEmail(vId)){
                    Toast.makeText(getApplicationContext(),"아이디가 이메일 형식이 아닙니다.",Toast.LENGTH_SHORT).show();
                    return;
                }
                //비밀번호 일치 불일치 체크
                if(!flag){
                    Toast.makeText(getApplicationContext(),"비밀번호를 다시 확인해 주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(vName.equals("")){
                    Toast.makeText(getApplicationContext(),"이름을 입력 해주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(rg.isChecked()){
                    vGender = "m";
                } else if(rg1.isChecked()){
                    vGender = "f";
                } else{
                    Toast.makeText(getApplicationContext(),"성별 체크를 해주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(vPhone.equals("")){
                    Toast.makeText(getApplicationContext(),"핸드폰을 입력 해주세요.",Toast.LENGTH_SHORT).show();
                    return;
                } else if(vBirth.equals("")){
                    Toast.makeText(getApplicationContext(),"생일을 입력 해주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }

                String[] st = vBirth.split("-");
                if(st.length==3){
                    if(st[0].length()!=4){
                        Toast.makeText(getApplicationContext(),"생일 형식이 틀렸습니다.",Toast.LENGTH_SHORT).show();
                        return;
                    } else if(st[1].length()!=2){
                        Toast.makeText(getApplicationContext(),"생일 형식이 틀렸습니다.",Toast.LENGTH_SHORT).show();
                        return;
                    } else if(st[2].length()!=2){
                        Toast.makeText(getApplicationContext(),"생일 형식이 틀렸습니다.",Toast.LENGTH_SHORT).show();
                        return;
                    } else if(Integer.parseInt(st[1]) < 1 || Integer.parseInt(st[1])>12){
                        Toast.makeText(getApplicationContext(),"생일 형식이 틀렸습니다.",Toast.LENGTH_SHORT).show();
                        return;
                    } else if(Integer.parseInt(st[2])<1||Integer.parseInt(st[2])>31){
                        Toast.makeText(getApplicationContext(),"생일 형식이 틀렸습니다.",Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"생일 형식이 틀렸습니다.",Toast.LENGTH_SHORT).show();
                    return;
                }

                //Toast.makeText(getApplicationContext(),vName+"/"+vId+"/"+vPwd+"/"+vPhone+"/"+vBirth+"/"+vGender,Toast.LENGTH_LONG).show();
                String requestURL =  "http://192.168.14.19:8805/meto/and/member/join.do";

                HttpClient client   = new DefaultHttpClient();
                HttpPost post    = new HttpPost(requestURL);
                List<NameValuePair> paramList = new ArrayList<>();
                paramList.add(new BasicNameValuePair("name" , vName));
                paramList.add(new BasicNameValuePair("id"   , vId));
                paramList.add(new BasicNameValuePair("pwd"  , vPwd));
                paramList.add(new BasicNameValuePair("phone" , vPhone));
                paramList.add(new BasicNameValuePair("gender" , vGender));
                paramList.add(new BasicNameValuePair("birth"  , vBirth));

                try {
                    post.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
                    HttpResponse response = client.execute(post);
                    /*Toast.makeText(getApplicationContext(), "mem DB  저장 확인 ",
                            Toast.LENGTH_SHORT).show();*/
                    finish();
                } catch(Exception e) {
                    Log.d("sendPost===> ", e.toString());
                }
            }
        });

        CancelBtn = (Button)findViewById(R.id.join_cancel_btn);
        CancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /*private void excute(Context context){
        this.context=context;
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.mem_ins,this,true);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        edtName = (EditText)findViewById(R.id.mem_name);
        edtId = (EditText) findViewById(R.id.mem_id);
        edtPwd = (EditText)findViewById(R.id.mem_pwd);
        edtPhone = (EditText)findViewById(R.id.mem_phone);
        edtEmail = (EditText)findViewById(R.id.mem_email);
        edtAddr = (EditText)findViewById(R.id.mem_addr);
        btnSend = (Button)findViewById(R.id.mem_join);
        btnSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                vName = edtName.getText().toString();
                vId = edtId.getText().toString();
                vPwd = edtPwd.getText().toString();
                vPhone = edtPhone.getText().toString();
                vEmail = edtEmail.getText().toString();
                vAddr = edtAddr.getText().toString();
                p_code = "mvcIns";
                Toast.makeText(getContext(),
                        "name="+vName+"id="+vId+" pwd"+vPwd+" phone"+vPhone+" email"+vEmail+" address"+vAddr,Toast.LENGTH_SHORT).show();
                String requestURL =  "http://192.168.14.19:8805/DbServer/andController.do";

                HttpClient client   = new DefaultHttpClient();
                HttpPost post    = new HttpPost(requestURL);
                List<NameValuePair> paramList = new ArrayList<>();
                paramList.add(new BasicNameValuePair("mem_name" , vName));
                paramList.add(new BasicNameValuePair("mem_id"   , vId));
                paramList.add(new BasicNameValuePair("mem_pwd"  , vPwd));
                paramList.add(new BasicNameValuePair("mem_phone" , vPhone));
                paramList.add(new BasicNameValuePair("mem_email" , vEmail));
                paramList.add(new BasicNameValuePair("mem_addr"  , vAddr));
                paramList.add(new BasicNameValuePair("p_code"  , p_code));

                try {
                    post.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
                    HttpResponse response = client.execute(post);
                    Toast.makeText(getContext(), "mem DB  저장 확인 ",
                            Toast.LENGTH_SHORT).show();
                } catch(Exception e) {
                    Log.d("sendPost===> ", e.toString());
                }
            }
        });
    }*/

}
