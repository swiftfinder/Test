package com.snappwish.smarthotel.devicemodule.video;

import com.baidu.duer.dcs.api.BaseDeviceModule;
import com.baidu.duer.dcs.api.IMessageSender;
import com.baidu.duer.dcs.util.message.ClientContext;
import com.baidu.duer.dcs.util.message.Directive;
import com.baidu.duer.dcs.util.message.HandleDirectiveException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.snappwish.smarthotel.devicemodule.video.ApiConstants.VIDEO_ON_DEMAND_RAW_INTENT_NAMESPACE;

public class VideoDeviceModule extends BaseDeviceModule {

    public static VideoDeviceModule createVideoDeviceModule(IMessageSender messageSender) {
        return new VideoDeviceModule(VIDEO_ON_DEMAND_RAW_INTENT_NAMESPACE, messageSender);
    }

    private VideoDeviceModule(String nameSpace, IMessageSender messageSender) {
        super(nameSpace, messageSender);
    }

    private final List<Listener> listeners = new ArrayList<>();

    @Override
    public ClientContext clientContext() {
        return null;
    }

    @Override
    public void handleDirective(Directive directive) throws HandleDirectiveException {
        for (Listener listener : listeners) {
            listener.handleDirective(directive);
        }
    }

    @Override
    public void release() {
        listeners.clear();
    }

    @Override
    public HashMap<String, Class<?>> supportPayload() {
        return null;
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    public interface Listener {
        void handleDirective(Directive directive) throws HandleDirectiveException;
    }
}