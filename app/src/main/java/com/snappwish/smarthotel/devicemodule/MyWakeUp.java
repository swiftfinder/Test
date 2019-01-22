package com.snappwish.smarthotel.devicemodule;

import com.baidu.duer.dcs.api.recorder.BaseAudioRecorder;
import com.baidu.duer.dcs.api.wakeup.BaseWakeup;
import com.baidu.duer.dcs.api.wakeup.IStopWakeupListener;

/**
 * Created by jinjin on 2019/1/22.
 * description:
 */
public class MyWakeUp extends BaseWakeup {

    private BaseAudioRecorder.IRecorderListener recorderListener = new BaseAudioRecorder.SimpleRecorderListener() {
        @Override
        public void onData(byte[] data) {
            // 这里接收语音的未压缩的pcm数据
            // ...
        }
    };

    @Override
    public void startWakeup() {

    }

    @Override
    public void stopWakeup(IStopWakeupListener iStopWakeupListener) {

    }
}
