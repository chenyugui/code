package com.taichuan.code.wifi.callback;


import com.taichuan.code.wifi.bean.WifiBean;

import java.util.List;

/**
 * Created by gui on 2018/12/7.
 */
public interface ScanWifiListener {
    /*** 扫描完毕， wifiBeanList已经排好序 */
    void onScanFinish(List<WifiBean> wifiBeanList);
}
