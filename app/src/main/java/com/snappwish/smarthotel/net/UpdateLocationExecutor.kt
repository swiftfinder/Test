package com.snappwish.smarthotel.net

import android.util.Log
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Created by jinjin on 2019/1/23.
 * description:
 */
class UpdateLocationExecutor private constructor() {
    private val pool: ExecutorService

    init {
        pool = Executors.newSingleThreadExecutor()
    }

    fun changeDeviceState(deviceNum: Int, state: Int) {
        val myThread = DeviceThread(deviceNum, state)
        pool.execute(myThread)
    }

    private class DeviceThread(private val deviceNum: Int, private val state: Int) : Thread() {
        override fun run() {
            val map = hashMapOf("deviceNum" to deviceNum, "state" to state)
            val changeStatusResponseCall = HttpApiHelper.apiService.changeStatus(map)
            val body = changeStatusResponseCall.execute().body()
            if (body != null) {
                Log.d("NetApi", "changeStatus: deviceNum ->$deviceNum state ->$state")
                Log.d("NetApi", body.toString())
                sleep(300)
            }
        }
    }

    companion object {

        val instance = UpdateLocationExecutor()
    }
}