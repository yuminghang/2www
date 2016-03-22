package com.delin.dgclient.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.delin.dgclient.ApplicationContext;
import com.delin.dgclient.R;
import com.delin.dgclient.api.Config;
import com.delin.dgclient.util.HttpHelper;
import com.delin.dgclient.util.JSONUtil;
import com.google.gson.internal.LinkedTreeMap;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.HashMap;

/**
 * Created by Administrator on 2015/10/12 0012.
 */
public class PushDetailActivity extends BaseActivity {
    private TextView storeNameTextView;
    private TextView cNameTextView;
    private TextView summaryTextView;
    private ImageView detailImageView;
    private TextView startTimeTextView;
    private LinearLayout goBackLinearLayout;

    private DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_detail);
        Intent intent = getIntent();
        String cid = intent.getStringExtra("cId");
        getCoupon(cid);
        storeNameTextView = (TextView) findViewById(R.id.storeNameTextView);
        cNameTextView = (TextView) findViewById(R.id.cNameTextView);
        summaryTextView = (TextView) findViewById(R.id.summaryTextView);
        detailImageView = (ImageView) findViewById(R.id.detailImageView);
        startTimeTextView = (TextView) findViewById(R.id.startTimeTextView);
        goBackLinearLayout = (LinearLayout) findViewById(R.id.goBackLinearLayout);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.yuanjiaojuxing08_09) // resource or drawable
                .showImageForEmptyUri(R.mipmap.yuanjiaojuxing08_09) // resource or drawable
                .showImageOnFail(R.mipmap.yuanjiaojuxing08_09) // resource or drawable
                .cacheInMemory(true) // default
                .cacheOnDisk(false) // default
                .considerExifParams(true)
                .build();

        goBackLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 获取推送详情
     * @param cId
     */
    private void getCoupon(String cId){
        HashMap params = new HashMap();
        params.put("c_id", cId);
        HttpHelper httpHelper = new HttpHelper(Config.URL_SERVER_PUSH_DETAIL,params, ApplicationContext.getInstance().getCookie()) {
            @Override
            protected void onPostExecute(Integer integer) {
                String result = this.getResult();
                HashMap hashMap = JSONUtil.toHashMap(result);
                if (hashMap.get("status").equals("success")){
                    LinkedTreeMap linkedTreeMap = (LinkedTreeMap) hashMap.get("msg");
                    storeNameTextView.setText((String)linkedTreeMap.get("store_name"));
                    cNameTextView.setText((String)linkedTreeMap.get("c_name"));
                    summaryTextView.setText((String)linkedTreeMap.get("summary"));
                    startTimeTextView.setText(linkedTreeMap.get("start_time")+"至"+linkedTreeMap.get("end_time"));
                    ImageLoader.getInstance().displayImage(Config.URL_SERVAE_COUPON_IMAGE+linkedTreeMap.get("pic1"),detailImageView,options);
                }
            }
        };
        httpHelper.execute();
    }
}
