package com.delin.dgclient.view;



import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;


import com.delin.dgclient.MainApplication;
import com.delin.dgclient.R;
import com.delin.dgclient.notification.NotificationCenter;
import com.delin.dgclient.notification.NotificationName;

import java.util.ArrayList;
import java.util.HashMap;

import com.delin.dgclient.library.PtrDefaultHandler;
import com.delin.dgclient.library.PtrFrameLayout;
import com.delin.dgclient.library.PtrHandler;


/**
 * Created by Administrator on 2015/9/18 0018.
 */
public class MapTab extends BaseFragment {
    private MainApplication mainApplication;
    private static int REQUEST_ENABLE_BT = 1;//

    private ListView mapTabListView;
    private PtrFrameLayout mapTabPtrFrame;
    @Override
    public int getViewId() {
        return R.layout.tab_map;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapTabListView = (ListView) view.findViewById(R.id.mapTabListView);
        mapTabPtrFrame = (PtrFrameLayout) view.findViewById(R.id.mapTabPtrFrame);
        View headerView = layoutInflater.inflate(R.layout.item_map_head,mapTabListView,false);
        Button mapButton = (Button) headerView.findViewById(R.id.mapButton);
        Button gameButton = (Button) headerView.findViewById(R.id.gameButton);
        Button storeButton = (Button) headerView.findViewById(R.id.storeButton);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBluetooth4()){
                    initSensoro();
                }else {
                    showShortToast("您的设备暂不支持蓝牙4.0");
                }
            }
        });
        gameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(baseContext,StartGameActivity.class);
                startActivity(intent);
            }
        });

        storeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(baseContext,SearchActivity.class);
                startActivity(intent);
            }
        });

        headerView.findViewById(R.id.parkButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(baseContext, ParkDetailActivity.class);
                startActivity(intent);
            }
        });
        mapTabListView.addHeaderView(headerView);
        ArrayList arrayList = new ArrayList();
        arrayList.add("");
        arrayList.add("");
        MapAdapter mapAdapter = new MapAdapter(getContext(),arrayList);
        mapTabListView.setAdapter(mapAdapter);
        mainApplication = (MainApplication) getActivity().getApplication();
        mapTabPtrFrame.setPtrHandler(new PtrHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mapTabPtrFrame.refreshComplete();
                    }
                }, 1800);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // 默认实现，根据实际情况做改动
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
        // the following are default settings
        mapTabPtrFrame.setResistance(1.7f);
        mapTabPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mapTabPtrFrame.setDurationToClose(200);
        mapTabPtrFrame.setDurationToCloseHeader(1000);
        // default is false
        mapTabPtrFrame.setPullToRefresh(false);
        // default is true
        mapTabPtrFrame.setKeepHeaderWhenRefresh(true);
        mapTabPtrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
//                mapTabPtrFrame.autoRefresh();
            }
        }, 100);

    }
    /**
     * 判断设备是否支持蓝牙4.0
     * @return boolean 支持返回true，否则返回false
     */
    private boolean isBluetooth4(){
        boolean isBluetooth4 = true;
        if(!baseContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)){
            isBluetooth4 = false;
        }

        return isBluetooth4;
    }

    /**
     * 打开蓝牙启动云子SDK
     */
    private void initSensoro(){
        if(!mainApplication.isBluetoothEnabled()){
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQUEST_ENABLE_BT);
        }else{
            mainApplication.startSensoroSDK();
            Intent intent = new Intent(getActivity(),MapActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT && resultCode==-1) {
            mainApplication.startSensoroSDK();
            Intent intent = new Intent(getActivity(),MapActivity.class);
            startActivity(intent);
        }
    }

    private class MapAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private ArrayList arrayList;

        public MapAdapter(Context context,ArrayList arrayList) {
            this.arrayList = arrayList;
            inflater = layoutInflater.from(context);
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
            if (view == null){
                view = inflater.inflate(R.layout.item_map,viewGroup,false);
            }
            return view;
        }
    }


}
