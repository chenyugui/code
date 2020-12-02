package com.taichuan.code.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xyh on 2019/2/27.
 */

public class IpUtil {
    public static boolean checkIpFormat(String ip){
        if (ip == null || "".equals(ip) || "0.0.0.0".equals(ip)){
            return false;
        }
        Pattern pa = Pattern.compile("^(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\." + "(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\."
                + "(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\." + "(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])$");
        Matcher ma = pa.matcher(ip);
        return ma.matches();
    }

    /** 验证ipAddr1 与ipAddr2 是否在同一网段 **/
    public static boolean checkTargetIp(String ipAddr1, String ipAddr2, String netmask) {
        boolean isSame = true;

        System.out.println("ipAddr1 " + ipAddr1 + " ipAddr2 " + ipAddr2 + " netmask" + netmask);
        try {
            // calc sub-net IP
            InetAddress ipAddress = InetAddress.getByName(ipAddr1);
            InetAddress ipAddress2 = InetAddress.getByName(ipAddr2);
            InetAddress maskAddress = InetAddress.getByName(netmask);

            byte[] ipRaw = ipAddress.getAddress();
            byte[] ipRaw2 = ipAddress2.getAddress();
            byte[] maskRaw = maskAddress.getAddress();

            int unsignedByteFilter = 0x000000ff;
            for (int i = 0; i < ipRaw.length; i++) {
                if ((ipRaw[i] & maskRaw[i] & unsignedByteFilter) != (ipRaw2[i] & maskRaw[i] & unsignedByteFilter))
                    isSame = false;
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
            isSame = false;
        }
        return isSame;
    }
}
