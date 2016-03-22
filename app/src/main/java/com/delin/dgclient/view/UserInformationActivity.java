package com.delin.dgclient.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.delin.dgclient.R;
import com.delin.dgclient.notification.Notification;
import com.delin.dgclient.notification.NotificationCenter;
import com.delin.dgclient.notification.NotificationName;
import com.delin.dgclient.service.PhotoBSUpload;
import com.delin.dgclient.service.PositionAccuracyBSLoad;
import com.myideaway.easyapp.core.lib.BizCookie;
import com.myideaway.easyapp.core.lib.service.Service;

import java.util.HashMap;


/**
 * Created by Administrator on 2015/12/2 0002.
 */
public class UserInformationActivity extends BaseActivity {
    private LinearLayout goBackLinearLayout;
    private LinearLayout nickNameLinearLayout;
    private LinearLayout sexLinearLayout;
    private LinearLayout nameLinearLayout;
    private LinearLayout passwordLinearLayout;
    private LinearLayout headPicLinearLayout;

    private TextView niceNameTextView;
    private TextView sexTextView;
    private TextView nameTextView;
    private CustomImageView baiLinImageView;
    private static final String PHOTO_PATH = Environment.getExternalStorageDirectory()+"/dl.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_set_information);
        goBackLinearLayout = (LinearLayout) findViewById(R.id.goBackLinearLayout);
        goBackLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        nickNameLinearLayout = (LinearLayout) findViewById(R.id.nickNameLinearLayout);
        sexLinearLayout = (LinearLayout) findViewById(R.id.sexLinearLayout);
        nameLinearLayout = (LinearLayout) findViewById(R.id.nameLinearLayout);
        passwordLinearLayout = (LinearLayout) findViewById(R.id.passwordLinearLayout);
        headPicLinearLayout = (LinearLayout) findViewById(R.id.headPicLinearLayout);
        baiLinImageView = (CustomImageView) findViewById(R.id.baiLinImageView);
        headPicLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(getBaseContext(),GetLocalPhotoActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        nickNameLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserInformationActivity.this,EditInforActivity.class);
                intent.putExtra("from","昵称");
                startActivityForResult(intent, 1);
            }
        });
        sexLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserInformationActivity.this,EditInforActivity.class);
                intent.putExtra("from","性别");
                startActivityForResult(intent, 2);
            }
        });
        nameLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserInformationActivity.this,EditInforActivity.class);
                intent.putExtra("from","姓名");
                startActivityForResult(intent, 3);
            }
        });
        passwordLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserInformationActivity.this,EditInforActivity.class);
                intent.putExtra("from","密码");
                startActivity(intent);
            }
        });


        niceNameTextView = (TextView) findViewById(R.id.niceNameTextView);
        nameTextView = (TextView) findViewById(R.id.nameTextView);
        sexTextView = (TextView) findViewById(R.id.sexTextView);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK)
        switch (requestCode){
            case 0:
                byte [] bis=data.getByteArrayExtra("bitmap");
                Bitmap bitmap = BitmapFactory.decodeByteArray(bis, 0, bis.length);
                baiLinImageView.setImageBitmap(bitmap);
                submitPhoto(bitmap);
            case 1:
                niceNameTextView.setText(data.getStringExtra("result"));
                break;
            case 2:
                sexTextView.setText(data.getStringExtra("result"));
                break;
            case 3:
                nameTextView.setText(data.getStringExtra("result"));
                break;
        }
    }


    private void submitPhoto(final Bitmap bitmap ) {
        showProgressDialog(this, "正在处理...");
        PhotoBSUpload photoBSSubmit = new PhotoBSUpload(getBaseContext());
        photoBSSubmit.setPhotoPath(PHOTO_PATH);

        runBizService(photoBSSubmit, new Service.OnCompleteHandler() {
            @Override
            public void onComplete(Service service) {
                dismissProgressDialog();
            }
        }, new Service.OnSuccessHandler() {
            @Override
            public void onSuccess(Service service, Object o) {
                PhotoBSUpload.ServiceResult serviceResult = (PhotoBSUpload.ServiceResult) o;
                HashMap hashMap = serviceResult.getHashMap();
                if (hashMap.get("status").equals("error")){
                    showShortToast((String)hashMap.get("msg"));
                }else {
                   showShortToast("头像修改成功");
                    NotificationCenter.getInstance().sendNotification(NotificationName.HEAD_PHOTO_CHANGE, bitmap);
                }

            }
        });
    }
}
