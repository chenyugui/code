package com.taichuan.code.wifi.enums;

/**
 * Created by gui on 2018/12/12.
 */

public class PswTypeBean {
    public int pswType;
    public String pswTypeName;

    public PswTypeBean(int pswType, String pswTypeName) {
        this.pswType = pswType;
        this.pswTypeName = pswTypeName;
    }

    @Override
    public String toString() {
        return pswTypeName;
    }
}
