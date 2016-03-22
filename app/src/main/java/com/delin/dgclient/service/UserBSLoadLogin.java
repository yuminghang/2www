package com.delin.dgclient.service;

import android.content.Context;

import com.delin.dgclient.api.Config;
import com.myideaway.easyapp.core.lib.L;
import com.myideaway.easyapp.core.lib.service.BizService;
import com.myideaway.easyapp.core.lib.service.BizServiceResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;

/**
 * Created by cdm on 15/5/29.
 */
public class UserBSLoadLogin extends BizService {
    public static String CATCH_PATH = Config.PATH_CACHE + "token.dat";

    public UserBSLoadLogin(Context context) {
        super(context);
    }

    @Override
    protected BizServiceResult onExecute() throws Exception {
        File catchFile = new File(CATCH_PATH);
        ServiceResult serviceResult = new ServiceResult();
        if (catchFile.exists()) {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(catchFile));
            HashMap<String,String> user = (HashMap<String, String>) objectInputStream.readObject();
            objectInputStream.close();
            L.d("读取成功");
            serviceResult.setData(user);
        }
        return serviceResult;
    }

    public class ServiceResult extends BizServiceResult {

    }


}
