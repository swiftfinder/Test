package com.snappwish.smarthotel.speech;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.VoiceWakeuper;
import com.iflytek.cloud.WakeuperListener;
import com.iflytek.cloud.WakeuperResult;
import com.iflytek.cloud.util.ResourceUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author lishibo
 * @date 2018/12/22
 * email : andy_li@swift365.com.cn
 */
public class WakeupManager {
    private static WakeupManager instance = null;
    private static final String TAG = "WakeupManager";
    private Context mContext;
    // 唤醒的阈值，就相当于门限值，当用户输入的语音的置信度大于这一个值的时候，才被认定为成功唤醒。
    private int mCurrentThresh = 1500;
    // 是否持续唤醒
    private String mKeepAlive = "1";
    /**
     * 闭环优化网络模式有三种：
     * 模式0：关闭闭环优化功能
     * <p>
     * 模式1：开启闭环优化功能，允许上传优化数据。需开发者自行管理优化资源。
     * sdk提供相应的查询和下载接口，请开发者参考API文档，具体使用请参考本示例
     * queryResource及downloadResource方法；
     * <p>
     * 模式2：开启闭环优化功能，允许上传优化数据及启动唤醒时进行资源查询下载；
     * 本示例为方便开发者使用仅展示模式0和模式2；
     */
    private String mIvwNetMode = "0";
    // 语音唤醒对象
    private VoiceWakeuper mIvw;
    // 存储唤醒词的ID
    private String wordID = "";
    private WakeupListener mListener;

    private WakeupManager(Context context) {

        mContext = context;
        mIvw = VoiceWakeuper.createWakeuper(context, null);
    }

    public static WakeupManager getInstance(Context context) {
        synchronized (WakeupManager.class) {
            if (instance == null) {
                instance = new WakeupManager(context);
            }
        }

        return instance;
    }

    public void startWakeup(WakeupListener listener) {
        mIvw = VoiceWakeuper.getWakeuper();
        mListener = listener;
        if (mIvw != null) {
            // 清空参数
            mIvw.setParameter(SpeechConstant.PARAMS, null);
            // 唤醒门限值，根据资源携带的唤醒词个数按照“id:门限;id:门限”的格式传入
            mIvw.setParameter(SpeechConstant.IVW_THRESHOLD, "0:" + mCurrentThresh);
            // 设置唤醒模式
            mIvw.setParameter(SpeechConstant.IVW_SST, "wakeup");
            // 设置持续进行唤醒
            mIvw.setParameter(SpeechConstant.KEEP_ALIVE, mKeepAlive);
            // 设置闭环优化网络模式
            mIvw.setParameter(SpeechConstant.IVW_NET_MODE, mIvwNetMode);
            // 设置唤醒资源路径
            mIvw.setParameter(SpeechConstant.IVW_RES_PATH, getResource());
            // 设置唤醒录音保存路径，保存最近一分钟的音频
            mIvw.setParameter(SpeechConstant.IVW_AUDIO_PATH, Environment.getExternalStorageDirectory().getPath() + "/msc/ivw.wav");
            mIvw.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
            // 如有需要，设置 NOTIFY_RECORD_DATA 以实时通过 onEvent 返回录音音频流字节
            //mIvw.setParameter( SpeechConstant.NOTIFY_RECORD_DATA, "1" );

            // 启动唤醒
            mIvw.startListening(new MyWakeupListener());
        }
    }

    /**
     * 获取唤醒词功能
     *
     * @return 返回文件位置
     */
    private String getResource() {
        return ResourceUtil.generateResourcePath(mContext, ResourceUtil.RESOURCE_TYPE.assets, "ivw/5c1d979f.jet");
    }

    /**
     * 销毁唤醒功能
     */
    public void destroyWakeup() {
        // 销毁合成对象
        mIvw = VoiceWakeuper.getWakeuper();
        if (mIvw != null) {
            mIvw.destroy();
        }
    }

    /**
     * 停止唤醒
     */
    public void stopWakeup() {
        mIvw.stopListening();
    }

    /**
     * 唤醒词监听类
     *
     * @author Administrator
     */
    private class MyWakeupListener implements WakeuperListener {
        //开始说话
        @Override
        public void onBeginOfSpeech() {
            Log.e(TAG, "start speech");
        }

        //错误码返回
        @Override
        public void onError(SpeechError arg0) {
            Log.e(TAG, arg0.getErrorDescription());
            mListener.wakeupFailed(arg0.getErrorDescription());
        }

        @Override
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {

        }

        @Override
        public void onVolumeChanged(int i) {

        }

        @Override
        public void onResult(WakeuperResult result) {
            if (!"1".equalsIgnoreCase(mKeepAlive)) {
                //setRadioEnable(true);
            }
            try {
                String text = result.getResultString();
                JSONObject object;
                object = new JSONObject(text);
                StringBuffer buffer = new StringBuffer();
                buffer.append("【RAW】 " + text);
                buffer.append("\n");
                buffer.append("【操作类型】" + object.optString("sst"));
                buffer.append("\n");
                buffer.append("【唤醒词id】" + object.optString("id"));
                buffer.append("\n");
                buffer.append("【得分】" + object.optString("score"));
                buffer.append("\n");
                buffer.append("【前端点】" + object.optString("bos"));
                buffer.append("\n");
                buffer.append("【尾端点】" + object.optString("eos"));
                Log.e(TAG, buffer.toString());
                mListener.wakeupSuccess();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
