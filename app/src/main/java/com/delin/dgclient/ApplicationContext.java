package com.delin.dgclient;





import android.os.Build;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cdm on 15/5/29.
 */
public class ApplicationContext {
    private static ApplicationContext instance = new ApplicationContext();
    private String token;
    private String cookie;
    private String serialNumber ="";
    private String postedSerialNumber;
    private ArrayList<String> beacons;
    private double positioningAccuracy=2.75d;
    private boolean canGetPush = true;
    String appId = "35" + //we make this look like a valid IMEI
            Build.BOARD.length()%10 +
            Build.BRAND.length()%10 +
            Build.CPU_ABI.length()%10 +
            Build.DEVICE.length()%10 +
            Build.DISPLAY.length()%10 +
            Build.HOST.length()%10 +
            Build.ID.length()%10 +
            Build.MANUFACTURER.length()%10 +
            Build.MODEL.length()%10 +
            Build.PRODUCT.length()%10 +
            Build.TAGS.length()%10 +
            Build.TYPE.length()%10 +
            Build.USER.length()%10 ;

    private ApplicationContext(){

    }
    public static ApplicationContext getInstance(){
        return instance;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public ArrayList<String> getBeacons() {
        return beacons;
    }

    public void setBeacons(ArrayList<String> beacons) {
        this.beacons = beacons;
    }

    public String getAppId() {
        return appId;
    }

    public String getPostedSerialNumber() {
        return postedSerialNumber;
    }

    public void setPostedSerialNumber(String postedSerialNumber) {
        this.postedSerialNumber = postedSerialNumber;
    }

    public double getPositioningAccuracy() {
        return positioningAccuracy;
    }

    public void setPositioningAccuracy(double positioningAccuracy) {
        this.positioningAccuracy = positioningAccuracy;
    }

    public boolean isCanGetPush() {
        return canGetPush;
    }

    public void setCanGetPush(boolean canGetPush) {
        this.canGetPush = canGetPush;
    }
}
