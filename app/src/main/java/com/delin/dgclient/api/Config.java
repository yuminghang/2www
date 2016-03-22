package com.delin.dgclient.api;

import android.content.Context;
import android.content.res.Resources;

import com.delin.dgclient.R;


/**
 * Created by Administrator on 2015/7/16.
 */
public class Config {
    public static String PATH_CACHE;

    public static String URL_SERVER;
    public static String URL_SERVER_IMAGE;
    public static String URL_SERVAE_COUPON_IMAGE;
    public static String URL_SERVER_HEAD_PIC;
    public static String URL_SERVER_USER_REGISTER;
    public static String URL_SERVER_USER_LOGIN;
    public static String URL_SERVER_USER_LOGOUT;
    public static String URL_SERVER_LOCATION_PUSH;
    public static String URL_SERVER_PUSH_DETAIL;
    public static String URL_SERVER_SAVE_COUPON;
    public static String URL_SERVER_COUPON_INFORMATION;
    public static String URL_SERVER_EDIT_USER_INFORMATION;
    public static String URL_SERVER_GET_LAYER;
    public static String URL_SERVER_GET_TYPE;
    public static String URL_SERVER_GET_STORE;
    public static String URL_SERVER_GET_STORE_DETAIL;
    public static String URL_SERVER_SAVE_BUN;
    public static String URL_SERVER_GET_BUN;
    public static String URL_SERVER_GET_CODE;
    public static String URL_SERVER_FAST_LOGIN;
    public static String URL_SERVER_UP_DB_DATA;




    public static void init(Context context) {
        try {
            PATH_CACHE = context.getExternalCacheDir().getPath();
        } catch (Exception e) {
            PATH_CACHE = context.getCacheDir().getPath();
        }
        URL_SERVER = "http://121.42.146.103/mall/index.php";
        URL_SERVER_IMAGE = "http://121.42.146.103/mall/Logo/";
        URL_SERVAE_COUPON_IMAGE = "http://121.42.146.103/mall/Public/pics/";
        URL_SERVER_HEAD_PIC = "http://121.42.146.103/mall/Public/avatar/";
        URL_SERVER_USER_REGISTER = URL_SERVER + "/App/Public/register";
        URL_SERVER_USER_LOGIN = URL_SERVER+ "/App/Public/login";
        URL_SERVER_USER_LOGOUT = URL_SERVER +"/App/Public/logout";
        URL_SERVER_LOCATION_PUSH = URL_SERVER + "/App/Record/locationPush";
        URL_SERVER_PUSH_DETAIL = URL_SERVER + "/App/Record/pushdetail";
        URL_SERVER_SAVE_COUPON = URL_SERVER + "/App/Record/savecoupon";
        URL_SERVER_COUPON_INFORMATION = URL_SERVER + "/App/Member/couponinfo";
        URL_SERVER_EDIT_USER_INFORMATION = URL_SERVER+ "/App/Public/infedit";
        URL_SERVER_GET_LAYER = URL_SERVER+"/App/Store/getLayer";
        URL_SERVER_GET_TYPE = URL_SERVER+"/App/Store/getTypeOfStore";
        URL_SERVER_GET_STORE = URL_SERVER+"/App/Store/getStoreList";
        URL_SERVER_GET_STORE_DETAIL = URL_SERVER+"/App/Store/getStoreDetail";
        URL_SERVER_SAVE_BUN = URL_SERVER +"/App/Record/saveBun";
        URL_SERVER_GET_BUN = URL_SERVER+ "/App/Record/queryBuns";
        URL_SERVER_GET_CODE = URL_SERVER+ "/App/Public/getcode";
        URL_SERVER_FAST_LOGIN = URL_SERVER+ "/App/Public/flashLogin";
        URL_SERVER_UP_DB_DATA = "http://ms.dataguiding.com/sn/pushupdate";
    }
}