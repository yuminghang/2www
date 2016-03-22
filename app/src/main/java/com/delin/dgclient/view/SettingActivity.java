package com.delin.dgclient.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.delin.dgclient.R;
import com.delin.dgclient.service.PositionAccuracyBSSave;

import java.text.DecimalFormat;
import java.util.Arrays;

/**
 * Created by Administrator on 2015/12/1 0001.
 */
public class SettingActivity extends BaseActivity {
    private SwitchButton switchButton;
    private LinearLayout goBackLinearLayout;
    private TextView titleText;
    private static final String[] PLANETS = new String[]{"2.00", "2.25", "2.50", "2.75",
            "3.00", "3.50", "4.00", "5.00"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        WheelView wva = (WheelView) findViewById(R.id.main_wv);

        wva.setOffset(2);
        wva.setItems(Arrays.asList(PLANETS));
        int selection=3;
        DecimalFormat df = new DecimalFormat("######0.00");
        for (int i = 0; i <PLANETS.length ; i++) {
            if (PLANETS[i].equals(df.format(applicationContext.getPositioningAccuracy()))){
                selection = i;
                break;
            }
        }
        wva.setSeletion(selection);
        wva.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                applicationContext.setPositioningAccuracy(Double.parseDouble(item));
                showShortToast("当前定位精度为" + item + "m");
            }
        });
        switchButton = (SwitchButton) findViewById(R.id.switchButton);
        switchButton.setmSwitchOn(applicationContext.isCanGetPush());
        switchButton.setOnChangeListener(new SwitchButton.OnChangeListener() {
            @Override
            public void onChange(SwitchButton sb, boolean state) {
                Toast.makeText(SettingActivity.this, state ? "接收" : "拒绝", Toast.LENGTH_SHORT).show();
                applicationContext.setCanGetPush(state);
            }
        });
        goBackLinearLayout = (LinearLayout) findViewById(R.id.goBackLinearLayout);
        titleText = (TextView) findViewById(R.id.titleTextView);
        goBackLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                SaveUserSetting();
            }
        });
        Intent receive = getIntent();
        String flog = receive.getStringExtra("flog");
        titleText.setText(flog);
    }

    private void SaveUserSetting(){

        PositionAccuracyBSSave positionAccuracyBSSave = new PositionAccuracyBSSave(this);
        positionAccuracyBSSave.setPositionAccuracy(applicationContext.getPositioningAccuracy());
        positionAccuracyBSSave.setCanGetPush(applicationContext.isCanGetPush());
        runBizService(positionAccuracyBSSave);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SaveUserSetting();
    }
}
