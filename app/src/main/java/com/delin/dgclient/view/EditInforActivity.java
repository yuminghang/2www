package com.delin.dgclient.view;


import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.delin.dgclient.ApplicationContext;
import com.delin.dgclient.R;
import com.delin.dgclient.api.Config;
import com.delin.dgclient.service.UserBSInforEdit;
import com.delin.dgclient.util.HttpHelper;
import com.delin.dgclient.util.JSONUtil;
import com.myideaway.easyapp.core.lib.service.Service;

import java.util.HashMap;


/**
 * Created by Administrator on 2015/12/2 0002.
 */
public class EditInforActivity extends BaseActivity {
    private LinearLayout goBackLinearLayout;
    private TextView titleTextView;

    private LinearLayout nickNameLinearLayout;
    private TextView typeTextView;
    private EditText typeEditText;
    private TextView waringTextView;

    private LinearLayout sexLinearLayout;
    private RadioButton manRadioButton;
    private RadioButton womanRadioButton;
    private LinearLayout manLinearLayout;
    private LinearLayout womanLinearLayout;

    private LinearLayout passwordLinearLayout;
    private EditText oldPasswordEditText;
    private EditText newPasswordEditText;
    private CheckBox showPasswordCheckbox;

    private TextView saveTextView;
    private String from;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_information);
        initActivity();
        Intent intent = getIntent();
        from = intent.getStringExtra("from");
        titleTextView.setText(from);

        if (from.equals("昵称") || from.equals("姓名")) {
            nickNameLinearLayout.setVisibility(View.VISIBLE);
            passwordLinearLayout.setVisibility(View.GONE);
            sexLinearLayout.setVisibility(View.GONE);
            if (from.equals("昵称")) {
                typeTextView.setText("昵称");
                typeEditText.setHint("请输入新昵称");
                waringTextView.setText("设置后，其他人将看到你的昵称");

            } else {
                typeTextView.setText("姓名");
                typeEditText.setHint("请输入你的姓名");
                waringTextView.setText("设置后，其他人将看到你的姓名");

            }

        } else if (from.equals("性别")) {
            nickNameLinearLayout.setVisibility(View.GONE);
            passwordLinearLayout.setVisibility(View.GONE);
            sexLinearLayout.setVisibility(View.VISIBLE);

        } else {
            nickNameLinearLayout.setVisibility(View.GONE);
            passwordLinearLayout.setVisibility(View.VISIBLE);
            sexLinearLayout.setVisibility(View.GONE);
        }
        manLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manRadioButton.setChecked(true);
                womanRadioButton.setChecked(false);

            }
        });
        womanLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                womanRadioButton.setChecked(true);
                manRadioButton.setChecked(false);
            }
        });
        goBackLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        showPasswordCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    newPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                    oldPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                } else {
                    newPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    oldPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        saveTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (from.equals("性别")){
                    if (manRadioButton.isChecked()) {
                        result = "男";
                    } else {
                        result = "女";
                    }
                    returnResult();
                    finish();
                }else if(from.equals("昵称") || from.equals("姓名")) {
                    result = typeEditText.getText().toString();
                    editUserInfor(result);

                } else {
                    showShortToast("亲，你还没有填写修改项呢");
                }
            }
        });
    }

    private void initActivity() {
        titleTextView = (TextView) findViewById(R.id.titleTextView);

        goBackLinearLayout = (LinearLayout) findViewById(R.id.goBackLinearLayout);

        sexLinearLayout = (LinearLayout) findViewById(R.id.sexLinearLayout);
        manRadioButton = (RadioButton) findViewById(R.id.manRadioButton);
        womanRadioButton = (RadioButton) findViewById(R.id.womanRadioButton);
        manLinearLayout = (LinearLayout) findViewById(R.id.manLinearLayout);
        womanLinearLayout = (LinearLayout) findViewById(R.id.womanLinearLayout);

        nickNameLinearLayout = (LinearLayout) findViewById(R.id.nickNameLinearLayout);
        typeTextView = (TextView) findViewById(R.id.typeTextView);
        typeEditText = (EditText) findViewById(R.id.typeEditText);
        waringTextView = (TextView) findViewById(R.id.waringTextView);

        passwordLinearLayout = (LinearLayout) findViewById(R.id.passwordLinearLayout);
        oldPasswordEditText = (EditText) findViewById(R.id.oldPasswordEditText);
        newPasswordEditText = (EditText) findViewById(R.id.newPasswordEditText);
        showPasswordCheckbox = (CheckBox) findViewById(R.id.showPasswordCheckbox);
        saveTextView = (TextView) findViewById(R.id.saveTextView);
    }

    private void returnResult() {
        Intent intent = new Intent();
        intent.putExtra("result", result);
        setResult(RESULT_OK,intent);
    }

    private void editUserInfor(String result){
        showProgressDialog(this,"处理中...");
        UserBSInforEdit userBSInforEdit = new UserBSInforEdit(this);
        userBSInforEdit.setResult(result);
        runBizService(userBSInforEdit, new Service.OnCompleteHandler() {
            @Override
            public void onComplete(Service service) {
                dismissProgressDialog();
            }
        }, new Service.OnSuccessHandler() {
            @Override
            public void onSuccess(Service service, Object o) {
                UserBSInforEdit.ServiceResult serviceResult = (UserBSInforEdit.ServiceResult) o;
                HashMap hashMap = serviceResult.getHashMap();
                if (hashMap.get("status").equals("success")){
                    showShortToast("修改成功");
                    returnResult();
                    finish();
                }else {
                    showShortToast((String)hashMap.get("msg"));
                    finish();
                }
            }
        });

    }
}
