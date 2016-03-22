package com.delin.dgclient.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.delin.dgclient.R;
import com.delin.dgclient.api.Config;
import com.delin.dgclient.notification.NotificationCenter;
import com.delin.dgclient.notification.NotificationName;
import com.delin.dgclient.service.StoreBSGetAll;
import com.delin.dgclient.service.StoreBSGetDetail;
import com.myideaway.easyapp.core.lib.service.Service;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.ogaclejapan.arclayout.ArcLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/3 0003.
 */
public class ShopDetailActivity extends BaseActivity {
    private ListView newProductListView;
    private LayoutInflater layoutInflater;
    private Button menuButton;
    ArcLayout arcLayout;
    View menuLayout;
    private String storeId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);
        initData();
        findViewById(R.id.goBackLinearLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        menuButton = (Button) findViewById(R.id.menuButton);
        menuLayout = findViewById(R.id.menu_layout);
        arcLayout = (ArcLayout) findViewById(R.id.arc_layout);
        newProductListView = (ListView) findViewById(R.id.newProductListView);

        findViewById(R.id.startMapTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBluetooth4()){
                    initSensoro(ShopDetailActivity.this,storeId);
                }else {
                    showShortToast("您的设备暂不支持蓝牙4.0");
                }
            }
        });
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFabClick(view);
            }
        });
    }

    private class NewShopAdapter extends BaseAdapter{
        private ArrayList arrayList;

        private LayoutInflater inflater;

        public NewShopAdapter(Context context, ArrayList arrayList) {
            this.arrayList = arrayList;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return arrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view==null){
                view = inflater.inflate(R.layout.item_new_product,viewGroup,false);
            }

            return view;
        }
    }
    private void onFabClick(View v) {
        if (v.isSelected()) {
            hideMenu();
        } else {
            showMenu();
        }
        v.setSelected(!v.isSelected());
    }

    private void showMenu() {
        menuLayout.setVisibility(View.VISIBLE);

        List<Animator> animList = new ArrayList();

        for (int i = 0, len = arcLayout.getChildCount(); i < len; i++) {
            animList.add(createShowItemAnimator(arcLayout.getChildAt(i)));
        }

        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(400);
        animSet.setInterpolator(new OvershootInterpolator());
        animSet.playTogether(animList);
        animSet.start();
    }
    private void hideMenu() {

        List<Animator> animList = new ArrayList();

        for (int i = arcLayout.getChildCount() - 1; i >= 0; i--) {
            animList.add(createHideItemAnimator(arcLayout.getChildAt(i)));
        }

        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(400);
        animSet.setInterpolator(new AnticipateInterpolator());
        animSet.playTogether(animList);
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                menuLayout.setVisibility(View.INVISIBLE);
            }
        });
        animSet.start();

    }

    private Animator createShowItemAnimator(View item) {

        float dx = menuButton.getX() - item.getX();
        float dy = menuButton.getY() - item.getY();

        item.setRotation(0f);
        item.setTranslationX(dx);
        item.setTranslationY(dy);

        Animator anim = ObjectAnimator.ofPropertyValuesHolder(
                item,
                AnimatorUtils.rotation(0f, 720f),
                AnimatorUtils.translationX(dx, 0f),
                AnimatorUtils.translationY(dy, 0f)
        );

        return anim;
    }

    private Animator createHideItemAnimator(final View item) {
        float dx = menuButton.getX() - item.getX();
        float dy = menuButton.getY() - item.getY();

        Animator anim = ObjectAnimator.ofPropertyValuesHolder(
                item,
                AnimatorUtils.rotation(720f, 0f),
                AnimatorUtils.translationX(0f, dx),
                AnimatorUtils.translationY(0f, dy)
        );

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                item.setTranslationX(0f);
                item.setTranslationY(0f);
            }
        });

        return anim;
    }


    private void initData(){
        Intent intent = getIntent();
        showProgressDialog(this, "正在加载数据......");
        layoutInflater = LayoutInflater.from(this);
        final DisplayImageOptions displayImageOptions =new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.logo_2_03) // resource or drawable
                .showImageForEmptyUri(R.mipmap.logo_2_03) // resource or drawable
                .showImageOnFail(R.mipmap.logo_2_03) // resource or drawable
                .cacheInMemory(true) // default
                .cacheOnDisk(false) // default
                .considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(180))
                .build();
        storeId= intent.getStringExtra("store_id");
        NotificationCenter.getInstance().sendNotification(NotificationName.STORE_ID_CHANGE,storeId);
        final StoreBSGetDetail storeBSGetDetail = new StoreBSGetDetail(this);
        storeBSGetDetail.setId(storeId);
        runBizService(storeBSGetDetail, new Service.OnCompleteHandler() {
            @Override
            public void onComplete(Service service) {
                dismissProgressDialog();
            }
        }, new Service.OnSuccessHandler() {
            @Override
            public void onSuccess(Service service, Object o) {
                StoreBSGetDetail.ServiceResult result = (StoreBSGetDetail.ServiceResult) o;
                View headView = layoutInflater.inflate(R.layout.item_shop_detail_head, null);

                ArrayList arrayList = new ArrayList();
                arrayList.add("1");
                arrayList.add("2");
                arrayList.add("");
                ImageView storeLogoImageView = (ImageView) headView.findViewById(R.id.storeLogoImageView);
                TextView storeNameTextView = (TextView) headView.findViewById(R.id.storeNameTextView);
                TextView storeTypeTextView = (TextView) headView.findViewById(R.id.storeTypeTextView);
                TextView storeLayerTextView = (TextView) headView.findViewById(R.id.storeLayerTextView);
                TextView commentsTextView = (TextView) headView.findViewById(R.id.commentsTextView);
                TextView favorTextView = (TextView) headView.findViewById(R.id.favorTextView);
                TextView favoritesTextView = (TextView) headView.findViewById(R.id.favoritesTextView);
                TextView shareTextView = (TextView) headView.findViewById(R.id.shareTextView);
                storeNameTextView.setText((String)result.getLinkedTreeMap().get("store_name"));
                storeTypeTextView.setText((String)result.getLinkedTreeMap().get("store_type"));
                storeLayerTextView.setText((String)result.getLinkedTreeMap().get("room"));
                commentsTextView.setText((String)result.getLinkedTreeMap().get("comments"));
                favorTextView.setText((String)result.getLinkedTreeMap().get("favars"));
                favoritesTextView.setText((String)result.getLinkedTreeMap().get("favorites"));
                shareTextView.setText((String)result.getLinkedTreeMap().get("shares"));
                ImageLoader.getInstance().displayImage(Config.URL_SERVER_IMAGE + result.getLinkedTreeMap().get("store_logo"), storeLogoImageView, displayImageOptions);
                newProductListView.addHeaderView(headView);
                NewShopAdapter newShopAdapter = new NewShopAdapter(getBaseContext(),arrayList);
                newProductListView.setAdapter(newShopAdapter);
            }
        });
    }
}
