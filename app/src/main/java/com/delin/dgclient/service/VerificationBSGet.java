package com.delin.dgclient.service;

import android.content.Context;

import com.delin.dgclient.api.Config;
import com.delin.dgclient.util.JSONUtil;
import com.myideaway.easyapp.core.lib.service.BizService;
import com.myideaway.easyapp.core.lib.service.BizServiceResult;
import com.myideaway.easyapp.core.lib.service.RemoteService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Administrator on 2015/11/19 0019.
 */
public class VerificationBSGet extends BizService {

    private String mobile;
    private String isRegist;

    public VerificationBSGet(Context context) {
        super(context);
    }

    @Override
    protected BizServiceResult onExecute() throws Exception {
        HashMap params = new HashMap();
        params.put("mobile",mobile);
        params.put("regist",isRegist);
        JSONObject jsonObject = (JSONObject) remoteJSON(RemoteService.REQUEST_METHOD_POST, Config.URL_SERVER_GET_CODE,params);
        HashMap hashMap = JSONUtil.toHashMap(jsonObject.toString());
        ServiceResult  serviceResult = new ServiceResult();
        serviceResult.setResult(hashMap);
        return serviceResult;
    }


    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setIsRegist(String isRegist) {
        this.isRegist = isRegist;
    }

    public class ServiceResult extends BizServiceResult{
        private HashMap result;

        public HashMap getResult() {
            return result;
        }

        public void setResult(HashMap result) {
            this.result = result;
        }
    }
}
