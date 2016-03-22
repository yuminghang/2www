package com.delin.dgclient.view;


import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.delin.dgclient.R;
import com.delin.dgclient.api.Config;
import com.delin.dgclient.notification.NotificationCenter;
import com.delin.dgclient.notification.NotificationName;
import com.delin.dgclient.util.HttpHelper;
import com.delin.dgclient.util.JSONUtil;

import java.util.HashMap;

/**
 * Created by Administrator on 2015/11/10 0010.
 */
public class StartGameActivity extends BaseActivity {
    private Button startGameButton;
    private Button gameRuleButton;
    private int num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);
        startGameButton = (Button) findViewById(R.id.startGameButton);
        gameRuleButton = (Button) findViewById(R.id.gameRuleButton);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getBunNumber();
            }
        });
        gameRuleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartGameActivity.this,RulerActivity.class);
                startActivity(intent);

            }
        });
        findViewById(R.id.goBackButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    /**
     * 打开蓝牙启动云子SDK
     */
    private void initSensoro(){
        if(!mainApplication.isBluetoothEnabled()){
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQUEST_ENABLE_BT);
        }else{

            startGame();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT && resultCode==-1) {

            startGame();
        }
    }

    private void startGame(){
        HashMap hashMap = new HashMap();
        hashMap.put("game", true);
        hashMap.put("num", num);
        NotificationCenter.getInstance().sendNotification(NotificationName.IS_FROM_GAME, hashMap);
        Intent intent = new Intent(StartGameActivity.this,MapActivity.class);
        intent.putExtra("game", true);
        intent.putExtra("num", num);
        startActivity(intent);
        mainApplication.startSensoroSDK();
    }


    private void getBunNumber() {
        HttpHelper httpHelper = new HttpHelper(Config.URL_SERVER_GET_BUN, applicationContext.getCookie()) {
            @Override
            protected void onPostExecute(Integer integer) {
                HashMap hashMap = JSONUtil.toHashMap(this.getResult());
                if (integer == TASK_RESULT_SUCCESS) {
                    if (hashMap.get("status").equals("success")) {
                        String number = (String) hashMap.get("msg");
                        num = Integer.parseInt(number);
                        initSensoro();
                    }else {
                        showShortToast(hashMap.get("msg").toString());
                    }
                } else {
                    showShortToast("网络异常，请稍后重试");
                }
                progressDialog.dismiss();
            }
        };
        httpHelper.showProgressDialog(StartGameActivity.this,"正在处理...");
        httpHelper.execute();
    }

}
