package com.myideaway.easyapp.core.lib;

/**
 * Created by Administrator on 2016/1/6 0006.
 */
public class BizCookie {
    private static BizCookie bizCookie;

    private String cookie;
    private BizCookie() {

    }

    public static BizCookie initBizCookie(){
        if (bizCookie==null){
            bizCookie = new BizCookie();
        }
        return bizCookie;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }
}
