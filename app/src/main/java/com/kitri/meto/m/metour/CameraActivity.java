package com.kitri.meto.m.metour;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btn,btntrans;
    private ImageView imgv;
    final int REQ_CODE_SELECT_IMAGE=100;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_layout);
       /* btn = (Button)findViewById(R.id.button);
        btntrans=(Button)findViewById(R.id.btntrans);
        imgv=(ImageView)findViewById(R.id.iv);

        btn.setOnClickListener(this);
        btntrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doInBackground();
            }
        });*/


        /*btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,1);
            }
        });*/
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Toast.makeText(getBaseContext(), "resultCode : "+resultCode,Toast.LENGTH_SHORT).show();
        String sdcard = Environment.getExternalStorageDirectory().getPath();
        if(requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    //Uri에서 이미지 이름을 얻어온다.
                    String name_Str = getImageNameToUri(data.getData());

                    //이미지 데이터를 비트맵으로 받아온다.
                    Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());


                    //배치해놓은 ImageView에 set
                    imgv.setImageBitmap(image_bitmap);


                    /*Toast.makeText(getBaseContext(), "name_Str : " + name_Str, Toast.LENGTH_SHORT).show();
                    HttpClient client = new DefaultHttpClient();
                    String url = "http://175.210.155.212:8080/diary_server/getMultipart.jsp"; //바꿔야함
                    HttpPost post = new HttpPost(url);

                    // FileBody 객체를 이용해서 파일을 받아옴

                    File glee = new File(sdcard + "/glee.jpg");
                    FileBody bin = new FileBody(glee);


                    MultipartEntityBuilder multipart = MultipartEntityBuilder.create();
                    multipart.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                    multipart.addPart("images", bin); //실제 파일을 multipart에 넣는다.

                    post.setEntity((HttpEntity) multipart); // Multipart를 post 형식에 담음
                    client.execute(post);    // post 형식의 데이터를 서버로 전달*/


                   /* Toast.makeText(getApplicationContext(),doInBackground(),Toast.LENGTH_SHORT).show();*/
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*protected String doInBackground(String... params) {
        String sdcard = Environment.getExternalStorageDirectory().getPath();

        String url = "http://192.168.14.47:8805/meto/and/share/trans.do";
        // 파일을 서버로 보내는 부분
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        File glee = new File(sdcard+"/my.db");
        FileBody bin = new FileBody(glee);

        MultipartEntityBuilder meb = MultipartEntityBuilder.create();
        meb.setCharset(Charset.forName("UTF-8"));
        meb.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        meb.addPart("database", bin);
        HttpEntity entity = meb.build();

        post.setEntity(entity);

        try {
            HttpResponse reponse = client.execute(post);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } // post 형식의 데이터를 서버로 전달

        return "SUCCESS";
    }*/


    public String getImageNameToUri(Uri data)
    {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        String imgPath = cursor.getString(column_index);
        String imgName = imgPath.substring(imgPath.lastIndexOf("/")+1);

        return imgName;
    }

    @Override
    public void onClick(View v)
    {
        DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                doTakePhotoAction();
            }
        };

        DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                doTakeAlbumAction();
            }
        };

        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        };

        new AlertDialog.Builder(this)
                .setTitle("업로드할 이미지 선택")
                .setPositiveButton("사진촬영", cameraListener)
                .setNeutralButton("앨범선택", albumListener)
                .setNegativeButton("취소", cancelListener)
                .show();
    }


    private void doTakePhotoAction(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }



    private void doTakeAlbumAction(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
    }

    /*
    @Override
    public void onClick(View v){
        if(v.getId()==btn.getId()){
            Intent intent = new Intent(Intent.ACTION_PICK);
            startActivityForResult(intent,REQ_CAMERA_SELECT);
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==100){
            if(resultCode== Activity.RESULT_OK){
                try{
                    Log.d("Real path is: ",getImagePath(data.getData()));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }*/
}