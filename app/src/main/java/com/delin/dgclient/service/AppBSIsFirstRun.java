package com.delin.dgclient.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.myideaway.easyapp.core.lib.service.BizService;
import com.myideaway.easyapp.core.lib.service.BizServiceResult;

/**
 * Created by cdm on 15/6/10.
 */
public class AppBSIsFirstRun extends BizService {

    public AppBSIsFirstRun(Context context) {
        super(context);
    }

    @Override
    protected BizServiceResult onExecute() throws Exception {
        SharedPreferences sharedPreferences = context.getSharedPreferences("app.dat", Context.MODE_PRIVATE);
        boolean hasRan = sharedPreferences.getBoolean("hasRan", false);

        ServiceResult serviceResult = new ServiceResult();
        serviceResult.setData(!hasRan);

        return serviceResult;
    }

    public class ServiceResult extends BizServiceResult {

    }
}
