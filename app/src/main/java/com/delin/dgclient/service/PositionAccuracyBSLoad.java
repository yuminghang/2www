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
 * Created by Administrator on 2015/12/1 0001.
 */
public class PositionAccuracyBSLoad extends BizService {
    public static String CATCH_PATH = Config.PATH_CACHE + "userSetting.dat";
    public PositionAccuracyBSLoad(Context context) {
        super(context);
    }

    @Override
    protected BizServiceResult onExecute() throws Exception {
        File catchFile = new File(CATCH_PATH);
        ServiceResult serviceResult = new ServiceResult();
        if (catchFile.exists()) {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(catchFile));
            HashMap<String,Object> user = (HashMap<String,Object>) objectInputStream.readObject();
            objectInputStream.close();
            L.d("读取成功");
            serviceResult.setData(user);
        }
        return serviceResult;

    }

    public class ServiceResult extends BizServiceResult {

    }
}
