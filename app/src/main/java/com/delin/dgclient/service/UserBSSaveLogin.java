package com.delin.dgclient.service;

import android.content.Context;

import com.delin.dgclient.api.Config;
import com.myideaway.easyapp.core.lib.L;
import com.myideaway.easyapp.core.lib.service.BizService;
import com.myideaway.easyapp.core.lib.service.BizServiceResult;



import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 * Created by mashuai on 15/5/29.
 */
public class UserBSSaveLogin extends BizService {
    public static String CATCH_PATH = Config.PATH_CACHE + "token.dat";

    private String token;


    public UserBSSaveLogin(Context context) {
        super(context);
    }

    @Override
    protected BizServiceResult onExecute() throws Exception {
        File catchFile = new File(CATCH_PATH);
        L.d(CATCH_PATH);
        catchFile.getParentFile().mkdirs();
        HashMap<String,String> hashMap = new HashMap();
        hashMap.put("token",token);
        FileOutputStream outputStream = new FileOutputStream(catchFile);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(hashMap);
        objectOutputStream.flush();
        objectOutputStream.close();
        L.d("保存成功");
        ServiceResult serviceResult = new ServiceResult();
        serviceResult.setData(true);
        return serviceResult;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public class ServiceResult extends BizServiceResult{

    }

}
