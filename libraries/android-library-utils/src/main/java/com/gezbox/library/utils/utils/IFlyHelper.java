package com.gezbox.library.utils.utils;

import android.content.Context;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;

/**
 * Created by chenzhaohua on 16/1/28.
 */
public class IFlyHelper {

    private static IFlyHelper instance;

    private final String KEY_IFLY_APPID = "54d03721";

    // 语音合成对象
    private SpeechSynthesizer mTts;

    private IFlyHelper(Context context) {
        super();
        SpeechUtility.createUtility(context, "appid=" + KEY_IFLY_APPID);
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(context, null);
        // 设置参数
        setParam();
    }

    public static IFlyHelper getInstance(Context context) {
        if (instance == null) {
            instance = new IFlyHelper(context);

        }

        return instance;
    }

    public int notifyNewMsg(String msg) {
        return mTts.startSpeaking(msg, null);
    }

    /**
     * 参数设置
     */
    private void setParam() {
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);

        //设置合成
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);

        //设置发音人
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");

        //设置语速
        mTts.setParameter(SpeechConstant.SPEED, "50");

        //设置音调
        mTts.setParameter(SpeechConstant.PITCH, "50");

        //设置音量
        mTts.setParameter(SpeechConstant.VOLUME, "50");

        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
    }

}
