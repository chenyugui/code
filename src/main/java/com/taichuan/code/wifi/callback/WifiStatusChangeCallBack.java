package com.taichuan.code.wifi.callback;

/**
 * Created by gui on 2018/12/7.
 */
public interface WifiStatusChangeCallBack {
    /*** wifi已关闭 */
    void onWifiClosed();

    /*** wifi已打开 */
    void onWifiOpened();

    void onWifiOpening();

    void onWifiClosing();
}