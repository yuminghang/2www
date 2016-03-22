package com.delin.dgclient.service;

import android.content.Context;

import com.delin.dgclient.api.Config;
import com.delin.dgclient.util.JSONUtil;
import com.myideaway.easyapp.core.lib.service.BizService;
import com.myideaway.easyapp.core.lib.service.BizServiceResult;
import com.myideaway.easyapp.core.lib.service.RemoteService;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/1/7 0007.
 */
public class PhotoBSUpload extends BizService {
    private String photoPath;

    public PhotoBSUpload(Context context) {
        super(context);
    }

    @Override
    protected BizServiceResult onExecute() throws Exception {
        RemoteService.FormFile formFile = new RemoteService.FormFile();
        List<RemoteService.FormFile> list = new ArrayList<RemoteService.FormFile>();
        File file = new File(photoPath);
        if (file.exists()){
            formFile.setName("avatar");
            formFile.setFile(file);
            list.add(formFile);
        }
        JSONObject jsonObject = (JSONObject) remoteJSON(RemoteService.REQUEST_METHOD_POST, Config.URL_SERVER_EDIT_USER_INFORMATION,null,list);
        HashMap hashMap = JSONUtil.toHashMap(jsonObject.toString());
        ServiceResult serviceResult = new ServiceResult();
        serviceResult.setHashMap(hashMap);
        return serviceResult;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
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
