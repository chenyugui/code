package com.taichuan.code.wifi.callback;

/**
 * Created by gui on 2018/12/7.
 */

public interface LinkWifiListener {
    int ERR_CODE_PSW_ERR = 1;

    /*** 连接成功 */
    void onSuccess(String ssid);

    void onFail(int errCode);

    void needPassword();
}
