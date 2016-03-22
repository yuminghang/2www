package com.delin.dgclient.service;

import android.content.Context;

import com.delin.dgclient.api.Config;
import com.delin.dgclient.util.JSONUtil;
import com.myideaway.easyapp.core.lib.service.BizService;
import com.myideaway.easyapp.core.lib.service.BizServiceResult;
import com.myideaway.easyapp.core.lib.service.RemoteService;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2015/11/5 0005.
 */
public class StoreBSGetType extends BizService {


    public StoreBSGetType(Context context) {
        super(context);
    }

    @Override
    protected BizServiceResult onExecute() throws Exception {
        JSONObject jsonObject = (JSONObject) remoteJSON(RemoteService.REQUEST_METHOD_GET, Config.URL_SERVER_GET_TYPE);
        HashMap result = JSONUtil.toHashMap(jsonObject.toString());
        ServiceResult serviceResult = new ServiceResult();
        serviceResult.setStatus((String) result.get("status"));
        serviceResult.setArrayList((ArrayList) result.get("msg"));
        return serviceResult;
    }

    public class ServiceResult extends BizServiceResult{
        private String status;
        private ArrayList arrayList;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public ArrayList getArrayList() {
            return arrayList;
        }

        public void setArrayList(ArrayList arrayList) {
            this.arrayList = arrayList;
        }
    }
}
