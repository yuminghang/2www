package com.delin.dgclient.view;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.delin.dgclient.R;

public class SettingsActivity extends BaseActivity {
    private LinearLayout goBackLinearLayout;
    private TextView titleText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        goBackLinearLayout = (LinearLayout) findViewById(R.id.goBackLinearLayout);
        titleText = (TextView) findViewById(R.id.titleTextView);
        TextView text = (TextView) findViewById(R.id.textView);
        goBackLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent receive = getIntent();
        String flog = receive.getStringExtra("flog");
        text.setText(flog);
        titleText.setText(flog);
    }

}
