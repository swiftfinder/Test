package com.snappwish.smarthotel

import android.support.design.widget.TabLayout
import android.widget.CompoundButton
import android.widget.SeekBar
import com.snappwish.smarthotel.base.IEventBus
import com.snappwish.smarthotel.base.MyBaseFragment
import com.snappwish.smarthotel.net.NetApi
import com.snappwish.smarthotel.net.NetApi.BORD
import com.snappwish.smarthotel.net.NetApi.BORD_CLOSE
import com.snappwish.smarthotel.net.NetApi.BORD_OPEN
import com.snappwish.smarthotel.net.NetApi.LEFT_LIGHT
import com.snappwish.smarthotel.net.NetApi.LIGHT_CLOSE
import com.snappwish.smarthotel.net.NetApi.LIGHT_OPEN
import com.snappwish.smarthotel.net.NetApi.RIGHT_LIGHT
import com.snappwish.smarthotel.net.NetApi.TIAO_GUANG_SWITCH
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
        tab_controller.addTab(tab_controller.newTab().setText("自定义模式"))
        tab_controller.addTab(tab_controller.newTab().setText("明亮模式"))
        tab_controller.addTab(tab_controller.newTab().setText("阅读模式"))
        tab_controller.addTab(tab_controller.newTab().setText("电视模式"))
        tab_controller.addTab(tab_controller.newTab().setText("浪漫模式"))
        tab_controller.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                var text = tab!!.text
                if (text == "自定义模式") {

                }else if (text == "明亮模式") {
                    NetApi.changeStatus(TIAO_GUANG_SWITCH, 100)
                    NetApi.changeStatus(LEFT_LIGHT, LIGHT_OPEN)
                    NetApi.changeStatus(RIGHT_LIGHT, LIGHT_OPEN)
                    NetApi.changeStatus(BORD, BORD_OPEN)
                }

            }

        })
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
        sb_bedside_lamp.setOnCheckedChangeListener { compoundButton: CompoundButton, isChecked: Boolean ->
            if (isChecked) {
                NetApi.changeStatus(TIAO_GUANG_SWITCH, 100)
            } else {
                NetApi.changeStatus(TIAO_GUANG_SWITCH, 0)
            }
        }
        sb_toilet_light.setOnCheckedChangeListener { compoundButton: CompoundButton, isChecked: Boolean ->
            if (isChecked) {
                NetApi.changeStatus(LEFT_LIGHT, LIGHT_OPEN)
            } else {
                NetApi.changeStatus(LEFT_LIGHT, LIGHT_CLOSE)
            }
        }
        sb_porch_light.setOnCheckedChangeListener { compoundButton: CompoundButton, isChecked: Boolean ->
//            porchStatus(isChecked)
            showToast("暂不支持此功能")
            sb_porch_light.isChecked = false
        }
        sb_king_light.setOnCheckedChangeListener { compoundButton: CompoundButton, isChecked: Boolean ->
            if (isChecked) {
                NetApi.changeStatus(RIGHT_LIGHT, LIGHT_OPEN)
            } else {
                NetApi.changeStatus(RIGHT_LIGHT, LIGHT_CLOSE)
            }
        }
        sb_bord.setOnCheckedChangeListener { compoundButton: CompoundButton, isChecked: Boolean ->
            if (isChecked) {
                NetApi.changeStatus(BORD, BORD_OPEN)
            } else {
                NetApi.changeStatus(BORD, BORD_CLOSE)
            }
        }
        sb_air_conditioner.setOnCheckedChangeListener { compoundButton: CompoundButton, isChecked: Boolean ->
//            airConditionerStatus(isChecked)
            showToast("暂不支持此功能")
            sb_air_conditioner.isChecked = false
        }
    }

    override fun destroyData() {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onDeviceStateChange(deviceEvent: DeviceEvent) {
        when (deviceEvent.device) {
            TIAO_GUANG_SWITCH -> bedSideStatus(deviceEvent.state)
            LEFT_LIGHT -> toiletStatus(deviceEvent.state != 0)
            RIGHT_LIGHT -> kingStatus(deviceEvent.state != 0)
            BORD -> bordStatus(deviceEvent.state != 2)
        }
    }

    private fun bedSideStatus(progress: Int) {
        if (progress == 0) {
            iv_bedside_lamp.setImageResource(R.drawable.icon_bedside_lamp_close)
            tv_bedside_lamp.setTextColor(resources.getColor(R.color.text_grey3))
            tv_bedside_lamp_status.text = "关闭"
            tv_bedside_lamp_status.setTextColor(resources.getColor(R.color.text_grey3))
            sb_bedside_lamp.setCheckedNoEvent(false)
            seek_bar.progress = progress
        } else {
            iv_bedside_lamp.setImageResource(R.drawable.icon_bedside_lamp_open)
            tv_bedside_lamp.setTextColor(resources.getColor(R.color.text_white))
            tv_bedside_lamp_status.setTextColor(resources.getColor(R.color.text_green))
            tv_bedside_lamp_status.text = progress.toString() + "%"
            sb_bedside_lamp.setCheckedNoEvent(true)
            seek_bar.progress = progress
        }
    }

    private fun toiletStatus(state: Boolean) {
        if (state) {
            iv_toilet_light.setImageResource(R.drawable.icon_toilet_light_open)
            tv_toilet_light.setTextColor(resources.getColor(R.color.text_white))
            tv_toilet_light_status.setTextColor(resources.getColor(R.color.text_green))
            tv_toilet_light_status.text = "打开"
            sb_toilet_light.setCheckedNoEvent(true)
        } else {
            iv_toilet_light.setImageResource(R.drawable.icon_toilet_light_close)
            tv_toilet_light.setTextColor(resources.getColor(R.color.text_grey3))
            tv_toilet_light_status.setTextColor(resources.getColor(R.color.text_grey3))
            tv_toilet_light_status.text = "关闭"
            sb_toilet_light.setCheckedNoEvent(false)
        }
    }

    private fun porchStatus(state: Boolean) {
        if (state) {
            iv_porch_light.setImageResource(R.drawable.icon_porch_light_open)
            tv_porch_light.setTextColor(resources.getColor(R.color.text_white))
            tv_porch_light_status.setTextColor(resources.getColor(R.color.text_green))
            tv_porch_light_status.text = "打开"
            sb_porch_light.setCheckedNoEvent(true)
        } else {
            iv_porch_light.setImageResource(R.drawable.icon_porch_light_close)
            tv_porch_light.setTextColor(resources.getColor(R.color.text_grey3))
            tv_porch_light_status.setTextColor(resources.getColor(R.color.text_grey3))
            tv_porch_light_status.text = "关闭"
            sb_porch_light.setCheckedNoEvent(false)
        }
    }

    private fun kingStatus(state: Boolean) {
        if (state) {
            iv_king_light.setImageResource(R.drawable.icon_king_light_open)
            tv_king_light.setTextColor(resources.getColor(R.color.text_white))
            tv_king_light_status.setTextColor(resources.getColor(R.color.text_green))
            tv_king_light_status.text = "打开"
            sb_king_light.setCheckedNoEvent(true)
        } else {
            iv_king_light.setImageResource(R.drawable.icon_king_light_close)
            tv_king_light.setTextColor(resources.getColor(R.color.text_grey3))
            tv_king_light_status.setTextColor(resources.getColor(R.color.text_grey3))
            tv_king_light_status.text = "关闭"
            sb_king_light.setCheckedNoEvent(false)
        }
    }

    private fun bordStatus(state: Boolean) {
        if (state) {
            iv_bord.setImageResource(R.drawable.icon_bord_open)
            tv_bord.setTextColor(resources.getColor(R.color.text_white))
            tv_bord_status.setTextColor(resources.getColor(R.color.text_green))
            tv_bord_status.text = "打开"
            sb_bord.setCheckedNoEvent(true)
        } else {
            iv_bord.setImageResource(R.drawable.icon_bord_close)
            tv_bord.setTextColor(resources.getColor(R.color.text_grey3))
            tv_bord_status.setTextColor(resources.getColor(R.color.text_grey3))
            tv_bord_status.text = "关闭"
            sb_bord.setCheckedNoEvent(false)
        }
    }

    private fun airConditionerStatus(state: Boolean) {
        if (state) {
            iv_air_conditioner.setImageResource(R.drawable.icon_air_conditioner_open)
            tv_air_conditioner.setTextColor(resources.getColor(R.color.text_white))
            tv_air_conditioner_status.setTextColor(resources.getColor(R.color.text_green))
            tv_air_conditioner_status.text = "制热 26℃"
            iv_refrigeration.setImageResource(R.drawable.icon_refrigeration_open)
            iv_timing.setImageResource(R.drawable.icon_timing_close)
            iv_heating.setImageResource(R.drawable.icon_heating_close)
            iv_auto.setImageResource(R.drawable.icon_timing_close)
            iv_wind_small.setImageResource(R.drawable.icon_wind_small_open)
            iv_wind_mid.setImageResource(R.drawable.icon_wind_mid_close)
            iv_wind_big.setImageResource(R.drawable.icon_wind_big_close)
        } else {
            iv_air_conditioner.setImageResource(R.drawable.icon_air_conditioner_close)
            tv_air_conditioner.setTextColor(resources.getColor(R.color.text_grey3))
            tv_air_conditioner_status.setTextColor(resources.getColor(R.color.text_grey3))
            tv_air_conditioner_status.text = "关闭"
            iv_refrigeration.setImageResource(R.drawable.icon_refrigeration_close)
            iv_timing.setImageResource(R.drawable.icon_timing_close)
            iv_heating.setImageResource(R.drawable.icon_heating_close)
            iv_auto.setImageResource(R.drawable.icon_timing_close)
            iv_wind_small.setImageResource(R.drawable.icon_wind_small_close)
            iv_wind_mid.setImageResource(R.drawable.icon_wind_mid_close)
            iv_wind_big.setImageResource(R.drawable.icon_wind_big_close)
        }
    }
}