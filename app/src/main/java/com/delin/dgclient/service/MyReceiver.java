package com.delin.dgclient.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.delin.dgclient.db.DBHelper;
import com.delin.dgclient.model.Beacon;
import com.delin.dgclient.model.exception.AppException;
import com.delin.dgclient.util.JSONUtil;
import com.google.gson.internal.LinkedTreeMap;
import com.myideaway.easyapp.core.lib.L;
import com.myideaway.easyapp.core.lib.exception.RemoteServiceException;
import com.myideaway.easyapp.core.lib.service.BizService;
import com.myideaway.easyapp.core.lib.service.Service;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";
    private Beacon beacon;
    private DBHelper dbHelper;
    private ArrayList<Beacon> mBeaconInfoList;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
        dbHelper = DBHelper.instance(context);

        if (!DBHelper.isDatabaseExist()) {
            dbHelper.copyDatabaseFile(context);
        }

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            String key = bundle.getString(JPushInterface.EXTRA_EXTRA);
            HashMap hashMap = JSONUtil.toHashMap(key);
            if (hashMap.get("msg").equals("update")) {
                int version = dbHelper.getReadableDatabase().getVersion();
                Update(context, version);
            }

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {

    }


    /**
     * 运行BizService
     *
     * @param bizService
     * @param onCompleteHandler
     * @param onSuccessHandler
     * @param onFaultHandler
     */
    protected void runBizService(BizService bizService, Service.OnCompleteHandler onCompleteHandler, Service.OnSuccessHandler onSuccessHandler, final Service.OnFaultHandler onFaultHandler) {

        bizService.asyncExecute(onCompleteHandler, onSuccessHandler, new Service.OnFaultHandler() {
            @Override
            public void onFault(Service service, Exception e) {
                handleException(e);

                if (onFaultHandler != null) {
                    onFaultHandler.onFault(service, e);
                }
            }
        });
    }

    /**
     * 运行BizService
     *
     * @param bizService
     * @param onCompleteHandler
     * @param onSuccessHandler
     */
    protected void runBizService(BizService bizService, Service.OnCompleteHandler onCompleteHandler, Service.OnSuccessHandler onSuccessHandler) {
        runBizService(bizService, onCompleteHandler, onSuccessHandler, null);
    }

    /**
     * 运行BizService
     *
     * @param bizService
     * @param onCompleteHandler
     */
    protected void runBizService(BizService bizService, Service.OnCompleteHandler onCompleteHandler) {
        runBizService(bizService, onCompleteHandler, null, null);
    }

    /**
     * 运行BizService
     *
     * @param bizService
     */
    protected void runBizService(BizService bizService) {
        runBizService(bizService, null, null, null);
    }

    /**
     * 统一对异常进行处理提示
     *
     * @param exception
     */
    protected void handleException(Exception exception) {
        L.e(exception.getMessage(), exception);
        if (exception instanceof RemoteServiceException) {
            L.e("error", exception);
        } else if (exception instanceof AppException) {
            L.e("error", exception);
        } else {
            L.e("error", exception);

        }
    }

    private void Update(Context context, int version) {
        DBBSUpdate dbbsUpdate = new DBBSUpdate(context);
        dbbsUpdate.setVersion(version);
        runBizService(dbbsUpdate, new Service.OnCompleteHandler() {
            @Override
            public void onComplete(Service service) {

            }
        }, new Service.OnSuccessHandler() {
            @Override
            public void onSuccess(Service service, Object o) {
                DBBSUpdate.ServiceResult serviceResult = (DBBSUpdate.ServiceResult) o;
                HashMap result = serviceResult.getHashMap();
                ArrayList<LinkedTreeMap> delete = (ArrayList) result.get("data_delete");
                ArrayList<LinkedTreeMap> update = (ArrayList) result.get("data_update");

                int serviceVersion = Integer.parseInt((String) result.get("version"));

                delete(delete);
                update(update);
                dbHelper.getReadableDatabase().setVersion(serviceVersion);
            }
        });
    }


    private void delete(ArrayList<LinkedTreeMap> delete) {
        if (delete.size() != 0) {
            String[] beacons = new String[delete.size()];
            for (int i = 0; i < delete.size(); i++) {
                beacons[i] = (String) delete.get(i).get("sn");
            }

            dbHelper.deleteData(beacons);
        }

    }


    private void update(ArrayList<LinkedTreeMap> insert) {
        if (insert.size() != 0) {
            for (int i = 0; i < insert.size(); i++) {
                beacon = new Beacon();
                beacon.setBeacon_sn((String) insert.get(i).get("sn"));
                beacon.setBeacon_x((String) insert.get(i).get("x"));
                beacon.setBeacon_y((String) insert.get(i).get("y"));
                beacon.setBeacon_z(0 + "");
                beacon.setType(0 + "");
                dbHelper.insertData(beacon);

            }
        }
        mBeaconInfoList = dbHelper.select();

    }

}
