package com.snappwish.smarthotel;

/**
 * Created by jinjin on 2019/1/17.
 * description:
 */
public class DeviceEvent {
    private int device;
    private int state;

    public DeviceEvent(int device, int state) {
        this.device = device;
        this.state = state;
    }

    public int getDevice() {
        return device;
    }

    public int getState() {
        return state;
    }

    public void setDevice(int device) {
        this.device = device;
    }

    public void setState(int state) {
        this.state = state;
    }
}
