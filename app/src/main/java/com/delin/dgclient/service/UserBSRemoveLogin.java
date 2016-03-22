package com.delin.dgclient.service;

import android.content.Context;

import com.delin.dgclient.api.Config;
import com.delin.dgclient.util.L;
import com.myideaway.easyapp.core.lib.service.BizService;
import com.myideaway.easyapp.core.lib.service.BizServiceResult;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cdm on 15/5/29.
 */
public class UserBSRemoveLogin extends BizService {
    public static String CATCH_PATH = Config.PATH_CACHE + "token.dat";


    public UserBSRemoveLogin(Context context) {
        super(context);
    }

    @Override
    protected BizServiceResult onExecute() throws Exception {
        File catchFile = new File(CATCH_PATH);

        catchFile.delete();
        L.d("删除成功");
        ServiceResult serviceResult = new ServiceResult();
        serviceResult.setData(true);
        return serviceResult;
    }

    public class ServiceResult extends BizServiceResult {


    }

}
