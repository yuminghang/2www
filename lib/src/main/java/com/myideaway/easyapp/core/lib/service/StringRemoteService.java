package com.myideaway.easyapp.core.lib.service;

import com.myideaway.easyapp.core.lib.Config;
import com.myideaway.easyapp.core.lib.L;
import com.myideaway.easyapp.core.lib.exception.RemoteServiceException;

import com.myideaway.easyapp.core.lib.util.JSONUtil;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class StringRemoteService extends RemoteService {


    @Override
    public Object onExecute() throws RemoteServiceException {

        try {

            String result = null;

            if (formFiles != null) {

                L.d("Send post multipart url " + requestUrl + ", param " + params);

                MultipartBuilder multipartBuilder = new MultipartBuilder();
                multipartBuilder.type(MultipartBuilder.FORM);

                for (FormFile formFile : formFiles) {
                    String fileName = FilenameUtils.getName(formFile.getFile().getName());
                    multipartBuilder.addFormDataPart(formFile.getName(), fileName, RequestBody.create(MultipartBuilder.FORM, formFile.getFile()));

                    L.d("File " + fileName);
                }

                if (params!=null){
                    for (String key : params.keySet()) {
                        Object value = params.get(key);

                        if (value instanceof List) {
                            List list = (List) value;

                            for (Object listItem : list) {
                                multipartBuilder.addFormDataPart(key, String.valueOf(listItem));
                            }
                        } else {
                            multipartBuilder.addFormDataPart(key, String.valueOf(value));
                        }
                    }
                }


                RequestBody requestBody = multipartBuilder.build();
                Request request;
                if (bizCookie.getCookie() == null) {
                    request = new Request.Builder()
                            .url(requestUrl)
                            .post(requestBody)
                            .build();
                } else {
                    request = new Request.Builder()
                            .url(requestUrl)
                            .addHeader("Cookie", bizCookie.getCookie())
                            .post(requestBody)
                            .build();
                }


                OkHttpClient okHttpClient = new OkHttpClient();
                okHttpClient.setConnectTimeout(Config.REMOTE_SERVICE_TIME_OUT, TimeUnit.MILLISECONDS);

                Response response = okHttpClient.newCall(request).execute();
                if (response.header("Set-Cookie") != null) {
                    bizCookie.setCookie(response.header("Set-Cookie").toString());
                }
                result = response.body().string();

            } else {
                if (requestMethod == REQUEST_METHOD_GET) {
                    L.d("Send get url " + requestUrl + ", param " + params);

                    StringBuffer appendParams = new StringBuffer();

                    if (params != null) {
                        for (String key : params.keySet()) {
                            Object value = params.get(key);

                            if (value instanceof List) {
                                List list = (List) value;

                                for (Object listItem : list) {
                                    appendParams.append(key).append("=").append(listItem).append("&");
                                }
                            } else {
                                appendParams.append(key).append("=").append(value).append("&");
                            }
                        }
                    }


                    String paramedUrl = requestUrl;
                    if (StringUtils.isNotEmpty(appendParams.toString())) {
                        paramedUrl = paramedUrl + "?" + appendParams.toString();
                    }
                    Request request;
                    if (bizCookie.getCookie() != null) {
                        request = new Request.Builder()
                                .url(requestUrl)
                                .addHeader("Cookie", bizCookie.getCookie())
                                .build();
                    } else {
                        request = new Request.Builder()
                                .url(requestUrl)
                                .build();
                    }

                    OkHttpClient okHttpClient = new OkHttpClient();
                    okHttpClient.setConnectTimeout(Config.REMOTE_SERVICE_TIME_OUT, TimeUnit.MILLISECONDS);
                    Response response = okHttpClient.newCall(request).execute();
                    if (response.header("Set-Cookie") != null) {
                        bizCookie.setCookie(response.header("Set-Cookie").toString());
                    }
                    result = response.body().string();
                } else if (requestMethod == REQUEST_METHOD_POST) {

                    L.d("Send post url " + requestUrl + ", param " + params);

                    FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();

                    if (params != null) {
                        for (String key : params.keySet()) {
                            Object value = params.get(key);

                            if (value instanceof List) {
                                List list = (List) value;

                                for (Object listItem : list) {
                                    formEncodingBuilder.add(key, String.valueOf(listItem));
                                }
                            } else {
                                formEncodingBuilder.add(key, String.valueOf(value));
                            }

                            formEncodingBuilder.add(key, String.valueOf(value));
                        }
                    }

                    RequestBody requestBody = formEncodingBuilder.build();

                    Request request = new Request.Builder()
                            .url(requestUrl)
                            .post(requestBody)
                            .build();

                    OkHttpClient okHttpClient = new OkHttpClient();
                    okHttpClient.setConnectTimeout(Config.REMOTE_SERVICE_TIME_OUT, TimeUnit.MILLISECONDS);

                    Response response = okHttpClient.newCall(request).execute();
                    if (response.header("Set-Cookie") != null) {
                        bizCookie.setCookie(response.header("Set-Cookie").toString());
                    }
                    result = response.body().string();
                } else {
                    String json = JSONUtil.toJSON(params);
                    MediaType jsonReq = MediaType.parse("application/json;charset=utf-8");
                    RequestBody body = RequestBody.create(jsonReq, json);
                    Request request;

                    if (bizCookie.getCookie() != null) {
                        request = new Request.Builder()
                                .url(requestUrl)
                                .post(body)
                                .addHeader("Cookie",bizCookie.getCookie())
                                .build();
                    } else {
                        request = new Request.Builder()
                                .url(requestUrl)
                                .post(body)
                                .build();
                    }

                    OkHttpClient okHttpClient = new OkHttpClient();
                    okHttpClient.setConnectTimeout(Config.REMOTE_SERVICE_TIME_OUT, TimeUnit.MILLISECONDS);
                    Response response = okHttpClient.newCall(request).execute();
                    if (response.header("Set-Cookie") != null) {
                        bizCookie.setCookie(response.header("Set-Cookie").toString());
                    }

                    result = response.body().string();
                }


            }

            L.d("Result " + result);

            return result;
        } catch (Exception e) {
            throw new RemoteServiceException(e);
        }
    }
}
