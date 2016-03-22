package com.delin.dgclient;

import android.app.Application;
import android.content.Context;



import com.delin.dgclient.api.Config;
import com.delin.dgclient.db.DBHelper;

import com.delin.dgclient.notification.NotificationCenter;
import com.delin.dgclient.notification.NotificationName;


import com.fengmap.android.FMMapSDK;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.sensoro.beacon.kit.Beacon;
import com.sensoro.beacon.kit.BeaconManagerListener;
import com.sensoro.cloud.SensoroManager;

import java.util.ArrayList;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2015/7/12.
 */
public class MainApplication extends Application implements BeaconManagerListener {
    public static SensoroManager sensoroManager;
    private String serialNumber = "";
    private ArrayList<com.delin.dgclient.model.Beacon> mBeacons;

    @Override
    public void onCreate() {
        super.onCreate();
        initSensoroSDK();
        Config.init(getApplicationContext());
        initImageLoader(getApplicationContext());
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        FMMapSDK.init(this,getString(R.string.FMap_key));
        BeaconDataInit();
    }

    /**
     * 初始化imageLoader
     * @param context
     */
    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);

    }

    /**
     * 初始化云子SDK
     */
    private void initSensoroSDK() {
        sensoroManager = SensoroManager.getInstance(getApplicationContext());
        sensoroManager.setCloudServiceEnable(true);
        sensoroManager.setBeaconManagerListener(this);
    }

    /**
     *启动云子SDK
     */
    public void startSensoroSDK() {
        try {
            sensoroManager.startService();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Check whether bluetooth enabled.
     * @return
     */
    public boolean isBluetoothEnabled(){
        return sensoroManager.isBluetoothEnabled();
    }


    @Override
    public void onTerminate(){

        if(sensoroManager != null){
            sensoroManager.stopService();
        }
        super.onTerminate();
    }

    @Override
    public void onNewBeacon(Beacon beacon) {

    }

    @Override
    public void onGoneBeacon(Beacon beacon) {

    }

    @Override
    /**
     * 云子回调先找出当前距离最近的云子，当距离小于2.5m并且不等于之前存储测sn号时
     * 发送消息可以获取推送消息同时更新坐标位置
     * 获取推送消息是cookie不为空
     * 该回调每秒执行一次
     */
    public void onUpdateBeacon(ArrayList<Beacon> beacons) {
        int minPoint = 0;

        if (beacons.size()!=0){
            for (int i = 1; i <beacons.size()-1; i++) {
                if (beacons.get(i).getAccuracy()<beacons.get(minPoint).getAccuracy()){
                    minPoint = i-1;
                }
            }

            serialNumber = beacons.get(minPoint).getSerialNumber();

            if (beacons.get(minPoint).getAccuracy()<2) {
                serialNumber = beacons.get(minPoint).getSerialNumber();

                for (com.delin.dgclient.model.Beacon beacon: mBeacons) {
                    if (beacon.getBeacon_sn()!=null
                            &&beacon.getBeacon_sn().equals(serialNumber)){
                        NotificationCenter.getInstance().sendNotification(NotificationName.LOCATION_CHANGE,beacon);
                        break;
                    }
                }
                if(ApplicationContext.getInstance().getCookie()!=null
                        &&!ApplicationContext.getInstance().getCookie().equals("")){
                    NotificationCenter.getInstance().sendNotification(NotificationName.CAN_GET_PUSH);

                }

            }



        }
    }

    /**
     * 读取数据库中存储的坐标值
     */
    private void BeaconDataInit() {
        DBHelper mDBHelper = DBHelper.instance(getBaseContext());
        if (!DBHelper.isDatabaseExist()) {
            mDBHelper.copyDatabaseFile(getBaseContext());
        }
        mBeacons = mDBHelper.select();
    }
}

