package com.delin.dgclient.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.delin.dgclient.R;
import com.nostra13.universalimageloader.utils.L;



import java.io.ByteArrayOutputStream;

import java.io.File;
import java.io.IOException;

/**
 * Created by mashuai on 15/7/22.
 */
public class GetLocalPhotoActivity extends BaseActivity {
    public static final int NONE = 0;
    public static final int PHOTOHRAPH = 1;
    public static final int PHOTOZOOM = 2;
    public static final int PHOTORESOULT = 3;


    private Button takePhotoButton;
    private Button pickPhotoButton;
    private Button cancelButton;


    private static final String PHOTO_PATH =Environment.getExternalStorageDirectory()+"/dl.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_local_photo);

        takePhotoButton = (Button) findViewById(R.id.takePhotoButton);
        pickPhotoButton = (Button) findViewById(R.id.pickPhotoButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);


        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File outputImage = new File(Environment.getExternalStorageDirectory(),"temp.jpg");

                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();

                } catch (IOException e) {
                    L.d(e.toString());
                }

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra("camerasensortype", 2);// 调用前置摄像头
                intent.putExtra("autofocus", true);// 自动对焦
                intent.putExtra("fullScreen", true);// 全屏
                intent.putExtra("showActionIcons", false);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputImage));
                startActivityForResult(intent, PHOTOHRAPH);

            }
        });
        pickPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PHOTOZOOM);
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    //实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == NONE)
            return;

        if (requestCode == PHOTOHRAPH) {
            File picture = new File(Environment.getExternalStorageDirectory()
                    + "/temp.jpg");
            startPhotoZoom(Uri.fromFile(picture));
        }

        if (data == null)
            return;

        if (requestCode == PHOTOZOOM) {
            startPhotoZoom(data.getData());
        }
        if (requestCode == PHOTORESOULT) {
            Bitmap bitmap = BitmapFactory.decodeFile(PHOTO_PATH);
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte [] bitmapByte =baos.toByteArray();
            Intent intent = new Intent();
            intent.putExtra("bitmap", bitmapByte);
            setResult(RESULT_OK,intent);
            finish();
        }
    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri,"image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 296);
        intent.putExtra("outputY", 296);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());// 返回格式
        intent.putExtra("return-data", true);
        File file = new File(PHOTO_PATH);
        if (file.exists()){
            file.delete();
        }

        intent.putExtra("output", Uri.fromFile(file));
        startActivityForResult(intent, PHOTORESOULT);
    }



}
