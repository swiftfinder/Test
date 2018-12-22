package com.snappwish.smarthotel.speech;

/**
 * @author lishibo
 * @date 2018/12/22
 * email : andy_li@swift365.com.cn
 */
public interface VoiceNplListener {
    /**
     * success
     *
     * @param content
     */
    void onSuccess(String content);

    /**
     * failed
     *
     * @param failedMsg
     */
    void onFailed(String failedMsg);
}
