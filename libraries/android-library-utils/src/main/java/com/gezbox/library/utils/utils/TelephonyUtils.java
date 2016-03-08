package com.gezbox.library.utils.utils;

import android.content.Context;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import java.util.List;

/**
 * Created by chenzhaohua on 16/1/20.
 * android API中的TelephonyManager对象，可以取得SIM卡中的信息
 */
public class TelephonyUtils {

    static final String NETWORK_TYPE_1xRTT = "1xRTT";
    static final String NETWORK_TYPE_CDMA = "CDMA";
    static final String NETWORK_TYPE_EDGE = "EDGE";
    static final String NETWORK_TYPE_EHRPD = "eHRPD";
    static final String NETWORK_TYPE_EVDO_0 = "EVDO revision 0";
    static final String NETWORK_TYPE_EVDO_A = "EVDO revision A";
    static final String NETWORK_TYPE_EVDO_B = "EVDO revision B";
    static final String NETWORK_TYPE_GPRS = "GPRS";
    static final String NETWORK_TYPE_HSDPA = "HSDPA";
    static final String NETWORK_TYPE_HSPA = "HSPA";
    static final String NETWORK_TYPE_HSPAP = "HSPA+";
    static final String NETWORK_TYPE_HSUPA = "HSUPA";
    static final String NETWORK_TYPE_IDEN = "iDen";
    static final String NETWORK_TYPE_LTE = "LTE";
    static final String NETWORK_TYPE_UMTS = "UMTS";
    static final String NETWORK_TYPE_UNKNOWN = "unknown";


    private static TelephonyManager getTelephoneManager(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm;
    }


    /**
     * 唯一的设备ID：
     * 如果是GSM网络，返回IMEI；如果是CDMA网络，返回MEID
     * 需要权限：android.permission.READ_PHONE_STATE
     *
     * @return null if device ID is not available.
     */
    public static String getDeviceId(Context context) {
        return getTelephoneManager(context).getDeviceId();
    }



    /**
     * 电话状态：
     * CALL_STATE_IDLE 无任何状态时
     * CALL_STATE_OFFHOOK 接起电话时
     * CALL_STATE_RINGING 电话进来时
     *
     * @return
     */
    public static int getCallState(Context context) {
        return getTelephoneManager(context).getCallState();
    }

    /**
     * 返回当前移动终端的位置
     *
     * @return
     */
    public static CellLocation getCellLocation(Context context) {
        CellLocation location = getTelephoneManager(context).getCellLocation();

        // 请求位置更新，如果更新将产生广播，接收对象为注册LISTEN_CELL_LOCATION的对象，需要的permission名称为ACCESS_COARSE_LOCATION。
        // location.requestLocationUpdate();

        return location;
    }



    /**
     * 返回移动终端的软件版本：
     * 例如：GSM手机的IMEI/SV码。
     *
     * @return null if the software version is not available.
     */
    public static String getDeviceSoftwareVersion(Context context) {
        return getTelephoneManager(context).getDeviceSoftwareVersion();
    }

    /**
     * 手机号：
     * 对于GSM网络来说即MSISDN
     *
     * @return null if it is unavailable.
     */
    public static String getLine1Number(Context context) {
        return getTelephoneManager(context).getLine1Number();
    }

    /**
     * 返回当前移动终端附近移动终端的信息:
     * 类型：List<NeighboringCellInfo>
     * 需要权限：android.Manifest.permission#ACCESS_COARSE_UPDATES
     *
     * @return
     */
    public static List<NeighboringCellInfo> getNeighboringCellInfo(Context context) {
        // List<NeighboringCellInfo> infos = getTelephoneManager(context).getNeighboringCellInfo();
        // for (NeighboringCellInfo info : infos) {
        // // 获取邻居小区号
        // int cid = info.getCid();
        //
        // // 获取邻居小区LAC，LAC:
        // // 位置区域码。为了确定移动台的位置，每个GSM/PLMN的覆盖区都被划分成许多位置区，LAC则用于标识不同的位置区。
        // info.getLac();
        // info.getNetworkType();
        // info.getPsc();
        //
        // // 获取邻居小区信号强度
        // info.getRssi();
        // }

        return getTelephoneManager(context).getNeighboringCellInfo();
    }

    /**
     * 获取ISO标准的国家码，即国际长途区号。<br/>
     * 注意：仅当用户已在网络注册后有效。<br/>
     * 在CDMA网络中结果也许不可靠。<br/>
     *
     * @return
     */
    public static String getNetworkCountryIso(Context context) {
        return getTelephoneManager(context).getNetworkCountryIso();
    }

    /**
     * MCC+MNC(mobile country code + mobile network code)<br/>
     * 注意：仅当用户已在网络注册时有效。<br/>
     * 在CDMA网络中结果也许不可靠。<br/>
     *
     * @return
     */
    public static String getNetworkOperator(Context context) {
        return getTelephoneManager(context).getNetworkOperator();
    }

    /**
     * 按照字母次序的current registered operator(当前已注册的用户)的名字
     * 注意：仅当用户已在网络注册时有效。
     * 在CDMA网络中结果也许不可靠。
     *
     * @return
     */
    public static String getNetworkOperatorName(Context context) {
        return getTelephoneManager(context).getNetworkOperatorName();
    }

    /**
     * 当前使用的网络类型：
     * NETWORK_TYPE_UNKNOWN 网络类型未知 0
     * NETWORK_TYPE_GPRS GPRS网络 1
     * NETWORK_TYPE_EDGE EDGE网络 2
     * NETWORK_TYPE_UMTS UMTS网络 3
     * NETWORK_TYPE_HSDPA HSDPA网络 8
     * NETWORK_TYPE_HSUPA HSUPA网络 9
     * NETWORK_TYPE_HSPA HSPA网络 10
     * NETWORK_TYPE_CDMA CDMA网络,IS95A 或 IS95B. 4
     * NETWORK_TYPE_EVDO_0 EVDO网络, revision 0. 5
     * NETWORK_TYPE_EVDO_A EVDO网络, revision A. 6
     * NETWORK_TYPE_1xRTT 1xRTT网络
     * 在中国，联通的3G为UMTS或HSDPA，移动和联通的2G为GPRS或EGDE，电信的2G为CDMA，电信的3G为EVDO
     *
     * @return
     */
    public static String getNetworkType(Context context) {

        switch (getTelephoneManager(context).getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return NETWORK_TYPE_1xRTT;
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return NETWORK_TYPE_CDMA;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return NETWORK_TYPE_EDGE;
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return NETWORK_TYPE_EHRPD;
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return NETWORK_TYPE_EVDO_0;
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return NETWORK_TYPE_EVDO_A;
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return NETWORK_TYPE_EVDO_B;
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return NETWORK_TYPE_GPRS;
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return NETWORK_TYPE_HSDPA;
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return NETWORK_TYPE_HSPA;
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return NETWORK_TYPE_HSPAP;
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return NETWORK_TYPE_HSUPA;
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return NETWORK_TYPE_IDEN;
            case TelephonyManager.NETWORK_TYPE_LTE:
                return NETWORK_TYPE_LTE;
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return NETWORK_TYPE_UMTS;
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return NETWORK_TYPE_UNKNOWN;
        }
        return "";
    }



    /**
     * 返回移动终端的类型：
     * PHONE_TYPE_CDMA 手机制式为CDMA，电信
     * PHONE_TYPE_GSM 手机制式为GSM，移动和联通
     * PHONE_TYPE_NONE 手机制式未知
     *
     * @return
     */
    public static int getPhoneType(Context context) {
        return getTelephoneManager(context).getPhoneType();
    }

    /**
     * 获取ISO国家码，相当于提供SIM卡的国家码。
     *
     * @return Returns the ISO country code equivalent for the SIM provider's
     *         country code.
     */
    public static String getSimCountryIso(Context context) {
        return getTelephoneManager(context).getSimCountryIso();
    }

    /**
     * 获取SIM卡提供的移动国家码和移动网络码.5或6位的十进制数字.
     * SIM卡的状态必须是 SIM_STATE_READY(使用getSimState()判断).
     *
     * @return Returns the MCC+MNC (mobile country code + mobile network code)
     *         of the provider of the SIM. 5 or 6 decimal digits.
     */
    public static String getSimOperator(Context context) {
        return getTelephoneManager(context).getSimOperator();
    }

    /**
     * 服务商名称：
     * 例如：中国移动、联通
     * SIM卡的状态必须是 SIM_STATE_READY(使用getSimState()判断).
     *
     * @return
     */
    public static String getSimOperatorName(Context context) {
        return getTelephoneManager(context).getSimOperatorName();
    }

    /**
     * SIM卡的序列号：
     * 需要权限：READ_PHONE_STATE
     *
     * @return
     */
    public static String getSimSerialNumber(Context context) {
        return getTelephoneManager(context).getSimSerialNumber();
    }

    /**
     * SIM的状态信息：
     * SIM_STATE_UNKNOWN 未知状态 0
     * SIM_STATE_ABSENT 没插卡 1
     * SIM_STATE_PIN_REQUIRED 锁定状态，需要用户的PIN码解锁 2
     * SIM_STATE_PUK_REQUIRED 锁定状态，需要用户的PUK码解锁 3
     * SIM_STATE_NETWORK_LOCKED 锁定状态，需要网络的PIN码解锁 4
     * SIM_STATE_READY 就绪状态 5
     *
     * @return
     */
    public static int getSimState(Context context) {
        return getTelephoneManager(context).getSimState();
    }

    /**
     * 唯一的用户ID：
     * 例如：IMSI(国际移动用户识别码) for a GSM phone.
     * 需要权限：READ_PHONE_STATE
     *
     * @return
     */
    public static String getSubscriberId(Context context) {
        return getTelephoneManager(context).getSubscriberId();
    }

    /**
     * 取得和语音邮件相关的标签，即为识别符
     * 需要权限：READ_PHONE_STATE
     *
     * @return
     */
    public static String getVoiceMailAlphaTag(Context context) {
        return getTelephoneManager(context).getVoiceMailAlphaTag();
    }

    /**
     * 获取语音邮件号码：
     * 需要权限：READ_PHONE_STATE
     *
     * @return
     */
    public static String getVoiceMailNumber(Context context) {
        return getTelephoneManager(context).getVoiceMailNumber();
    }

    /**
     * ICC卡是否存在
     *
     * @return
     */
    public static boolean hasIccCard(Context context) {
        return getTelephoneManager(context).hasIccCard();
    }

    /**
     * 是否漫游:(在GSM用途下)
     *
     * @return
     */
    public static boolean isNetworkRoaming(Context context) {
        return getTelephoneManager(context).isNetworkRoaming();
    }

    /**
     * 获取数据活动状态
     * DATA_ACTIVITY_IN 数据连接状态：活动，正在接受数据
     * DATA_ACTIVITY_OUT 数据连接状态：活动，正在发送数据
     * DATA_ACTIVITY_INOUT 数据连接状态：活动，正在接受和发送数据
     * DATA_ACTIVITY_NONE 数据连接状态：活动，但无数据发送和接受
     *
     * @return
     */
    public static int getDataActivity(Context context) {
        return getTelephoneManager(context).getDataActivity();
    }

    /**
     * 获取数据连接状态
     * DATA_CONNECTED 数据连接状态：已连接
     * DATA_CONNECTING 数据连接状态：正在连接
     * DATA_DISCONNECTED 数据连接状态：断开
     * DATA_SUSPENDED 数据连接状态：暂停
     *
     * @return
     */
    public static int getDataState(Context context) {
        return getTelephoneManager(context).getDataState();
    }

}
