package com.delin.dgclient.view;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.delin.dgclient.ApplicationContext;
import com.delin.dgclient.R;
import com.delin.dgclient.api.Config;
import com.delin.dgclient.notification.NotificationCenter;
import com.delin.dgclient.notification.NotificationName;
import com.delin.dgclient.service.UserBSSaveLogin;
import com.delin.dgclient.service.VerificationBSGet;
import com.delin.dgclient.util.HttpHelper;
import com.delin.dgclient.util.JSONUtil;
import com.google.gson.internal.LinkedTreeMap;
import com.myideaway.easyapp.core.lib.L;
import com.myideaway.easyapp.core.lib.service.Service;

import java.util.HashMap;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by Administrator on 2015/9/18 0018.
 */
public class RegisterActivity extends BaseActivity {
    private LinearLayout goBackLinearLayout;
    private EditText phoneEditText;
    private EditText passwordEditText;
    private EditText rePasswordEditText;
    private Button registerButton;
    private CheckBox agreeCheckbox;
    private HashMap result;
    private TextView getVerificationCodeTextView;
    private EditText verificationCodeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        goBackLinearLayout = (LinearLayout) findViewById(R.id.goBackLinearLayout);
        phoneEditText = (EditText) findViewById(R.id.phoneEditText);
        getVerificationCodeTextView = (TextView) findViewById(R.id.getVerificationCodeTextView);
        agreeCheckbox  = (CheckBox) findViewById(R.id.agreeCheckbox);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        rePasswordEditText = (EditText) findViewById(R.id.rePasswordEditText);
        registerButton = (Button) findViewById(R.id.registerButton);
        getVerificationCodeTextView = (TextView) findViewById(R.id.getVerificationCodeTextView);
        verificationCodeEditText = (EditText) findViewById(R.id.verificationCodeEditText);
        goBackLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
        getVerificationCodeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobile = phoneEditText.getText().toString();
                if (isMobileNO(mobile)){
                    waiting();
                    getVerificationCode(mobile,"1");
                }else {
                    showShortToast("请输入正确的手机号码");
                }
            }
        });

    }

    private void register(){
        HashMap params = new HashMap();
        final String mobile =phoneEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String rePassword = rePasswordEditText.getText().toString();
        String verificationCode = verificationCodeEditText.getText().toString();
        if (!isMobileNO(mobile)){
            showShortToast("请输入正确的手机号码");
        }else if (verificationCode.equals("")){
            showShortToast("请输入验证码");
        }else if (password.equals("")){
            showShortToast("请输入密码");
        }else if (rePassword.equals("")){
            showShortToast("请确认密码");
        }else {
            params.put("mobile",mobile);
            params.put("code",verificationCode);
            params.put("password",password);
            params.put("repassword", rePassword);
            final HttpHelper httpHelper = new HttpHelper(Config.URL_SERVER_USER_REGISTER, params,null) {

                @Override
                protected void onPostExecute(Integer integer) {
                    if (integer==TASK_RESULT_SUCCESS){
                        HashMap hashMap = JSONUtil.toHashMap(this.getResult());
                        if (hashMap.get("status").equals("success")){

                            JPushInterface.setAlias(getBaseContext(), mobile, new TagAliasCallback() {
                                @Override
                                public void gotResult(int i, String s, Set<String> set) {
                                    if (i==0){
                                        L.d("别名绑定成功");
                                        L.d("当前别名是" + s);
                                    }
                                }
                            });
                            showShortToast("注册成功");
                            LinkedTreeMap linkedTreeMap = (LinkedTreeMap) hashMap.get("msg");
                            String token = (String) linkedTreeMap.get("token");
                            ApplicationContext.getInstance().setToken(token);
                            finish();
                            NotificationCenter.getInstance().sendNotification(NotificationName.LOGIN_SUCCESS, linkedTreeMap);
                        }else {
                            showShortToast((String)hashMap.get("msg"));
                        }

                    }else {
                        showShortToast("网络异常");
                    }
                    progressDialog.dismiss();
                }
            };

            httpHelper.showProgressDialog(RegisterActivity.this, "loading......");
            httpHelper.execute();
        }


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
        String telRegex = "[1][3578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    private void getVerificationCode(String mobile,String isRegister){
        VerificationBSGet verificationBSGet = new VerificationBSGet(this);
        verificationBSGet.setMobile(mobile);
        verificationBSGet.setIsRegist(isRegister);
        runBizService(verificationBSGet, new Service.OnCompleteHandler() {
            @Override
            public void onComplete(Service service) {

            }
        }, new Service.OnSuccessHandler() {
            @Override
            public void onSuccess(Service service, Object o) {
                VerificationBSGet.ServiceResult serviceResult = (VerificationBSGet.ServiceResult) o;
                result = serviceResult.getResult();
                if (result.get("status").equals("error")){
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
                if (result!=null&&result.get("status").equals("error")){
                    values[0] =-1;
                }
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
}
