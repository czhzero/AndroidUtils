package com.gezbox.library.utils.utils;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.List;


/**
 * Created by chenzhaohua on 16/1/20.
 */
public class NetworkUtils {


    /**
     * 检测是否连接网络
     *
     * @param context
     * @return
     */
    public static boolean isConnectedOrConnecting(Context context) {
        try {
            ConnectivityManager ccm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = ccm.getActiveNetworkInfo();
            return ni != null && ni.isConnectedOrConnecting();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 检查网络是否已连接
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
        try {
            ConnectivityManager ccm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = ccm.getActiveNetworkInfo();
            return ni.isConnected();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 检测是否连接wifi
     *
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        try {
            ConnectivityManager ccm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = ccm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            return ni != null && ni.isConnected();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 检测是否打开gps
     *
     * @param context
     * @return
     */
    public static boolean isGPSOn(Context context) {
        try {
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 检测是否有gps
     *
     * @param context
     * @return
     */
    public static boolean hasGPS(Context context) {
        try {
            final LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (lm == null) {
                return false;
            }

            final List<String> providers = lm.getAllProviders();
            if (providers == null) {
                return false;
            }

            return providers.contains(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 获得网络类型
     *
     * @param context
     * @return
     */
    public static int getNetWorkStatus(Context context) {

        if (context == null)
            return NetworkType.NETWORK_UNAVAILABLE.ordinal();

        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity == null) {
            return NetworkType.NETWORK_UNAVAILABLE.ordinal();
        }

        NetworkInfo info = connectivity.getActiveNetworkInfo();

        if(info == null) {
            return NetworkType.NETWORK_UNAVAILABLE.ordinal();
        }

        if (info.getTypeName().toLowerCase().equals("wifi")) {
            if (info.getState() == NetworkInfo.State.CONNECTED) {
                return NetworkType.NETWORK_TYPE_WIFI.ordinal();
            } else {
                return NetworkType.NETWORK_UNAVAILABLE.ordinal();
            }
        } else if (info.getTypeName().toLowerCase().equals("mobile")) {
            if (info.getState() == NetworkInfo.State.CONNECTED) {
                if (info.getExtraInfo() != null && info.getExtraInfo().indexOf("wap") != -1)
                    return NetworkType.NETWORK_TYPE_WAP.ordinal();
                else
                    return NetworkType.NETWORK_TYPE_NET.ordinal();
            }
        }

        return NetworkType.NETWORK_UNAVAILABLE.ordinal();
    }


    public enum NetworkType {

        NETWORK_UNAVAILABLE(-1),
        NETWORK_TYPE_WAP(0),
        NETWORK_TYPE_NET(1),
        NETWORK_TYPE_WIFI(2);

        private int nCode;

        // 构造函数，枚举类型只能为私有
        NetworkType(int nCode) {
            this.nCode = nCode;
        }

        @Override
        public String toString() {
            return String.valueOf(this.nCode);
        }
    }


    /**
     * 获取mac地址
     * @param context
     * @return
     */
    public static String getMacAddress(Context context) {
        try {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            return info.getMacAddress();
        } catch (Exception e) {
            return "";
        }
    }







}
