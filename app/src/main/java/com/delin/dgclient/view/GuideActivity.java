package com.delin.dgclient.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.delin.dgclient.R;
import com.delin.dgclient.service.AppBSDoFirstRun;
import com.delin.dgclient.service.AppBSIsFirstRun;

import com.myideaway.easyapp.core.lib.L;



/**
 * Created by cdm on 15/6/10.
 */
public class GuideActivity extends BaseFragmentActivity {
    private ViewPager guideViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        guideViewPager = (ViewPager) findViewById(R.id.guideViewPager);
        // 判断程序是否第一次运行
        AppBSIsFirstRun appBSIsFirstRun = new AppBSIsFirstRun(this);
        try {
            AppBSIsFirstRun.ServiceResult serviceResult = (AppBSIsFirstRun.ServiceResult) appBSIsFirstRun.syncExecute();
            boolean isFirstRun = (Boolean)serviceResult.getData();
            if (!isFirstRun) {
                gotoMainActivity();
                return;
            }
        } catch (Exception e) {
            L.e(e.getMessage(), e);
        }

        guideViewPager.setAdapter(new GuideViewPagerAdapter());
    }

    private void gotoMainActivity() {
        AppBSDoFirstRun appBSDoFirstRun = new AppBSDoFirstRun(this);
        try {
            appBSDoFirstRun.syncExecute();
        } catch (Exception e) {
            L.e(e.getMessage(), e);
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    class GuideViewPagerAdapter extends PagerAdapter {
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());



        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = null;

            if (position == 0) {
                view = inflater.inflate(R.layout.item_guide_1, container, false);
            } else if (position == 1) {
                view = inflater.inflate(R.layout.item_guide_2, container, false);
            } else {
                view = inflater.inflate(R.layout.item_guide_3, container, false);
                Button entryButton = (Button) view.findViewById(R.id.entryButton);
                entryButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        gotoMainActivity();
                    }
                });
            }

            container.addView(view);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
