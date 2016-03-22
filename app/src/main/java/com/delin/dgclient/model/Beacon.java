package com.delin.dgclient.model;

/**
 * Created by Administrator on 2015/12/16 0016.
 */
public class Beacon {
    private String beacon_sn;
    private String beacon_x;
    private String beacon_y;
    private String beacon_z;
    private String type;

    public String getBeacon_sn() {
        return beacon_sn;
    }

    public void setBeacon_sn(String beacon_sn) {
        this.beacon_sn = beacon_sn;
    }

    public String getBeacon_x() {
        return beacon_x;
    }

    public void setBeacon_x(String beacon_x) {
        this.beacon_x = beacon_x;
    }

    public String getBeacon_y() {
        return beacon_y;
    }

    public void setBeacon_y(String beacon_y) {
        this.beacon_y = beacon_y;
    }

    public String getBeacon_z() {
        return beacon_z;
    }

    public void setBeacon_z(String beacon_z) {
        this.beacon_z = beacon_z;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String[] toStrings() {
        return new String[]{beacon_sn, beacon_x , beacon_y
                , beacon_z, type };
    }
}
