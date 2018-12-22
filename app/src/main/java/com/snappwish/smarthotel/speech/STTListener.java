package com.snappwish.smarthotel.speech;

/**
 * @author lishibo
 * @date 2018/12/22
 * email : andy_li@swift365.com.cn
 */
public interface STTListener {
    /**
     * stt success
     * @param content
     */
    void SttSuccess(String content);

    /**
     * stt failed
     */
    void SttFailed();
}
