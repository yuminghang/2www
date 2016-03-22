package com.delin.dgclient.util;



import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.AsyncTask;
import android.util.Log;

import com.delin.dgclient.ApplicationContext;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import okio.BufferedSink;

/**
 * Created by Administrator on 2015/9/23 0023.
 */
public abstract class HttpHelper extends AsyncTask<Object, Object,Integer> {
    private OkHttpClient httpClient = new OkHttpClient();
    public static final int TASK_RESULT_SUCCESS = 1;
    public static final int TASK_RESULT_FAULT = 2;

    private HashMap params = new HashMap();
    private String result;
    private String url;
    private String cookie = null;
    public ProgressDialog progressDialog;
    private Bitmap bitmap;

    public HttpHelper(String url, String cookie) {
        this.cookie = cookie;
        this.url = url;
    }

    public HttpHelper(String url, HashMap params, String cookie) {
        this.url = url;
        this.params = params;
        this.cookie = cookie;

    }


    public String getResult() {
        return result;
    }


    @Override
    protected Integer doInBackground(Object[] objects) {
        RequestBody body = null;
        if (params != null) {
            String json = JSONUtil.toJSON(params);
            MediaType jsonReq = MediaType.parse("application/json;charset=utf-8");
            body = RequestBody.create(jsonReq, json);
        }

        Request request;
        if (body == null) {
            if (cookie != null) {
                cookie = ApplicationContext.getInstance().getCookie();
                request = new Request.Builder()
                        .url(url)
                        .addHeader("Cookie", cookie)
                        .build();
            } else {
                request = new Request.Builder()
                        .url(url)
                        .build();
            }

        } else {
            if (cookie != null) {
                request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .addHeader("Cookie", cookie)
                        .build();
            } else {
                request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
            }
        }


        try {
            Response response = httpClient.newCall(request).execute();
            if (response.header("Set-Cookie") != null) {
                String sessionId = response.header("Set-Cookie").toString();
                ApplicationContext.getInstance().setCookie(sessionId);
            }

            result = response.body().string();
            Log.d("result", result);
        } catch (IOException e) {
            L.d(e.toString());
            return TASK_RESULT_FAULT;
        }
        return TASK_RESULT_SUCCESS;
    }

    public void showProgressDialog(Context context, String msg) {
        if (context != null) {
            progressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage(msg);
            progressDialog.show();
        }

    }

    public byte[] toByte(Bitmap bitmap){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }



}

