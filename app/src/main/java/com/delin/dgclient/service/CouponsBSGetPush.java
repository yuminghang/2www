package com.delin.dgclient.service;

import android.content.Context;

import com.delin.dgclient.ApplicationContext;
import com.delin.dgclient.api.Config;
import com.google.gson.JsonObject;

import com.myideaway.easyapp.core.lib.service.BizService;
import com.myideaway.easyapp.core.lib.service.BizServiceResult;
import com.myideaway.easyapp.core.lib.service.RemoteService;
import com.myideaway.easyapp.core.lib.util.JSONUtil;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/3/11 0011.
 */
public class CouponsBSGetPush extends BizService {

    private String iBeaconSN;
    private String appID = ApplicationContext.getInstance().getAppId();

    public CouponsBSGetPush(Context context) {
        super(context);
    }

    @Override
    protected BizServiceResult onExecute() throws Exception {
        HashMap params = new HashMap();
        params.put("ibeacon_sn",iBeaconSN);
        params.put("app_id",appID);
        JSONObject jsonObject = (JSONObject) remoteJSON(RemoteService.REQUEST_JSON,Config.URL_SERVER_LOCATION_PUSH,params);
        ServiceResult serviceResult = new ServiceResult();
        serviceResult.setHashMap(JSONUtil.toHashMap(jsonObject.toString()));
        return serviceResult;
    }

    public void setIBeaconSN(String iBeaconSN) {
        this.iBeaconSN = iBeaconSN;
    }

    public class ServiceResult extends  BizServiceResult{
        private HashMap hashMap = new HashMap();

        public HashMap getHashMap() {
            return hashMap;
        }

        public void setHashMap(HashMap hashMap) {
            this.hashMap = hashMap;
        }
    }
}
