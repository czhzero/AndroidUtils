package com.gezbox.library.utils.utils;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by albert on 9/18/14.
 */
public class ValidateUtils {
    /**
     * 判断邮箱地址是否正确
     *
     * @param email
     * @return
     */
    public static boolean isValidEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    /**
     * 判断手机号码是否正确
     * 
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[^4,\\D])|(16[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);

        return m.matches();
    }

    /**
     * 判断固定电话是否正确
     *
     * @param tel
     * @return
     */
    public static boolean isTel(String tel) {
        Pattern p = Pattern.compile("^((010|021|022|023|0\\d{3})?(\\d{7}|\\d{8}))$");
        Matcher m = p.matcher(tel);

        return m.matches();
    }

    /**
     * 判断小票格式是否正确
     *
     * @param tip
     * @return
     */
    public static boolean isTip(String tip) {
        Pattern p = Pattern.compile("^(\\d{7,12})$");
        Matcher m = p.matcher(tip);

        return m.matches();
    }

    /**
     * 检查身高是否正确
     *
     * @param height
     * @return
     */
    public static boolean isValidHeight(String height) {
        try {
            int h = Integer.parseInt(height);
            if (h < 100 || h > 300) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 检查身份证格式是否正确
     *
     * @param idCard
     * @return
     */
    public static boolean isValidIdCard(String idCard) {
        Pattern p = Pattern.compile("^[1-9][0-7]\\d{4}(((19|20)?\\d{2}(0[469]|11)(0[1-9]|[12]\\d|30))|((19|20)?\\d{2}(0[13578]|1[02])(0[1-9]|[12]\\d|30|31))|((19|20)?\\d{2}02(0[1-9]|1\\d|2[0-9])))\\d{3}(\\d|X|x)?$");
        Matcher m = p.matcher(idCard);

        return m.matches();
    }
}
