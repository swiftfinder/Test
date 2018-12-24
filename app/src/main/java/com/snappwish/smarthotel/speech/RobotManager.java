package com.snappwish.smarthotel.speech;

import android.content.Context;
import android.util.Log;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

/**
 * @author lishibo
 * @date 2018/12/22
 * email : andy_li@swift365.com.cn
 */
public class RobotManager {
    private static final String TAG = "RobotManager";

    private static RobotManager sRobotManager = null;

    public static RobotManager getInstance() {
        if (null == sRobotManager) {
            sRobotManager = new RobotManager();
        }
        return sRobotManager;
    }

    public void init(Context context) {
        SpeechUtility.createUtility(context, SpeechConstant.APPID + SpeechConfig.IFLYTEK_ID);
        initTTSEngine(context);
    }

    private STTEngine mSTTEngine = null;

    /**
     * init stt engine
     *
     * @param context
     */
    public void initSTTEngine(Context context) {
        mSTTEngine = new STTEngine(context);
    }

    /**
     * init tts engine
     *
     * @param context context
     */
    public void initTTSEngine(Context context) {
        mTTSEngine = new TTSEngine(context);
        RobotManager.getInstance().setVoiceState(true);
        RobotManager.getInstance().setVoiceType("xiaoyan");
    }

    public void startRecognizing(STTListener sttListener) {
        if (null == mSTTEngine) {
            Log.e(TAG, "STT engine is not init.");
            return;
        }
        mSTTEngine.startRecognizing(sttListener);
    }

    private TTSEngine mTTSEngine = null;

    /**
     * speak out the content
     *
     * @param content content should be speaked out
     */
    public void startSpeaking(String content) {
        if (null == mTTSEngine) {
            Log.e(TAG, "TTS engine is not init.");
            return;
        }
        mTTSEngine.startSpeaking(content);
    }

    /**
     * stop speaking
     */
    public void stopSpeaking() {
        if (null == mTTSEngine) {
            Log.e(TAG, "TTS engine is not init.");
            return;
        }
        mTTSEngine.stopSpeaking();
    }

    /**
     * set voice type
     *
     * @param type see VoiceType class
     */
    public void setVoiceType(String type) {
        if (null == mTTSEngine) {
            Log.e(TAG, "TTS engine is not init.");
            return;
        }
        mTTSEngine.setVoiceType(type);
    }

    /**
     * set voice speaking close or open state
     *
     * @param on, true, permission speaking, false, shut speaking
     */
    public void setVoiceState(boolean on) {
        if (null == mTTSEngine) {
            Log.e(TAG, "TTS engine is not init.");
            return;
        }
        mTTSEngine.setVoiceSpeakSwitch(on);
    }
}
