package com.delin.dgclient.service;

import android.content.Context;

import com.delin.dgclient.api.Config;
import com.delin.dgclient.util.JSONUtil;
import com.google.gson.internal.LinkedTreeMap;
import com.myideaway.easyapp.core.lib.service.BizService;
import com.myideaway.easyapp.core.lib.service.BizServiceResult;
import com.myideaway.easyapp.core.lib.service.RemoteService;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2015/11/5 0005.
 */
public class StoreBSGetAll extends BizService {
    private String keywords= "0";
    private String layer="0";
    private String type = "0";
    private String sort = "0";
    private String page="1";
    public StoreBSGetAll(Context context) {
        super(context);
    }

    @Override
    protected BizServiceResult onExecute() throws Exception {
        HashMap params =new HashMap();
        params.put("keywords",keywords);
        params.put("layer",layer);
        params.put("type",type);
        params.put("sort",sort);
        params.put("p",page);

        JSONObject jsonObject = (JSONObject) remoteJSON(RemoteService.REQUEST_METHOD_GET, Config.URL_SERVER_GET_STORE,params);
        HashMap hashMap = JSONUtil.toHashMap(jsonObject.toString());
        String status = (String) hashMap.get("status");
        ServiceResult serviceResult = new ServiceResult();
        serviceResult.setStatus(status);
        if (status.equals("error")){
            String msg = (String) hashMap.get("msg");
            serviceResult.setMsg(msg);
        }else {
            serviceResult.setArrayList((ArrayList) hashMap.get("msg"));
        }



        return serviceResult;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public class ServiceResult extends BizServiceResult{
        private String status;
        private ArrayList<LinkedTreeMap> arrayList;

        private String msg;

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

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
