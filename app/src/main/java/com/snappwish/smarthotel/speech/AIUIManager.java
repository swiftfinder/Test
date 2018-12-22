package com.snappwish.smarthotel.speech;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.iflytek.aiui.AIUIAgent;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.aiui.AIUIEvent;
import com.iflytek.aiui.AIUIListener;
import com.iflytek.aiui.AIUIMessage;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author lishibo
 * @date 2018/12/22
 * email : andy_li@swift365.com.cn
 */
public class AIUIManager {
    private static final String TAG = "AIUIManager";
    private static AIUIManager instance = null;
    private AIUIAgent mAIUIAgent = null;
    private int mAIUIState = AIUIConstant.STATE_IDLE;
    private Context mContext;
    private VoiceNplListener mListener;

    private AIUIManager() {
    }

    public static AIUIManager getInstance() {
        synchronized (AIUIManager.class) {
            if (instance == null) {
                instance = new AIUIManager();
            }
        }

        return instance;
    }

    public void startVoiceNlp(Context context, VoiceNplListener listener) {
        mContext = context;
        mListener = listener;
        if (!checkAIUIAgent()) {
            return;
        }
        Log.i(TAG, "start voice nlp");
        if (AIUIConstant.STATE_WORKING != this.mAIUIState) {
            AIUIMessage wakeupMsg = new AIUIMessage(AIUIConstant.CMD_WAKEUP, 0, 0, "", null);
            mAIUIAgent.sendMessage(wakeupMsg);
        }

        String params = "sample_rate=16000,data_type=audio";
        AIUIMessage writeMsg = new AIUIMessage(AIUIConstant.CMD_START_RECORD, 0, 0, params, null);
        mAIUIAgent.sendMessage(writeMsg);
    }

    /**
     * 读取配置
     */
    private String getAIUIParams() {
        String params = "";
        AssetManager assetManager = mContext.getResources().getAssets();
        try {
            InputStream ins = assetManager.open("cfg/aiui_phone.cfg");
            byte[] buffer = new byte[ins.available()];

            ins.read(buffer);
            ins.close();

            params = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return params;
    }

    private boolean checkAIUIAgent() {
        if (null == mAIUIAgent) {
            Log.i(TAG, "creat AIUI agent");

            //创建AIUIAgent
            mAIUIAgent = AIUIAgent.createAgent(mContext, getAIUIParams(), mAIUIListener);
        }

        if (null == mAIUIAgent) {
            final String strErrorTip = "创建 AIUI Agent 失败！";
            mListener.onFailed(strErrorTip);
        }

        return null != mAIUIAgent;
    }


    private AIUIListener mAIUIListener = new AIUIListener() {

        @Override
        public void onEvent(AIUIEvent event) {
            switch (event.eventType) {
                case AIUIConstant.EVENT_WAKEUP:
                    //唤醒事件
                    break;

                case AIUIConstant.EVENT_RESULT:
                    //结果事件
                    try {
                        JSONObject bizParamJson = new JSONObject(event.info);
                        JSONObject data = bizParamJson.getJSONArray("data").getJSONObject(0);
                        JSONObject params = data.getJSONObject("params");
                        JSONObject content = data.getJSONArray("content").getJSONObject(0);

                        if (content.has("cnt_id")) {
                            String cntId = content.getString("cnt_id");
                            JSONObject cntJson = new JSONObject(new String(event.data.getByteArray(cntId), "utf-8"));

                            String sub = params.optString("sub");
                            JSONObject result = cntJson.optJSONObject("intent");
                            if ("nlp".equals(sub) && result.length() > 2) {
                                // 解析得到语义结果
                                String str = "";
                                //在线语义结果
                                if (result.optInt("rc") == 0) {
                                    JSONObject answer = result.optJSONObject("answer");
                                    if (answer != null) {
                                        str = answer.optString("text");
                                    }
                                } else {
                                    str = "出错了哟";
                                    mListener.onFailed(str);
                                }
                                mListener.onSuccess(str);
                                stopSentMsg();

                            }
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                        mListener.onFailed(e.toString());
                    }
                    break;
                //  错误事件
                case AIUIConstant.EVENT_ERROR:
                    mListener.onFailed(event.arg1 + "\n" + event.info);
                    break;

                case AIUIConstant.EVENT_VAD:
                    //vad事件
                    if (AIUIConstant.VAD_BOS == event.arg1) {
                        //找到语音前端点
                    } else if (AIUIConstant.VAD_EOS == event.arg1) {
                        //找到语音后端点
                    } else {
                    }

                    break;

                // 开始录音事件
                case AIUIConstant.EVENT_START_RECORD:
                    break;

                //  停止录音事件
                case AIUIConstant.EVENT_STOP_RECORD:
                    break;

                case AIUIConstant.EVENT_STATE:
                    mAIUIState = event.arg1;
                    if (AIUIConstant.STATE_IDLE == mAIUIState) {
                        // 闲置状态，AIUI未开启
                    } else if (AIUIConstant.STATE_READY == mAIUIState) {
                        // AIUI已就绪，等待唤醒
                    } else if (AIUIConstant.STATE_WORKING == mAIUIState) {
                        // AIUI工作中，可进行交互
                    }
                    break;
                default:
                    break;
            }
        }

    };

    public void stopSentMsg() {
        if (null != mAIUIAgent) {
            AIUIMessage stopMsg = new AIUIMessage(AIUIConstant.CMD_STOP, 0, 0, null, null);
            mAIUIAgent.sendMessage(stopMsg);

            mAIUIAgent.destroy();
            mAIUIAgent = null;
        }
    }

}
