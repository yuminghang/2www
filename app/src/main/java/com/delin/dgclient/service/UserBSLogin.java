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
 * Created by Administrator on 2016/1/8 0008.
 */
public class UserBSLogin extends BizService {
    private String name;
    private String password;
    private HashMap token;
    public UserBSLogin(Context context) {
        super(context);
    }

    @Override
    protected BizServiceResult onExecute() throws Exception {
        HashMap params = new HashMap();
        if (token==null){
            params.put("name",name);
            params.put("password",password);
        }else {
            params = token;
        }

        JSONObject jsonObject = (JSONObject) remoteJSON(RemoteService.REQUEST_JSON, Config.URL_SERVER_USER_LOGIN,params);
        HashMap hashMap = JSONUtil.toHashMap(jsonObject.toString());
        ServiceResult serviceResult = new ServiceResult();
        serviceResult.setHashMap(hashMap);
        return serviceResult;
    }

    public void setToken(HashMap token) {
        this.token = token;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
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
