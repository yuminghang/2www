package com.delin.dgclient.service;

import android.content.Context;

import com.delin.dgclient.ApplicationContext;

import com.delin.dgclient.api.Config;

import com.myideaway.easyapp.core.lib.service.BizService;
import com.myideaway.easyapp.core.lib.service.BizServiceResult;
import com.myideaway.easyapp.core.lib.service.RemoteService;
import com.myideaway.easyapp.core.lib.util.JSONUtil;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/3/11 0011.
 */
public class CouponsBSReceive extends BizService {

    private String cId;
    private String appId = ApplicationContext.getInstance().getAppId();

    public CouponsBSReceive(Context context) {
        super(context);
    }

    @Override
    protected BizServiceResult onExecute() throws Exception {
        HashMap params = new HashMap();
        params.put("app_id", appId);
        params.put("c_id", cId);
        JSONObject jsonObject = (JSONObject) remoteJSON(RemoteService.REQUEST_JSON, Config.URL_SERVER_SAVE_COUPON,params);
        ServiceResult serviceResult =new ServiceResult();
        serviceResult.setHashMap(JSONUtil.toHashMap(jsonObject.toString()));
        return serviceResult;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public class ServiceResult extends BizServiceResult{
        private HashMap hashMap = new HashMap();

        public HashMap getHashMap() {
            return hashMap;
        }

        public void setHashMap(HashMap hashMap) {
            this.hashMap = hashMap;
        }
    }
}
