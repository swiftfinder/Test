package com.snappwish.smarthotel

import android.util.Log
import android.widget.SeekBar
import com.snappwish.base_core.basemvp.BaseFragment
import com.snappwish.smarthotel.net.HttpApiHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_jk_demo.*


/**
 * @author lishibo
 * @date 2019/1/12
 * email : andy_li@swift365.com.cn
 */
class JkDemoFragment : BaseFragment() {

    val TIAO_GUANG_SWITCH: Int = 0
    val LEFT_LIGHT: Int = 1
    val RIGHT_LIGHT: Int = 2
    val BORD: Int = 3
    val LIGHT_OPEN = 1
    val LIGHT_CLOSE = 0
    val SWITCH_OPEN = 1
    val SWITCH_CLOSE = 2

    override fun getContentView(): Int {
        return R.layout.fragment_jk_demo
    }

    override fun initView() {
        seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                showToast("progress is $p1")
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })
    }

    override fun initData() {
        btn_login.setOnClickListener { reqServer() }
        btn_l1_open.setOnClickListener { changeStatus(LEFT_LIGHT, LIGHT_OPEN) }
        btn_l1_close.setOnClickListener { changeStatus(LEFT_LIGHT, LIGHT_CLOSE) }
        btn_l2_open.setOnClickListener { changeStatus(RIGHT_LIGHT, LIGHT_OPEN) }
        btn_l2_close.setOnClickListener { changeStatus(RIGHT_LIGHT, LIGHT_CLOSE) }
        btn_switch_open.setOnClickListener { changeStatus(BORD, SWITCH_OPEN) }
        btn_switch_close.setOnClickListener { changeStatus(BORD, SWITCH_CLOSE) }

    }

    override fun destroyData() {
    }

    private fun reqServer() {
        HttpApiHelper.apiService.login()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (result.status.equals("0")) {
                        showToast("success")
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
                }, { error ->
                    error.printStackTrace()
                }, {
                    Log.d("Kotlin", "onComplete")
                }, {
                    Log.d("Kotlin", "onStart")
                })


    }
}