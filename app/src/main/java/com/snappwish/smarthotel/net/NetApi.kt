package com.snappwish.smarthotel.net

import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by jinjin on 2019/1/23.
 * description:
 */
object NetApi {
    fun reqServer() {
        HttpApiHelper.apiService.login()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (result.status == "0") {
                        Log.d("NetApi", "login success")
                    }
                }, Throwable::printStackTrace)
    }

    fun changeStatus(deviceNum: Int, state: Int) {
        UpdateLocationExecutor.instance.changeDeviceState(deviceNum, state)
    }

    const val TIAO_GUANG_SWITCH: Int = 0
    const val LEFT_LIGHT: Int = 1
    const val RIGHT_LIGHT: Int = 2
    const val BORD: Int = 3
    const val LIGHT_OPEN = 1
    const val LIGHT_CLOSE = 0
    const val SWITCH_OPEN = 1
    const val SWITCH_CLOSE = 2

    var tiaoGuangSwitch: Int = 50
        get() = field
}