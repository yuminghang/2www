package com.delin.dgclient.view;

import android.os.Bundle;
import android.view.View;

import com.delin.dgclient.R;

/**
 * Created by Administrator on 2015/12/4 0004.
 */
public class ShareActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        findViewById(R.id.goBackLinearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
