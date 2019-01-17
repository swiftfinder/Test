package com.snappwish.smarthotel

import android.util.Log
import android.widget.SeekBar
import android.widget.Toast
import com.snappwish.smarthotel.base.IEventBus
import com.snappwish.smarthotel.base.MyBaseFragment
import com.snappwish.smarthotel.net.HttpApiHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_jk_demo.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * @author lishibo
 * @date 2019/1/12
 * email : andy_li@swift365.com.cn
 */
class JkDemoFragment : MyBaseFragment(), IEventBus {

    var tiaoGuangSwitch: Int = 50


    override fun getContentView(): Int {
        return R.layout.fragment_jk_demo
    }

    override fun initView() {
        seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                if (p0 != null) {
                    changeStatus(TIAO_GUANG_SWITCH, p0.progress)
                }

            }

        })
    }

    override fun initData() {
        reqServer()
//        btn_login.setOnClickListener { reqServer() }
        btn_l1_open.setOnClickListener { changeStatus(LEFT_LIGHT, LIGHT_OPEN) }
        btn_l1_close.setOnClickListener { changeStatus(LEFT_LIGHT, LIGHT_CLOSE) }
        btn_l2_open.setOnClickListener { changeStatus(RIGHT_LIGHT, LIGHT_OPEN) }
        btn_l2_close.setOnClickListener { changeStatus(RIGHT_LIGHT, LIGHT_CLOSE) }
        btn_switch_open.setOnClickListener { changeStatus(BORD, SWITCH_OPEN) }
        btn_switch_close.setOnClickListener { changeStatus(BORD, SWITCH_CLOSE) }

    }

    override fun destroyData() {
    }

    public fun reqServer() {
        HttpApiHelper.apiService.login()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (result.status == "0") {
                        Toast.makeText(context, "login success", Toast.LENGTH_LONG).show()
                    }
                }, Throwable::printStackTrace)
    }

    private fun changeStatus(deviceNum: Int, state: Int) {
        val map = hashMapOf("deviceNum" to deviceNum, "state" to state)
        HttpApiHelper.apiService.changeStatus(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    Log.d("Kotlin", result.toString())
                    if (result.status == "0") {
                        Toast.makeText(context, "ok", Toast.LENGTH_SHORT).show()
                    }
                }, { error ->
                    error.printStackTrace()
                })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onDeviceStateChange(deviceEvent: DeviceEvent) {

        if (deviceEvent.device == TIAO_GUANG_SWITCH) {
            tiaoGuangSwitch += deviceEvent.state
            if (tiaoGuangSwitch >= 100) {
                tiaoGuangSwitch = 100
            } else if (tiaoGuangSwitch <= 0) {
                tiaoGuangSwitch = 0
            }
            seek_bar.progress = tiaoGuangSwitch
            deviceEvent.state = tiaoGuangSwitch
        }

        changeStatus(deviceEvent.device, deviceEvent.state)
    }

    companion object {
        const val TIAO_GUANG_SWITCH: Int = 0
        const val LEFT_LIGHT: Int = 1
        const val RIGHT_LIGHT: Int = 2
        const val BORD: Int = 3
        const val LIGHT_OPEN = 1
        const val LIGHT_CLOSE = 0
        const val SWITCH_OPEN = 1
        const val SWITCH_CLOSE = 2
    }


}