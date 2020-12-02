package com.taichuan.code.wifi.util;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.taichuan.code.app.AppGlobal;
import com.taichuan.code.receiver.NetBroadcastReceiver;
import com.taichuan.code.thread.GlobalThreadManager;
import com.taichuan.code.utils.NetWorkUtil;
import com.taichuan.code.utils.ThreadUtil;
import com.taichuan.code.wifi.bean.WifiBean;
import com.taichuan.code.wifi.callback.LinkWifiListener;
import com.taichuan.code.wifi.callback.ScanWifiListener;
import com.taichuan.code.wifi.callback.WifiConnectCallBack;
import com.taichuan.code.wifi.callback.WifiStatusChangeCallBack;
import com.taichuan.code.wifi.enums.PswType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by gui on 2018/12/6.
 * Wifi模块管理类
 */
public class WifiAdmin {
    private static final String TAG = "WifiAdmin";
    private Context mContext;
    private WifiManager mWifiManager;
    private NetBroadcastReceiver mNetBroadcastReceiver;

    private WifiConnectCallBack mWifiConnectCallBack;
    private WifiStatusChangeCallBack mWifiStatusCallBack;

    private LinkWifiListener mLinkWifiListener;
    private ScanWifiListener mScanWifiListener;
    private String targetSSID;
    private boolean isConnecting;
    private static final String UNKONW_SSID = "<unknown ssid>";

    public WifiAdmin(Context context) {
        mWifiManager = (WifiManager) AppGlobal.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mContext = context;
        registerReceiver();
    }

    private void registerReceiver() {
        mNetBroadcastReceiver = new NetBroadcastReceiver();
        mNetBroadcastReceiver.setWifiConnectCallBack(new NetBroadcastReceiver.WifiConnectCallBack() {
            @Override
            public void onWifiDisconnected() {
                if (mWifiConnectCallBack != null) {
                    mWifiConnectCallBack.onWifiDisconnected();
                }
            }

            @Override
            public void onWifiConnected() {
                if (mWifiConnectCallBack != null) {
                    mWifiConnectCallBack.onWifiConnected();
                }
                if (isConnecting && getConnectingWifiInfo() != null && checkReConnectWifi(targetSSID)) {
                    isConnecting = false;
                    if (mLinkWifiListener != null) {
                        String currentSSID = getSSID();
                        mLinkWifiListener.onSuccess(currentSSID);
                    }
                    targetSSID = null;
                }
            }

            @Override
            public void onWifiConnectStatusChange() {
                if (mWifiConnectCallBack != null) {
                    mWifiConnectCallBack.onWifiConnectStatusChange();
                }
            }

            @Override
            public void onWifiPasswordErr() {
                if (isConnecting) {
                    if (mLinkWifiListener != null) {
                        mLinkWifiListener.onFail(LinkWifiListener.ERR_CODE_PSW_ERR);
                    }
                    targetSSID = null;
                }
            }
        });
        mNetBroadcastReceiver.setWifiStatusChangeCallBack(new NetBroadcastReceiver.WifiStatusChangeCallBack() {
            @Override
            public void onWifiClosed() {
                if (mWifiStatusCallBack != null) {
                    mWifiStatusCallBack.onWifiClosed();
                }
            }

            @Override
            public void onWifiOpened() {
                if (mWifiStatusCallBack != null) {
                    mWifiStatusCallBack.onWifiOpened();
                }
            }

            @Override
            public void onWifiOpenning() {
                if (mWifiStatusCallBack != null) {
                    mWifiStatusCallBack.onWifiOpening();
                }
            }

            @Override
            public void onWifiClosing() {
                if (mWifiStatusCallBack != null) {
                    mWifiStatusCallBack.onWifiClosing();
                }
            }
        });
        mNetBroadcastReceiver.setWifiScanCallBack(new NetBroadcastReceiver.WifiScanCallBack() {
            @Override
            public void onScanFinish() {
                if (mScanWifiListener != null) {
                    GlobalThreadManager.getInstance().addRun(new Runnable() {
                        @Override
                        public void run() {
                            final List<WifiBean> wifiBeanList = getScanWifiListAutoSort();
                            AppGlobal.getHandler().post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        mScanWifiListener.onScanFinish(wifiBeanList);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });
        mNetBroadcastReceiver.registerThis(mContext);
    }

    /*** 注意每new一个WifiAdmin， 都必须在对应生命周期销毁的地方调用此destroy方法 */
    public void destroy() {
        if (mNetBroadcastReceiver != null) {
            mNetBroadcastReceiver.unRegisterThis(mContext);
        }
        mWifiConnectCallBack = null;
        mLinkWifiListener = null;
        mScanWifiListener = null;
        mWifiStatusCallBack = null;
    }

    public void scan() {
        mWifiManager.startScan();
    }

    /*** 设置wifi热点连接状态变化监听 */
    public void setWifiConnectCallBack(WifiConnectCallBack wifiConnectCallBack) {
        mWifiConnectCallBack = wifiConnectCallBack;
    }

    /*** 设置wifi模块开关状态变化监听 */
    public void setWifiStatusChangeCallBack(WifiStatusChangeCallBack wifiStatusCallBack) {
        mWifiStatusCallBack = wifiStatusCallBack;
    }

    /*** 设置wifi热点扫描监听 */
    public void setWifiScanCallBack(ScanWifiListener scanWifiListener) {
        mScanWifiListener = scanWifiListener;
    }


    private boolean checkIsMainThread() {
        if (ThreadUtil.isMainThread()) {
            throw new RuntimeException("此操作要在子线程");
        }
        return false;
    }

    public boolean isWifiEnabled() {
//        if (!checkIsMainThread()) {
        return mWifiManager.isWifiEnabled();
//        }
//        return false;
    }


    @SuppressWarnings("UnusedReturnValue")
    public boolean openWifi() {
        boolean bRet = true;
        if (!mWifiManager.isWifiEnabled()) {
            bRet = mWifiManager.setWifiEnabled(true);
        }
        return bRet;
    }

    @SuppressWarnings("UnusedReturnValue")
    public boolean closeWifi() {
        boolean bRet = true;
        if (mWifiManager.isWifiEnabled()) {
            bRet = mWifiManager.setWifiEnabled(false);
        }
        return bRet;
    }

    /*** 将搜索到的wifi根据信号进行排序 */
    private void sortWifiByLevel(List<WifiBean> list) {
        Collections.sort(list, new Comparator<WifiBean>() {
            @Override
            public int compare(WifiBean wifiBean, WifiBean wifiBean2) {
                return wifiBean2.getLevel() - wifiBean.getLevel();
            }
        });
    }

    /*** 将搜索到的wifi根据状态进行排序 */
    private void sortWifiByStatus(List<WifiBean> list) {
        Collections.sort(list, new Comparator<WifiBean>() {
            @Override
            public int compare(WifiBean wifiBean, WifiBean wifiBean2) {
                return wifiBean2.getStatus() - wifiBean.getStatus();
            }
        });
    }

    public String getSSID() {
        WifiInfo wifiInfo = getConnectingWifiInfo();
        if (wifiInfo != null) {
            return subSSID(wifiInfo.getSSID());
        }
        return null;
    }

    @SuppressWarnings("WeakerAccess")
    public List<WifiBean> getScanWifiList() {
        if (checkIsMainThread()) {
            return null;
        }
        List<ScanResult> list = mWifiManager.getScanResults();
        List<WifiBean> wifiBeanList = new ArrayList<>();
        if (list == null) {
            list = new ArrayList<>();
        }
        WifiInfo wifiInfo = getConnectingWifiInfo();
        for (ScanResult scanResult : list) {
            WifiBean wifiBean = new WifiBean();
            wifiBean.setScanResult(scanResult);
            wifiBean.setLevel(scanResult.level);
            wifiBean.setPswType(getWifiEncryptTypeStr(scanResult.capabilities));
            // 判断热点是否是保存过的
            WifiConfiguration wifiConfiguration = NetWorkUtil.isExist(mContext, scanResult.SSID);
            if (wifiConfiguration != null) {
                wifiBean.setStatus(WifiBean.STATUS_SAVED);
            }
            // 判断是否是当前连接的wifi
            if (wifiInfo != null) {
                String SSID = subSSID(wifiInfo.getSSID());
                if (SSID.equals(scanResult.SSID)) {
                    wifiBean.setStatus(WifiBean.STATUS_LINKED);
                }
            }
            if (!wifiBeanList.contains(wifiBean)) {// 去除重复SSID的热点
                wifiBeanList.add(wifiBean);
            }
        }
        return wifiBeanList;
    }

    public WifiInfo getConnectingWifiInfo() {
        if (NetWorkUtil.isConnectWifi(mContext)) {
            return mWifiManager.getConnectionInfo();
        }
        return null;
    }

    @SuppressWarnings("WeakerAccess")
    public List<WifiBean> getScanWifiListAutoSort() {
        List<WifiBean> wifiBeanList = getScanWifiList();
        sortWifiByLevel(wifiBeanList);
        sortWifiByStatus(wifiBeanList);
        return wifiBeanList;
    }

    private String subSSID(String SSID) {
        if (SSID.startsWith("\"")) {
            SSID = SSID.substring(1, SSID.length());
        }
        if (SSID.endsWith("\"")) {
            SSID = SSID.substring(0, SSID.length() - 1);
        }
        return SSID;
    }

    private int getWifiEncryptTypeStr(String capabilities) {
        if (TextUtils.isEmpty(capabilities)) {
            return -1;
        }
        int encryptType;
        if (capabilities.contains("WPA") && capabilities.contains("WPA2")) {
            encryptType = PswType.PSW_TYPE_WPA_WPA2;
        } else if (capabilities.contains("WPA2")) {
            encryptType = PswType.PSW_TYPE_WPA2;
        } else if (capabilities.contains("WPA")) {
            encryptType = PswType.PSW_TYPE_WPA;
        } else if (capabilities.contains("WEP")) {
            encryptType = PswType.PSW_TYPE_WEP;
        } else {
            encryptType = PswType.PSW_TYPE_NON;
        }
        return encryptType;
    }

    private WifiConfiguration createWifiConfig(String ssid, String password, @PswType int encryptType) {
        if (checkIsMainThread()) {
            return null;
        }
        if (!isWifiEnabled()) {
            return null;
        }
        WifiConfiguration wc = new WifiConfiguration();
        wc.allowedAuthAlgorithms.clear();
        wc.allowedGroupCiphers.clear();
        wc.allowedKeyManagement.clear();
        wc.allowedPairwiseCiphers.clear();
        wc.allowedProtocols.clear();

        wc.SSID = "\"" + ssid + "\"";

        WifiConfiguration configuration = NetWorkUtil.isExist(mContext, ssid);
        if (configuration != null) {
            // 必须先移除原来该配置（因为之前的配置有可能密码输错了）
            mWifiManager.removeNetwork(configuration.networkId);
        }
        switch (encryptType) {
            case PswType.PSW_TYPE_NON:
                //不加密
                wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                break;
            case PswType.PSW_TYPE_WEP:
                //wep加密
                wc.hiddenSSID = true;
                wc.wepKeys[0] = "\"" + password + "\"";
                wc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
                wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                wc.wepTxKeyIndex = 0;
                break;
            case PswType.PSW_TYPE_WPA_WPA2:
                //wpa/wap2加密
            case PswType.PSW_TYPE_WPA2:
                //wpa2加密
            case PswType.PSW_TYPE_WPA:
                //wpa加密
                wc.preSharedKey = "\"" + password + "\"";
                wc.hiddenSSID = true;
                wc.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                wc.status = WifiConfiguration.Status.ENABLED;
                break;
            default:
                break;
        }
        return wc;
    }

    public boolean disConnectWifi(String SSID) {
        if (checkIsMainThread()) {
            return false;
        }
        WifiConfiguration wifiConfiguration = NetWorkUtil.isExist(mContext, SSID);
        if (wifiConfiguration == null) {
            return false;
        }
        if (targetSSID != null && targetSSID.equals(SSID)) {
            targetSSID = null;
            isConnecting = false;
        }
        return mWifiManager.disableNetwork(wifiConfiguration.networkId);
    }

    public boolean disConnectCurrentWifi() {
        WifiInfo wifiInfo = getConnectingWifiInfo();
        if (wifiInfo == null) {
            return true;
        }
        if (targetSSID != null && targetSSID.equals(subSSID(wifiInfo.getSSID()))) {
            targetSSID = null;
            isConnecting = false;
        }
        return mWifiManager.disconnect();
    }

    public boolean forgetWifi(String SSID) {
        if (!checkIsMainThread()) {
            WifiConfiguration wifiConfiguration = NetWorkUtil.isExist(mContext, SSID);
            //noinspection SimplifiableIfStatement
            if (wifiConfiguration == null) {
                return false;
            }
            //return mWifiManager.removeNetwork(wifiConfiguration.networkId);
            mWifiManager.removeNetwork(wifiConfiguration.networkId);
            return mWifiManager.saveConfiguration();
        }
        return false;
    }

    /**
     * @return true: 已经连上目标SSID
     */
    private boolean checkReConnectWifi(String targetLinkSSID) {
        String currentSSID = getSSID();
        Log.d(TAG, "checkReConnectWifi: currentSSID=" + currentSSID);
        Log.d(TAG, "checkReConnectWifi: targetLinkSSID=" + targetLinkSSID);
        if (TextUtils.isEmpty(targetLinkSSID)) {
            return true;
        }
        if (TextUtils.isEmpty(currentSSID)) {
            return false;
        }
        // 检查当前连接的wifi是否是我们想要的wifi
        if (!targetLinkSSID.equals(currentSSID)) {
            // 不是我们要连的wifi，说明系统自动重连了其他wifi
            disConnectCurrentWifi();
            linkSavedWifi(targetLinkSSID, mLinkWifiListener);
        } else if (targetLinkSSID.equals(currentSSID)) {
            return true;
        }
        return false;
    }

    public void linkSavedWifi(String ssid, LinkWifiListener linkWifiListener) {
        linkNoSavedWifi(ssid, "", linkWifiListener);
    }

    public void linkNoSavedWifi(final String ssid, final String password, final LinkWifiListener linkWifiListener) {
        GlobalThreadManager.getInstance().addRun(new Runnable() {
            @Override
            public void run() {
                WifiConfiguration config = NetWorkUtil.isExist(mContext, ssid);
                if (config != null && TextUtils.isEmpty(password)) {
                    // 已保存，可以直接连接
                    realLinkWifi(config.networkId, ssid, linkWifiListener);
                } else {
                    List<WifiBean> wifiBeanList = getScanWifiList();
                    for (WifiBean wifiBean : wifiBeanList) {
                        if (wifiBean.getScanResult().SSID.equals(ssid)) {
                            if (wifiBean.getPswType() == PswType.PSW_TYPE_NON) {
                                // 热点无加密
                                config = createWifiConfig(ssid, "", PswType.PSW_TYPE_NON);
                                int networkId = mWifiManager.addNetwork(config);
                                //Android6.0以下需要添加此方法
                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                                    mWifiManager.saveConfiguration();
                                }
                                realLinkWifi(networkId, ssid, linkWifiListener);
                            } else {
                                // 热点有加密
                                if (TextUtils.isEmpty(password)) {
                                    if (linkWifiListener != null) {
                                        linkWifiListener.needPassword();
                                    }
                                } else {
                                    config = createWifiConfig(ssid, password, wifiBean.getPswType());
                                    int networkId = mWifiManager.addNetwork(config);
                                    //Android6.0以下需要添加此方法
                                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                                        mWifiManager.saveConfiguration();
                                    }
                                    realLinkWifi(networkId, ssid, linkWifiListener);
                                }
                            }
                            return;
                        }
                    }
                }
            }
        });
    }

    public void linkCustomWifi(final String SSID, final String password, @PswType final int pswType, final LinkWifiListener linkWifiListener) {
        GlobalThreadManager.getInstance().addRun(new Runnable() {
            @Override
            public void run() {
                WifiConfiguration config = createWifiConfig(SSID, password, pswType);
                int networkId = mWifiManager.addNetwork(config);
                //Android6.0以下需要添加此方法
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    mWifiManager.saveConfiguration();
                }
                realLinkWifi(networkId, SSID, linkWifiListener);
            }
        });
    }

    private boolean realLinkWifi(int networkId, String ssid, LinkWifiListener linkWifiListener) {
        mLinkWifiListener = linkWifiListener;
        targetSSID = ssid;
        isConnecting = true;
        return mWifiManager.enableNetwork(networkId, true);
    }
}
