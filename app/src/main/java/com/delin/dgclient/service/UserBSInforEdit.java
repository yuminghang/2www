package com.delin.dgclient.service;

import android.content.Context;

import com.delin.dgclient.api.Config;
import com.delin.dgclient.util.JSONUtil;
import com.myideaway.easyapp.core.lib.BizCookie;
import com.myideaway.easyapp.core.lib.service.BizService;
import com.myideaway.easyapp.core.lib.service.BizServiceResult;
import com.myideaway.easyapp.core.lib.service.RemoteService;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Administrator on 2016/1/8 0008.
 */
public class UserBSInforEdit extends BizService {

    private String result;

    public UserBSInforEdit(Context context) {
        super(context);
    }

    @Override
    protected BizServiceResult onExecute() throws Exception {
        HashMap params = new HashMap();
        params.put("nickname",result);
        JSONObject jsonObject = (JSONObject) remoteJSON(RemoteService.REQUEST_METHOD_POST, Config.URL_SERVER_EDIT_USER_INFORMATION,params);
        ServiceResult serviceResult = new ServiceResult();
        serviceResult.setHashMap(JSONUtil.toHashMap(jsonObject.toString()));
        return serviceResult;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public class ServiceResult extends BizServiceResult {
       HashMap hashMap = new HashMap();

        public HashMap getHashMap() {
            return hashMap;
        }

        public void setHashMap(HashMap hashMap) {
            this.hashMap = hashMap;
        }
    }
}
