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
 * Created by Administrator on 2015/12/1 0001.
 */
public class PositionAccuracyBSSave  extends BizService{

    public static String CATCH_PATH = Config.PATH_CACHE + "userSetting.dat";
    private Double positionAccuracy;
    private boolean canGetPush;
    public PositionAccuracyBSSave(Context context) {
        super(context);
    }

    @Override
    protected BizServiceResult onExecute() throws Exception {
        File catchFile = new File(CATCH_PATH);
        L.d(CATCH_PATH);
        catchFile.getParentFile().mkdirs();
        HashMap<String,Object> hashMap = new HashMap();
        hashMap.put("positionAccuracy",positionAccuracy);
        hashMap.put("canGetPush",canGetPush);
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


    public void setPositionAccuracy(Double positionAccuracy) {
        this.positionAccuracy = positionAccuracy;
    }


    public void setCanGetPush(boolean canGetPush) {
        this.canGetPush = canGetPush;
    }

    public class ServiceResult extends BizServiceResult{

    }
}
