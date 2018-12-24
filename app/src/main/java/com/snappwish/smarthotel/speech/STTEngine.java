package com.snappwish.smarthotel.speech;

import android.content.Context;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

/**
 * @author lishibo
 * @date 2018/12/22
 * email : andy_li@swift365.com.cn
 */
public class STTEngine {
    private RecognizerDialog iatDialog;

    public STTEngine(Context context) {
        iatDialog = new RecognizerDialog(context, mInitListener);
        //        iatDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
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
                sttListener.sttFailed();
            }
        });
        iatDialog.show();
    }
}
