package com.taichuan.code.wifi.callback;

/**
 * Created by gui on 2018/12/7.
 */

public interface WifiConnectCallBack {
    /*** wifi未连接 */
    void onWifiDisconnected();

    /*** wifi已连接 */
    void onWifiConnected();

    /*** wifi连接状态变化 */
    void onWifiConnectStatusChange();
}
