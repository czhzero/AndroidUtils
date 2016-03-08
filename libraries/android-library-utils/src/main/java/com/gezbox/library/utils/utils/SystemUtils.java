package com.gezbox.library.utils.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenzhaohua on 16/1/19.
 */
public class SystemUtils {

    private static Toast toast;

    /**
     * 显示Toast
     *
     * @param context
     * @param message
     */
    public static void showToast(Context context, String message) {
        SystemUtils.showToast(context, message, Gravity.BOTTOM);
    }

    /**
     * 显示Toast
     *
     * @param context
     * @param message
     * @param gravity
     */
    public static void showToast(Context context, String message, int gravity) {
        if (context == null || TextUtils.isEmpty(message)) {
            return;
        }

        if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }

        toast.setGravity(gravity, 0, 0);
        toast.show();
    }



    /**
     * 检查是否存在相应的Intent
     *
     * @param context
     * @param intent
     * @return
     */
    public static boolean isIntentAvailable(Context context, Intent intent) {
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> list = pm.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);

        return list.size() > 0;
    }

    /**
     * 跳转到拨号页面
     * @param context
     * @param tel
     */
    public static void makeCall(Context context, String tel) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel));
        if (isIntentAvailable(context, intent)) {
            context.startActivity(intent);
        } else {
            SystemUtils.showToast(context, "请检查您的手机是否有拨号应用");
        }
    }




    /**
     * 检测设备是否root
     *
     * @return
     */
    public static boolean isDeviceRooted() {
        if (checkRootMethod1()) {
            return true;
        }
        if (checkRootMethod2()) {
            return true;
        }
        if (checkRootMethod3()) {
            return true;
        }
        return false;
    }

    private static boolean checkRootMethod1() {
        String buildTags = android.os.Build.TAGS;

        if (buildTags != null && buildTags.contains("test-keys")) {
            return true;
        }
        return false;
    }

    private static boolean checkRootMethod2() {
        try {
            File file = new File("/system/app/Superuser.apk");
            if (file.exists()) {
                return true;
            }
        } catch (Exception e) {
        }

        return false;
    }

    private static boolean checkRootMethod3() {
        if (new ExecShell().executeCommand(ExecShell.SHELL_CMD.check_su_binary) != null) {
            return true;
        } else {
            return false;
        }
    }

    private static class ExecShell {

        private static String LOG_TAG = ExecShell.class.getName();

        public enum SHELL_CMD {
            check_su_binary(new String[]{"/system/xbin/which", "su"}),;

            String[] command;

            SHELL_CMD(String[] command) {
                this.command = command;
            }
        }

        public ArrayList<String> executeCommand(SHELL_CMD shellCmd) {
            String line;
            ArrayList<String> fullResponse = new ArrayList<String>();
            Process localProcess;

            try {
                localProcess = Runtime.getRuntime().exec(shellCmd.command);
            } catch (Exception e) {
                return null;
            }

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(localProcess.getOutputStream()));
            BufferedReader in = new BufferedReader(new InputStreamReader(localProcess.getInputStream()));

            try {
                while ((line = in.readLine()) != null) {
                    Log.d(LOG_TAG, "--> Line received: " + line);
                    fullResponse.add(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.d(LOG_TAG, "--> Full response was: " + fullResponse);

            return fullResponse;
        }

    }


    /**
     * get version name
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * get version code
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 发送短信
     *
     * @param context
     * @param number
     * @param message
     */
    public static void sendSMS(Context context, String number, String message) {
        String SENT = "SENT_SMS_ACTION";
        String DELIVERED = "DELIVERED_SMS_ACTION";

        PendingIntent sentPI = PendingIntent.getActivity(context, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getActivity(context, 0, new Intent(DELIVERED), 0);

        context.registerReceiver(new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Log.i("sms", "Activity.RESULT_OK");
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Log.i("sms", "RESULT_ERROR_GENERIC_FAILURE");
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Log.i("sms", "RESULT_ERROR_NO_SERVICE");
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Log.i("sms", "RESULT_ERROR_NULL_PDU");
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Log.i("sms", "RESULT_ERROR_RADIO_OFF");
                        break;
                }
            }
        }, new IntentFilter(SENT));

        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Log.i("sms", "RESULT_OK");
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i("sms", "RESULT_CANCELED");
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager smsm = SmsManager.getDefault();
        smsm.sendTextMessage(number, null, message, sentPI, deliveredPI);
    }


    /**
     * 显示或隐藏软键盘
     *
     * @param context
     * @param view
     * @param shouldShow
     */
    public static void toggleSoftInput(Context context, View view, boolean shouldShow) {
        InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (shouldShow) {
            im.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        } else {
            im.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }



    /**
     * 注册本地广播接收器
     *
     * @param context
     * @param receiver
     * @param intentFilter
     */
    public static void registerLocalReceiver(Context context, BroadcastReceiver receiver, IntentFilter intentFilter) {
        if (context == null) {
            return;
        }

        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context);
        manager.registerReceiver(receiver, intentFilter);
    }

    /**
     * 取消注册本地广播接收器
     *
     * @param context
     * @param receiver
     */
    public static void unRegisterLocalReceiver(Context context, BroadcastReceiver receiver) {
        if (context == null) {
            return;
        }

        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context);
        manager.unregisterReceiver(receiver);
    }

    /**
     * 发送本地广播
     *
     * @param context
     * @param intent
     */
    public static void sendLocalBroadcast(Context context, Intent intent) {
        if (context == null) {
            return;
        }

        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context);
        manager.sendBroadcast(intent);
    }

    /**
     * 将Uri对应的内容保存到File中
     *
     * @param context
     * @param uri
     * @param filePath
     */
    public static void saveToFile(Context context, Uri uri, String filePath) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(context.getContentResolver().openInputStream(uri));
            bos = new BufferedOutputStream(new FileOutputStream(filePath, false));
            byte[] buffer = new byte[1024];

            bis.read(buffer);
            do {
                bos.write(buffer);
            } while (bis.read(buffer) != -1);
        } catch (IOException ioe) {

        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {

            }
        }
    }


}
