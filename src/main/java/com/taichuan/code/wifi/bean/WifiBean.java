package com.taichuan.code.wifi.bean;

import android.net.wifi.ScanResult;

/**
 * Created by gui on 2018/12/6.
 */

public class WifiBean {
    private int level;
    private int status;// 0：未连接未保存， 1：已保存，  2：已连接
    private int pswType;// 密码类型
    private ScanResult scanResult;

    public static final int STATUS_NON = 0;
    public static final int STATUS_SAVED = 1;
    public static final int STATUS_LINKED = 2;

    public ScanResult getScanResult() {
        return scanResult;
    }

    public void setScanResult(ScanResult scanResult) {
        this.scanResult = scanResult;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPswType() {
        return pswType;
    }

    public void setPswType(int pswType) {
        this.pswType = pswType;
    }

    @Override
    public String toString() {
        return "WifiBean{" +
                "level=" + level +
                ", status=" + status +
                ", pswType='" + pswType + '\'' +
                ", scanResult=" + scanResult +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof WifiBean) {
            WifiBean wifiBean = (WifiBean) obj;
            if (wifiBean.getScanResult().SSID.equals(getScanResult().SSID)) {
                return true;
            } else {
                return false;
            }
        }
        return super.equals(obj);
    }
}
