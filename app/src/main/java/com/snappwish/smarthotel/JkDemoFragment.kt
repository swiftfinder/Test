package com.snappwish.smarthotel

import android.widget.SeekBar
import com.snappwish.smarthotel.base.IEventBus
import com.snappwish.smarthotel.base.MyBaseFragment
import com.snappwish.smarthotel.net.NetApi
import com.snappwish.smarthotel.net.NetApi.BORD
import com.snappwish.smarthotel.net.NetApi.LEFT_LIGHT
import com.snappwish.smarthotel.net.NetApi.LIGHT_CLOSE
import com.snappwish.smarthotel.net.NetApi.LIGHT_OPEN
import com.snappwish.smarthotel.net.NetApi.RIGHT_LIGHT
import com.snappwish.smarthotel.net.NetApi.SWITCH_CLOSE
import com.snappwish.smarthotel.net.NetApi.SWITCH_OPEN
import com.snappwish.smarthotel.net.NetApi.TIAO_GUANG_SWITCH
import com.snappwish.smarthotel.net.NetApi.tiaoGuangSwitch
import kotlinx.android.synthetic.main.fragment_jk_demo.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * @author lishibo
 * @date 2019/1/12
 * email : andy_li@swift365.com.cn
 */
class JkDemoFragment : MyBaseFragment(), IEventBus {

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
                    NetApi.changeStatus(TIAO_GUANG_SWITCH, p0.progress)
                }

            }

        })
    }

    override fun initData() {
        btn_l1_open.setOnClickListener { NetApi.changeStatus(LEFT_LIGHT, LIGHT_OPEN) }
        btn_l1_close.setOnClickListener { NetApi.changeStatus(LEFT_LIGHT, LIGHT_CLOSE) }
        btn_l2_open.setOnClickListener { NetApi.changeStatus(RIGHT_LIGHT, LIGHT_OPEN) }
        btn_l2_close.setOnClickListener { NetApi.changeStatus(RIGHT_LIGHT, LIGHT_CLOSE) }
        btn_switch_open.setOnClickListener { NetApi.changeStatus(BORD, SWITCH_OPEN) }
        btn_switch_close.setOnClickListener { NetApi.changeStatus(BORD, SWITCH_CLOSE) }

    }

    override fun destroyData() {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onDeviceStateChange(deviceEvent: DeviceEvent) {
        when (deviceEvent.device) {
            TIAO_GUANG_SWITCH -> seek_bar.progress = deviceEvent.state
        }
    }
}