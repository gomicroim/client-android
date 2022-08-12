package com.gomicroim.lib.util;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.WIFI_SERVICE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import androidx.core.app.ActivityCompat;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.UUID;

/**
 * Android DeviceId获取。原理：https://www.jianshu.com/p/7967e9ce711c
 */
public class AndroidDeviceId {
    private String uuid;
    private static final String KEY_UDID = "KEY_UDID";
    //在Android6.0的版本以后用原来的getMacAddress()方法获取手机的MAC地址都为：02:00:00:00:00:00这个固定的值
    private static final String EMPTY_MAC_ADDRESS = "02:00:00:00:00:00";
    private Activity activity;

    public AndroidDeviceId(Activity activity) {
        this.activity = activity;
    }

    /**
     * 获取DeviceId
     *
     * @return the unique device id
     */
    @SuppressLint({"MissingPermission", "HardwareIds"})
    public String getUniqueDeviceId() {
        if (!StringUtils.isEmpty(uuid)) {
            return uuid;
        }

        String localDeviceId = getLocalDeviceId();
        if (!StringUtils.isEmpty(localDeviceId)) {
            uuid = localDeviceId;
            return uuid;
        }

        String deviced = null;
        try {

            // 先获取mac
            deviced = getMacAddress();

            // 为空再获取AndroidId
            if (!StringUtils.isEmpty(deviced)) {
                deviced = getAndroidID();
            }

            // 为空再获取imei
            if (!StringUtils.isEmpty(deviced)) {
                deviced = getDeviceId();
            }
        } catch (Exception ignore) {
        }

        // 都为空，创建1个新的UUID
        if (!StringUtils.isEmpty(deviced)) {
            deviced = getNewUUID();
        }

        // 保存deviceId
        if (!StringUtils.isEmpty(deviced)) {
            saveDeviceId(deviced);
        }
        uuid = deviced;

        return uuid;
    }

    // 获取Mac地址
    private String getMacAddress() {
        //在Android6.0的版本以后用原来的getMacAddress()方法获取手机的MAC地址都为：02:00:00:00:00:00这个固定的值
        String macAddress = getMacAddressByNetworkInterface();
        if (isAddressNotInExcepts(macAddress, (String[]) null)) {
            return macAddress;
        }
        macAddress = getMacAddressByInetAddress();
        if (isAddressNotInExcepts(macAddress, (String[]) null)) {
            return macAddress;
        }
        macAddress = getMacAddressByWifiInfo();
        if (isAddressNotInExcepts(macAddress, (String[]) null)) {
            return macAddress;
        }

        return null;
    }

    private boolean isAddressNotInExcepts(final String address, final String... excepts) {
        if (StringUtils.isEmpty(address)) {
            return false;
        }
        if (excepts == null || excepts.length == 0) {
            return !EMPTY_MAC_ADDRESS.equals(address);
        }
        for (String filter : excepts) {
            if (address.equals(filter)) {
                return false;
            }
        }
        return true;
    }

    private String getMacAddressByNetworkInterface() {
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                if (ni == null || !ni.getName().equalsIgnoreCase("wlan0")) continue;
                byte[] macBytes = ni.getHardwareAddress();
                if (macBytes != null && macBytes.length > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (byte b : macBytes) {
                        sb.append(String.format("%02x:", b));
                    }
                    return sb.substring(0, sb.length() - 1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EMPTY_MAC_ADDRESS;
    }

    private String getMacAddressByInetAddress() {
        try {
            InetAddress inetAddress = getInetAddress();
            if (inetAddress != null) {
                NetworkInterface ni = NetworkInterface.getByInetAddress(inetAddress);
                if (ni != null) {
                    byte[] macBytes = ni.getHardwareAddress();
                    if (macBytes != null && macBytes.length > 0) {
                        StringBuilder sb = new StringBuilder();
                        for (byte b : macBytes) {
                            sb.append(String.format("%02x:", b));
                        }
                        return sb.substring(0, sb.length() - 1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EMPTY_MAC_ADDRESS;
    }

    private InetAddress getInetAddress() {
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                // To prevent phone of xiaomi return "10.0.2.15"
                if (!ni.isUp()) continue;
                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress inetAddress = addresses.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String hostAddress = inetAddress.getHostAddress();
                        if (hostAddress.indexOf(':') < 0) return inetAddress;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getMacAddressByWifiInfo() {
        try {
            final WifiManager wifi = (WifiManager) activity.getApplicationContext().getSystemService(WIFI_SERVICE);
            if (wifi != null) {
                final WifiInfo info = wifi.getConnectionInfo();
                if (info != null)
                    if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return EMPTY_MAC_ADDRESS;
                    }
                return info.getMacAddress();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return EMPTY_MAC_ADDRESS;
    }

    // 获取AndroidId
    private String getAndroidID() {
        String id = Settings.Secure.getString(
                activity.getContentResolver(),
                Settings.Secure.ANDROID_ID
        );
        if ("9774d56d682e549c".equals(id)) {
            return null;
        } else {
            return id;
        }
    }

    // 获取DeviceId
    private String getDeviceId() {
        TelephonyManager tm = (TelephonyManager) activity.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = tm.getDeviceId();
        if (deviceId == null) {
            return null;
        } else {
            return deviceId;
        }
    }

    // 获得1个新的UUID
    private String getNewUUID() {
        return UUID.randomUUID().toString();
    }

    private String getLocalDeviceId() {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(KEY_UDID, MODE_PRIVATE);
        if (sharedPreferences != null) {
            return sharedPreferences.getString(KEY_UDID, null);
        }
        return null;
    }

    private void saveDeviceId(String deviceId) {
        SharedPreferences mShare = activity.getSharedPreferences(KEY_UDID, MODE_PRIVATE);
        if (mShare != null) {
            mShare.edit().putString(KEY_UDID, deviceId).apply();
        }
    }
}
