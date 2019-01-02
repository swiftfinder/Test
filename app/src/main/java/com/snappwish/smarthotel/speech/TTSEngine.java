package com.snappwish.smarthotel.speech;

import android.content.Context;
import android.os.Bundle;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.snappwish.smarthotel.Constant;

/**
 * @author lishibo
 * @date 2018/12/22
 * email : andy_li@swift365.com.cn
 */
public class TTSEngine {
    SpeechSynthesizer mTts;
    boolean mVoiceSwitch = true;

    public TTSEngine(Context context) {
        mTts = SpeechSynthesizer.createSynthesizer(context, null);
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        mTts.setParameter(SpeechConstant.SPEED, "50");
        mTts.setParameter(SpeechConstant.VOLUME, "50");
//        mTts.setParameter(SpeechConstant.LANGUAGE, Constant.LANGUAGE);
//        mTts.setParameter(SpeechConstant.ACCENT, null);
    }

    /**
     * set voice type to speech
     */
    public void setVoiceType(String type) {
        mTts.setParameter(SpeechConstant.VOICE_NAME, type);
    }

    /**
     * put on or off speaking
     *
     * @param on speaking state
     */
    public void setVoiceSpeakSwitch(boolean on) {
        mVoiceSwitch = on;
    }

    /**
     * stop speaking
     */
    public void stopSpeaking() {
        mTts.stopSpeaking();
    }

    public void destroy() {
        mTts.destroy();
    }

    /**
     * start speaking
     *
     * @param content content to speak
     */
    public void startSpeaking(String content) {
        if (!mVoiceSwitch) {  //if voice switch is not opened, do not speak
            return;
        }
        mTts.startSpeaking(content, new SynthesizerListener() {
            @Override
            public void onSpeakBegin() {

            }

            @Override
            public void onBufferProgress(int i, int i2, int i3, String s) {

            }

            @Override
            public void onSpeakPaused() {

            }

            @Override
            public void onSpeakResumed() {

            }

            @Override
            public void onSpeakProgress(int i, int i2, int i3) {

            }

            @Override
            public void onCompleted(SpeechError speechError) {
                if (listener != null) {
                    listener.onCompleted();
                }
            }

            @Override
            public void onEvent(int i, int i2, int i3, Bundle bundle) {

            }
        });
    }

    private TtsListener listener;

    public void setListener(TtsListener listener) {
        this.listener = listener;
    }

    public interface TtsListener {
        void onCompleted();
    }
}
