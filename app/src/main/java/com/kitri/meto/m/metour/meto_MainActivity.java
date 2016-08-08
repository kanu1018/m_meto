package com.kitri.meto.m.metour;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class meto_MainActivity extends AppCompatActivity{
    private Button btn;
    private ImageView imgv;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        btn = (Button)findViewById(R.id.button);
        imgv=(ImageView)findViewById(R.id.iv);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,1);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        imgv.setImageURI(data.getData());
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
    }
    private String getImagePath(Uri data){
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this,data,proj,null,null,null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }*/


}

