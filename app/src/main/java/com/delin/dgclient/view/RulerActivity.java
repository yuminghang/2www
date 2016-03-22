package com.delin.dgclient.view;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.delin.dgclient.R;

import org.w3c.dom.Text;

/**
 * Created by Administrator on 2015/11/13 0013.
 */
public class RulerActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruler);
        findViewById(R.id.goBackLinearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/fangzhen.TTF");
        ((TextView)findViewById(R.id.eatTextView)).setTypeface(typeface);
        ((TextView)findViewById(R.id.rule1)).setTypeface(typeface);
        ((TextView)findViewById(R.id.rule2)).setTypeface(typeface);

    }
}
