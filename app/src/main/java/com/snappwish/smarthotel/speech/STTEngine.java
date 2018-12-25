package com.snappwish.smarthotel.speech;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

/**
 * @author lishibo
 * @date 2018/12/22
 * email : andy_li@swift365.com.cn
 */
public class STTEngine {
    private SpeechRecognizer mIat;
    private RecognizerDialog iatDialog;
    private boolean mHasDialog;

    public STTEngine(Context context, boolean hasDialog) {
        mHasDialog = hasDialog;
        if (hasDialog) {
            iatDialog = new RecognizerDialog(context, mInitListener);
            //        iatDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        } else {
            mIat = SpeechRecognizer.createRecognizer(context, mInitListener);
            mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            mIat.setParameter(SpeechConstant.VAD_BOS, "4000");
        }

    }

    private InitListener mInitListener = new InitListener() {
        @Override
        public void onInit(int code) {

            if (code != ErrorCode.SUCCESS) {
                Log.e("STTEngine", "初始化失败，错误码：" + code);
            }
        }
    };

    public void startRecognizing(final STTListener sttListener) {
        if (mHasDialog) {
            iatDialog.setListener(new RecognizerDialogListener() {
                String text = "";

                @Override
                public void onResult(RecognizerResult recognizerResult, boolean complete) {
                    Log.i("STTEngine", "stt result = " + recognizerResult.getResultString());
                    text += JsonParser.parseIatResult(recognizerResult.getResultString());
                    if (complete) {
                        sttListener.sttSuccess(text);
                    }
                    Log.e("STTEngine", text);
                }

                @Override
                public void onError(SpeechError speechError) {
                    sttListener.sttFailed(speechError.getErrorDescription());
                }
            });
            iatDialog.show();
        } else {

            mIat.startListening(new RecognizerListener() {
                String text = "";

                @Override
                public void onVolumeChanged(int i, byte[] bytes) {

                }

                @Override
                public void onBeginOfSpeech() {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onResult(RecognizerResult recognizerResult, boolean complete) {
                    Log.i("STTEngine", "stt result = " + recognizerResult.getResultString());
                    text += JsonParser.parseIatResult(recognizerResult.getResultString());
                    if (complete) {
                        sttListener.sttSuccess(text);
                    }
                    Log.e("STTEngine", text);
                }

                @Override
                public void onError(SpeechError speechError) {
                    sttListener.sttFailed(speechError.getErrorDescription());
                }

                @Override
                public void onEvent(int i, int i1, int i2, Bundle bundle) {

                }
            });
        }
    }
}
