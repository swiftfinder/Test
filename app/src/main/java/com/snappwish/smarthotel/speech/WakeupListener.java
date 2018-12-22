package com.snappwish.smarthotel.speech;

/**
 * @author lishibo
 * @date 2018/12/22
 * email : andy_li@swift365.com.cn
 */
public interface WakeupListener {
    /**
     * 唤醒成功
     */
    void wakeupSuccess();

    /**
     * 唤醒失败
     *
     * @param failedMsg 失败的原因
     */
    void wakeupFailed(String failedMsg);
}
