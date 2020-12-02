package com.taichuan.code.wifi.util;

import com.taichuan.code.wifi.enums.PswType;

/**
 * @author gui
 * @date 2018/12/12
 */
public class PswTypeCoverUtil {
    public static String cover(@PswType int pswType) {
        String string = null;
        switch (pswType) {
            case PswType.PSW_TYPE_NON:
                string = "无";
                break;
            case PswType.PSW_TYPE_WPA_WPA2:
                string = "WPA/WPA2 PSK";
                break;
            case PswType.PSW_TYPE_WPA:
                string = "WPA PSK";
                break;
            case PswType.PSW_TYPE_WPA2:
                string = "WPA2 PSK";
                break;
            case PswType.PSW_TYPE_WEP:
                string = "WEP";
                break;
            default:
                break;
        }
        return string;
    }
}
