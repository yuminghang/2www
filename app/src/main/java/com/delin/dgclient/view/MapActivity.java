package com.delin.dgclient.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;

import com.delin.dgclient.R;
import com.delin.dgclient.db.DBHelper;
import com.delin.dgclient.model.Beacon;
import com.delin.dgclient.notification.Notification;
import com.delin.dgclient.notification.NotificationCenter;
import com.delin.dgclient.notification.NotificationListener;
import com.delin.dgclient.notification.NotificationName;
import com.delin.dgclient.service.CouponsBSGetPush;
import com.delin.dgclient.service.CouponsBSReceive;
import com.delin.dgclient.widge.MapManger;
import com.fengmap.android.map.FMViewMode;
import com.google.gson.internal.LinkedTreeMap;
import com.myideaway.easyapp.core.lib.service.Service;


import java.util.ArrayList;
import java.util.HashMap;


public class MapActivity extends BaseActivity implements View.OnClickListener {

    private MapManger fmMapView;
    private LinearLayout goBackLinearLayout;
    private LinearLayout searchLinearLayout;
    private LinearLayout changeFoolLinearLayout;
    private LinearLayout nearLinearLayout;
    private LinearLayout layerLinearLayout;
    private LinearLayout seeAnywhereLinearLayout;


    private ListPopupWindow listPopupWindow;
    private CustomDialog.Builder customDialog;
    private boolean serverIsRunning = false;
    private int width;
    private int height;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        initView();
        registerNotification();


    }

    private void initView() {
        goBackLinearLayout = (LinearLayout) findViewById(R.id.goBackLinearLayout);
        searchLinearLayout = (LinearLayout) findViewById(R.id.searchLinearLayout);
        changeFoolLinearLayout = (LinearLayout) findViewById(R.id.changeFoolLinearLayout);
        nearLinearLayout = (LinearLayout) findViewById(R.id.nearLinearLayout);
        layerLinearLayout = (LinearLayout) findViewById(R.id.layerLinearLayout);
        seeAnywhereLinearLayout = (LinearLayout) findViewById(R.id.seeAnywhereLinearLayout);
        fmMapView = (MapManger) findViewById(R.id.mapview);
        goBackLinearLayout.setOnClickListener(this);
        searchLinearLayout.setOnClickListener(this);
        changeFoolLinearLayout.setOnClickListener(this);
        nearLinearLayout.setOnClickListener(this);
        layerLinearLayout.setOnClickListener(this);
        seeAnywhereLinearLayout.setOnClickListener(this);

        ViewTreeObserver viewTreeObserver = changeFoolLinearLayout.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                changeFoolLinearLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                width = changeFoolLinearLayout.getWidth();
                height = changeFoolLinearLayout.getHeight();
            }
        });

    }






    @Override
    protected void onDestroy() {
        super.onDestroy();
        fmMapView.getFmMap().onDestory();
    }

    private void registerNotification() {
        final String[] notificationNames = {NotificationName.CAN_GET_PUSH,};
        NotificationCenter.getInstance().register(notificationNames, new NotificationListener() {
            @Override
            public void handleNotification(Notification notification) {
                if (notification.getName().equals(NotificationName.CAN_GET_PUSH)
                        && applicationContext.isCanGetPush()) {
                    if (serverIsRunning){
                        serverIsRunning = false;
                        return;
                    }
                    getPush();
                }
            }
        });
    }


    /**
     * 获取推送消息
     */
    private void getPush() {
        serverIsRunning =true;
        CouponsBSGetPush couponsBSGetPush = new CouponsBSGetPush(this);
        couponsBSGetPush.setIBeaconSN(applicationContext.getSerialNumber());
        runBizService(couponsBSGetPush,new Service.OnSuccessHandler() {
            @Override
            public void onSuccess(Service service, Object o) {
                CouponsBSGetPush.ServiceResult serviceResult = (CouponsBSGetPush.ServiceResult) o;
                HashMap hashMap = serviceResult.getHashMap();
                if (hashMap.get("status").equals("success")
                        && hashMap.get("msg").toString().length() > 5) {
                    LinkedTreeMap coupon = (LinkedTreeMap) hashMap.get("msg");
                    if (coupon != null && coupon.size() > 1 && coupon.get("type").equals("1")) {
                        clearDialog();
                        showCouponDialog(coupon);
                    } else {
                        showShortToast("服务器异常");
                    }
                }
            }
        });
    }

    /**
     * 领取优惠券
     * @param cid   店铺标识
     */
    private void getCoupon(String cid){
        CouponsBSReceive couponsBSReceive = new CouponsBSReceive(this);
        couponsBSReceive.setcId(cid);
        runBizService(couponsBSReceive,new Service.OnSuccessHandler() {
            @Override
            public void onSuccess(Service service, Object o) {
                CouponsBSReceive.ServiceResult serviceResult = (CouponsBSReceive.ServiceResult) o;
                HashMap<String, String> hashMap = serviceResult.getHashMap();
                if (hashMap.get("status").equals("success")) {
                    showShortToast(hashMap.get("msg"));
                }
            }
        });

    }
    private void clearDialog() {
        if (customDialog != null && customDialog.isCreated) {
            customDialog.dismiss();
            customDialog = null;
        }
    }

    /**
     * 显示优惠券弹窗
     * @param coupon 推送消息
     */
    private void showCouponDialog(final LinkedTreeMap coupon) {
        customDialog = new CustomDialog.Builder(this);
        customDialog.setMessage("接收到" + coupon.get("store_name"));
        customDialog.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getCoupon((String) coupon.get("c_id"));
                dialogInterface.dismiss();
            }
        });
        customDialog.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        customDialog.create().show();
    }



    @Override
    public void onClick(View view) {
        ArrayList<String> fool = new ArrayList<String>();
        fool.add("1f");
        fool.add("2f");
        fool.add("3f");
        ArrayList<String> near = new ArrayList<String>();
        near.add("直达电梯");
        near.add("扶梯");
        near.add("卫生间");
        near.add("服务台");

        switch (view.getId()) {
            case R.id.goBackLinearLayout:
                finish();
                break;

            case R.id.searchLinearLayout:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.changeFoolLinearLayout:
                initPopupWindow(fool, view);
                break;
            case R.id.nearLinearLayout:
                initPopupWindow(near, view);
                break;
            case R.id.layerLinearLayout:
                fmMapView.getFmMap().setFMViewMode(FMViewMode.FMVIEW_MODE_2D);
                fmMapView.getFmMap().updateMap();
                break;
            case R.id.seeAnywhereLinearLayout:
                fmMapView.getFmMap().setFMViewMode(FMViewMode.FMVIEW_MODE_3D);
                fmMapView.getFmMap().updateMap();
                break;
            default:
                break;
        }
    }

    /**
     * 初始化popupWindow
     *
     * @param arrayList
     * @param view
     */
    private void initPopupWindow(final ArrayList<String> arrayList, View view) {
        listPopupWindow = new ListPopupWindow(this);
        //自定义Adapter
        ListPopupWindowAdapter listPopupWindowAdapter = new ListPopupWindowAdapter(arrayList, this);
        listPopupWindow.setAdapter(listPopupWindowAdapter);
        listPopupWindow.setWidth(width);
        if (arrayList.size() >= 7) {
            listPopupWindow.setHeight(height * 4);
        } else {
            listPopupWindow.setHeight(height * (arrayList.size() - 1));
        }
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long k) {

                listPopupWindow.dismiss();
            }
        });

        listPopupWindow.setAnchorView(view);
        listPopupWindow.show();
    }
}
