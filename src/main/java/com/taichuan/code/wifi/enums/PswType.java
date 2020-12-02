package com.taichuan.code.wifi.enums;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by gui on 2018/12/7.
 */
@Retention(RetentionPolicy.SOURCE)
public @interface PswType {
    public static final int PSW_TYPE_NON = 1;
    public static final int PSW_TYPE_WPA_WPA2 = 2;// WPA/WPA2 PSK
    public static final int PSW_TYPE_WPA = 3;// WPA PSK
    public static final int PSW_TYPE_WPA2 = 4;// WPA2 PSK
    public static final int PSW_TYPE_WEP = 5;// WEP
}
