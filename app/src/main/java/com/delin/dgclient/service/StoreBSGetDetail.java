package com.delin.dgclient.service;

import android.content.Context;

import com.delin.dgclient.api.Config;
import com.delin.dgclient.util.JSONUtil;
import com.google.gson.internal.LinkedTreeMap;
import com.myideaway.easyapp.core.lib.service.BizService;
import com.myideaway.easyapp.core.lib.service.BizServiceResult;
import com.myideaway.easyapp.core.lib.service.RemoteService;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Administrator on 2015/11/6 0006.
 */
public class StoreBSGetDetail extends BizService {

    private String storeId;
    public StoreBSGetDetail(Context context) {
        super(context);
    }

    @Override
    protected BizServiceResult onExecute() throws Exception {
        HashMap params = new HashMap();
        params.put("store_id",storeId);
        JSONObject jsonObject = (JSONObject) remoteJSON(RemoteService.REQUEST_METHOD_GET, Config.URL_SERVER_GET_STORE_DETAIL,params);
        HashMap hashMap = JSONUtil.toHashMap(jsonObject.toString());
        String status = (String) hashMap.get("status");
        ServiceResult serviceResult = new ServiceResult();
        if (status.equals("success")){
            serviceResult.setLinkedTreeMap((LinkedTreeMap) hashMap.get("msg"));
        }else {
            serviceResult.setErrorMsg((String) hashMap.get("msg"));
        }

        return serviceResult;
    }

    public String getId() {
        return storeId;
    }

    public void setId(String storeId) {
        this.storeId = storeId;
    }

    public class ServiceResult extends BizServiceResult{
        private LinkedTreeMap linkedTreeMap;
        private String errorMsg;

        public LinkedTreeMap getLinkedTreeMap() {
            return linkedTreeMap;
        }

        public void setLinkedTreeMap(LinkedTreeMap linkedTreeMap) {
            this.linkedTreeMap = linkedTreeMap;
        }

        public String getErrorMsg() {
            return errorMsg;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }
    }

}
