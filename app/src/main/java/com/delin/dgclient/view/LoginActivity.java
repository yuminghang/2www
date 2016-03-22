package com.delin.dgclient.view;


import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.delin.dgclient.ApplicationContext;
import com.delin.dgclient.R;
import com.delin.dgclient.api.Config;
import com.delin.dgclient.notification.NotificationCenter;
import com.delin.dgclient.notification.NotificationName;
import com.delin.dgclient.service.UserBSLoginFast;
import com.delin.dgclient.service.UserBSSaveLogin;
import com.delin.dgclient.service.UserBSLogin;
import com.delin.dgclient.service.VerificationBSGet;
import com.google.gson.internal.LinkedTreeMap;
import com.myideaway.easyapp.core.lib.BizCookie;
import com.myideaway.easyapp.core.lib.service.Service;



import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2015/9/18 0018.
 */

public class LoginActivity extends BaseActivity {

    private LinearLayout goBackLinearLayout;
    private Button loginButton;
    private EditText phoneEditText;
    private TextView getVerificationCodeTextView;
    private RadioButton codeRadioButton;
    private RadioButton passwordRadioButton;
    private TextView typeTextView;
    private EditText verificationCodeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        goBackLinearLayout = (LinearLayout) findViewById(R.id.goBackLinearLayout);
        loginButton = (Button) findViewById(R.id.loginButton);
        phoneEditText = (EditText) findViewById(R.id.phoneEditText);
        verificationCodeEditText = (EditText) findViewById(R.id.verificationCodeEditText);
        getVerificationCodeTextView = (TextView) findViewById(R.id.getVerificationCodeTextView);
        codeRadioButton = (RadioButton) findViewById(R.id.codeRadioButton);
        passwordRadioButton = (RadioButton) findViewById(R.id.passwordRadioButton);
        typeTextView = (TextView) findViewById(R.id.typeTextView);

        findViewById(R.id.registerTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        goBackLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!getVerificationCodeTextView.getText().toString().equals("忘记密码？")){
                    fastLogin();
                }else {
                    String name = phoneEditText.getText().toString();
                    String password = verificationCodeEditText.getText().toString();

                    if (name.equals("")) {
                        showShortToast("请输入您的手机号");
                    } else if (password.equals("")) {
                        showShortToast("请输入您的密码");
                    } else {
                        login(name, password);
                    }

                }

            }
        });

        codeRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typeTextView.setText("验证码");
                verificationCodeEditText.getText().clear();
                getVerificationCodeTextView.setText("获取验证码");
                verificationCodeEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                findViewById(R.id.codeLinearLayout).setVisibility(View.VISIBLE);
            }
        });

        passwordRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typeTextView.setText("密    码");
                getVerificationCodeTextView.setText("忘记密码？");
                verificationCodeEditText.getText().clear();
                verificationCodeEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                findViewById(R.id.codeLinearLayout).setVisibility(View.GONE);
            }
        });

        getVerificationCodeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobile = phoneEditText.getText().toString();
                if (isMobileNO(mobile)) {
                    waiting();
                    getVerificationCode(mobile, "0");

                } else {
                    showShortToast("请输入正确的手机号码");
                }
            }
        });

    }



    /**
     * 将token存储到本地
     *
     * @param token
     */

    private void saveToken(String token) {
        UserBSSaveLogin userBSSaveLogin = new UserBSSaveLogin(getBaseContext());
        userBSSaveLogin.setToken(token);
        runBizService(userBSSaveLogin);
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        /*
		移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		联通：130、131、132、152、155、156、185、186
		电信：133、153、180、189、（1349卫通）
		总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		*/
        String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    private void getVerificationCode(String mobile, String isRegist) {
        VerificationBSGet verificationBSGet = new VerificationBSGet(this);
        verificationBSGet.setMobile(mobile);
        verificationBSGet.setIsRegist(isRegist);
        runBizService(verificationBSGet, new Service.OnCompleteHandler() {
            @Override
            public void onComplete(Service service) {

            }
        }, new Service.OnSuccessHandler() {
            @Override
            public void onSuccess(Service service, Object o) {
                VerificationBSGet.ServiceResult serviceResult = (VerificationBSGet.ServiceResult) o;
                HashMap result = serviceResult.getResult();
                if (result.get("status").equals("error")) {
                    showShortToast((String) result.get("msg"));
                }
            }
        });
    }

    //计时器
    private void waiting() {
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                final Timer timer = new Timer(true);
                final int[] i = {120};
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        i[0]--;
                        Object[] objects = new Object[2];
                        objects[0] = i[0];
                        objects[1] = timer;
                        publishProgress(objects);
                    }
                }, 10, 1000);
                return null;
            }

            @Override
            protected void onProgressUpdate(Object[] values) {
                super.onProgressUpdate(values);
                getVerificationCodeTextView.setText("重新发送(" + values[0] + ")");
                getVerificationCodeTextView.setTextColor(Color.parseColor("#7d7d7d"));
                findViewById(R.id.lineLinearLayout).setBackgroundColor(Color.parseColor("#7d7d7d"));
                if (Integer.parseInt(values[0].toString()) <= 0) {
                    ((Timer) values[1]).cancel();
                    getVerificationCodeTextView.setText("重新发送");
                    getVerificationCodeTextView.setTextColor(Color.parseColor("#452b7c"));
                    findViewById(R.id.lineLinearLayout).setBackgroundColor(Color.parseColor("#452b7c"));
                    getVerificationCodeTextView.setEnabled(true);
                }
            }
        };
        getVerificationCodeTextView.setEnabled(false);
        asyncTask.execute();

    }


    private void fastLogin() {
        String mobile = phoneEditText.getText().toString();
        String code = verificationCodeEditText.getText().toString();
        if (mobile.equals("")) {
            showShortToast("请输入您的手机号");
        } else if (code.equals("")) {
            showShortToast("验证码不能为空");
        } else {

            showProgressDialog(this,"正在处理......");
            UserBSLoginFast userBSLoginFast = new UserBSLoginFast(this);
            userBSLoginFast.setCode(code);
            userBSLoginFast.setMobile(mobile);
            runBizService(userBSLoginFast, new Service.OnCompleteHandler() {
                @Override
                public void onComplete(Service service) {
                    dismissProgressDialog();
                }
            }, new Service.OnSuccessHandler() {
                @Override
                public void onSuccess(Service service, Object o) {
                    UserBSLoginFast.ServiceResult serviceResult = (UserBSLoginFast.ServiceResult) o;
                    HashMap result = serviceResult.getResult();

                    if (result.get("status").equals("success")) {
                        LinkedTreeMap linkedTreeMap = (LinkedTreeMap) result.get("msg");
                        String token = (String) linkedTreeMap.get("token");
                        ApplicationContext.getInstance().setToken(token);
                        saveToken(token);
                        finish();
                        NotificationCenter.getInstance().sendNotification(NotificationName.LOGIN_SUCCESS, linkedTreeMap);
                    } else {
                        showShortToast((String) result.get("msg"));
                    }
                }
            });
        }
    }

    private void login(String name,String password){
        showProgressDialog(this,"正在处理......");
        UserBSLogin userBSLogin = new UserBSLogin(this);
        userBSLogin.setName(name);
        userBSLogin.setPassword(password);
        runBizService(userBSLogin, new Service.OnCompleteHandler() {
            @Override
            public void onComplete(Service service) {
                dismissProgressDialog();
            }
        }, new Service.OnSuccessHandler() {
            @Override
            public void onSuccess(Service service, Object o) {
                UserBSLogin.ServiceResult serviceResult = (UserBSLogin.ServiceResult) o;
                HashMap hashMap = serviceResult.getHashMap();
                if (hashMap.size()!=0&&hashMap.get("status").equals("success")) {
                    LinkedTreeMap linkedTreeMap = (LinkedTreeMap) hashMap.get("msg");
                    String token = (String) linkedTreeMap.get("token");
                    ApplicationContext.getInstance().setToken(token);
                    ApplicationContext.getInstance().setCookie(BizCookie.initBizCookie().getCookie());
                    saveToken(token);
                    finish();
                    NotificationCenter.getInstance().sendNotification(NotificationName.LOGIN_SUCCESS, linkedTreeMap);
                } else {
                    if (hashMap.size()==0){
                        showShortToast("服务器异常");
                    }else {
                        showShortToast((String) hashMap.get("msg"));
                    }
                }
            }
        });


    }
}
