package com.snappwish.smarthotel.net

import android.util.Log
import com.snappwish.smarthotel.DeviceEvent
import org.greenrobot.eventbus.EventBus
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Created by jinjin on 2019/1/23.
 * description:
 */
class UpdateLocationExecutor private constructor() {
    private val pool: ExecutorService = Executors.newSingleThreadExecutor()

    fun changeDeviceState(deviceNum: Int, state: Int) {
        val myThread = DeviceThread(deviceNum, state)
        pool.execute(myThread)
    }

    private class DeviceThread(private val deviceNum: Int, private val state: Int) : Thread() {
        override fun run() {
            val map = hashMapOf("deviceNum" to deviceNum, "state" to state)
            val changeStatusResponseCall = HttpApiHelper.apiService.changeStatus(map)
            val body = changeStatusResponseCall.execute().body()
            Log.d("NetApi", "changeStatus: deviceNum ->$deviceNum state ->$state")
            Log.d("NetApi", body.toString())
            if (body != null && body.status == "0") {
                EventBus.getDefault().post(DeviceEvent(deviceNum, state))
                sleep(300)
            }
        }
    }

    companion object {
        val instance = UpdateLocationExecutor()
    }
}