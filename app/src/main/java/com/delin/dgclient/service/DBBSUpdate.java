package com.delin.dgclient.service;

import android.content.Context;

import com.delin.dgclient.api.Config;
import com.delin.dgclient.util.JSONUtil;
import com.myideaway.easyapp.core.lib.service.BizService;
import com.myideaway.easyapp.core.lib.service.BizServiceResult;
import com.myideaway.easyapp.core.lib.service.RemoteService;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/2/1 0001.
 */
public class DBBSUpdate extends BizService {

    private int version;

    public DBBSUpdate(Context context) {
        super(context);
    }

    @Override
    protected BizServiceResult onExecute() throws Exception {
        HashMap params = new HashMap();
        params.put("update_id",version+"");
        JSONObject jsonObject = (JSONObject) remoteJSON(RemoteService.REQUEST_JSON, Config.URL_SERVER_UP_DB_DATA,params);
        ServiceResult serviceResult = new ServiceResult();
        serviceResult.setHashMap(JSONUtil.toHashMap(jsonObject.toString()));
        return serviceResult;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public class ServiceResult extends BizServiceResult{
        HashMap hashMap = new HashMap();

        public HashMap getHashMap() {
            return hashMap;
        }

        public void setHashMap(HashMap hashMap) {
            this.hashMap = hashMap;
        }
    }
}
