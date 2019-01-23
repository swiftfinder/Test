package com.snappwish.smarthotel;

import android.Manifest;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.snappwish.base_core.basemvp.FragmentManagerHelper;
import com.snappwish.base_core.permission.PermissionFailure;
import com.snappwish.base_core.permission.PermissionHelper;
import com.snappwish.base_core.permission.PermissionSuccess;
import com.snappwish.smarthotel.base.MyBaseActivity;
import com.snappwish.smarthotel.bean.JsonTools;
import com.snappwish.smarthotel.bean.WeatherBean;
import com.snappwish.smarthotel.net.NetApi;
import com.snappwish.smarthotel.util.Utils;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by jinjin on 2018/12/22.
 * description:
 */

public class MainActivity extends MyBaseActivity {

    private static final String TAG = "MainActivity";
    @BindView(R.id.fragment)
    FrameLayout fragment;
    @BindView(R.id.lottie_view)
    LottieAnimationView lottieAnimationView;

    private static final int PERMISSION_RECORD = 100;
    @BindView(R.id.tv_toast)
    TextView tvToast;
    @BindView(R.id.iv_sleep)
    ImageView ivSleep;


    private FragmentManagerHelper fragmentHelper;
    private WelcomeFragment welcomeFragment;
    private MainFragment mainFragment;
    private MealFragment mealFragment;
    private CleanAndDndstFragment cleanAndDndstFragment;
    private CheckOutFragment checkOutFragment;
    private WeatherFragment weatherFragment;
    private UnsubscribeFragment unsubscribeFragment;
    private PayGoodsFragment payGoodsFragment;
    private VideoFragment videoFragment;
    private JkDemoFragment jkDemoFragment;
    private String language;


    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initTitle() {
    }

    @Override
    protected void initView() {
        fragmentHelper = new FragmentManagerHelper(getSupportFragmentManager(), R.id.fragment);
        NetApi.INSTANCE.reqServer();
    }

    @Override
    protected void initData() {
        PermissionHelper.with(this)
                .requestCode(PERMISSION_RECORD)
                .permissions(Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.INTERNET,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .request();
        Locale locale = getResources().getConfiguration().locale;
        language = locale.getLanguage();
        chooseFragment(Constant.FRAGMENT_MAIN);
        if (language.contains("en")) {
            mWeatherInfo = "Tomorrow will be sunny, with highest temperature at 55 degrees Fahrenheit";
        } else {
                        queryWeather();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.hideBottomUIMenu(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        cancelTimer();
    }

    @Override
    protected void destroyData() {

    }

    @PermissionSuccess(requestCode = PERMISSION_RECORD)
    private void callSuccess() {
        //        WakeupManager.getInstance(this).startWakeup(this);
        initSdk();
        sdkRun();
        initListener();
    }

    @PermissionFailure(requestCode = PERMISSION_RECORD)
    private void callFailure() {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionHelper.onRequestPermissionsResult(this, requestCode, permissions);
    }

    private boolean checkOut = false;

    @Override
    public void sttSuccess(String speaker) {
        String content = "";
        if (!TextUtils.isEmpty(speaker)) {
            content = speaker.toLowerCase();
        }
        Log.e(TAG, content);
        String text = "";
        if (content.contains("天气")
                || content.contains("weather")) {
            text = mWeatherInfo;
            chooseFragment(Constant.FRAGMENT_WEATHER);
        } else if (content.contains("早餐") || content.contains("吃") || content.contains("早饭")
                || content.contains("breakfast") || content.contains("eat")) {
            text = getString(R.string.answer_meal);
            chooseFragment(Constant.FRAGMENT_MEAL);
        } else if (content.contains("新闻")
                || content.contains("news") || content.contains("headline")) {
            text = getString(R.string.answer_video);
            chooseFragment(Constant.FRAGMENT_VIDEO);
        } else if (content.contains("打扫")
                || content.contains("make up the room")) {
            text = getString(R.string.answer_clean_hotel);
            chooseFragment(Constant.FRAGMENT_CLEAN_DNDST, 1);
        } else if (content.contains("打扰")
                || content.contains("disturb")) {
            text = getString(R.string.answer_not_dndst);
            chooseFragment(Constant.FRAGMENT_CLEAN_DNDST, 2);
        } else if (content.contains("关灯") || content.contains("睡觉") || content.contains("休息")
                || content.contains("sleep") || content.contains("turn off")) {
            isSleep = true;
            text = getString(R.string.ansewer_good_night);
            ivSleep.setVisibility(View.VISIBLE);
            sendDeviceEvent(NetApi.TIAO_GUANG_SWITCH, -100);
            sendDeviceEvent(NetApi.LEFT_LIGHT, NetApi.LIGHT_CLOSE);
            sendDeviceEvent(NetApi.RIGHT_LIGHT, NetApi.LIGHT_CLOSE);
        } else if (content.contains("退房")
                || content.contains("check out")) {
            checkOut = true;
            text = getString(R.string.answer_checkout);
            chooseFragment(Constant.FRAGMENT_CHECK_OUT);
        } else if ((content.contains("确认") || content.contains("确定")
                || content.contains("yes"))
                && checkOut) {
            text = getString(R.string.answer_pay_goods);
            chooseFragment(Constant.FRAGMENT_PAY_GOODS);
        } else if ((content.contains("没有") || content.contains("no")) && checkOut) {
            text = getString(R.string.answer_unsubscribe);
            chooseFragment(Constant.FRAGMENT_UNSUBSCRIBE);
        } else if ((content.contains("一分")
                || content.contains("二分")
                || content.contains("两分")
                || content.contains("三分")
                || content.contains("四分")
                || content.contains("五分")
                || content.contains("scale")
                || content.contains("one")
                || content.contains("two")
                || content.contains("three")
                || content.contains("four")
                || content.contains("five")) && checkOut) {
            text = getString(R.string.answer_unsubscribe_end);
            checkOut = false;
            //            chooseFragment(Constant.FRAGMENT_MAIN);
        } else if (content.contains("谢谢") || content.contains("thank you")) {
            text = getString(R.string.answer_thank_you);
            chooseFragment(Constant.FRAGMENT_MAIN);
        } else if (content.contains("控制") || content.contains("control")) {
            text = getString(R.string.answer_controller);
            chooseFragment(Constant.FRAGMENT_JKDEMO);
        } else if (content.contains("床头灯") && content.contains("打开")) {
            text = "已经为你打开床头灯";
            sendDeviceEvent(NetApi.TIAO_GUANG_SWITCH, 100);
        } else if (content.contains("床头灯") && content.contains("关闭")) {
            text = "已经为你关闭床头灯";
            sendDeviceEvent(NetApi.TIAO_GUANG_SWITCH, -100);
        } else if (content.contains("床头灯") && content.contains("高")) {
            text = "已经为你调高床头灯";
            sendDeviceEvent(NetApi.TIAO_GUANG_SWITCH, 10);
        } else if (content.contains("床头灯") && content.contains("低")) {
            text = "已经为你调低床头灯";
            sendDeviceEvent(NetApi.TIAO_GUANG_SWITCH, -10);
        } else if (content.contains("卫生间") && content.contains("打开")) {
            text = "已经为你打开卫生间灯";
            sendDeviceEvent(NetApi.LEFT_LIGHT, NetApi.LIGHT_OPEN);
        } else if (content.contains("卫生间") && content.contains("关闭")) {
            text = "已经为你关闭卫生间灯";
            sendDeviceEvent(NetApi.LEFT_LIGHT, NetApi.LIGHT_CLOSE);
        } else if (content.contains("走廊") && content.contains("打开")) {
            text = "已经为你打开走廊灯";
            sendDeviceEvent(NetApi.RIGHT_LIGHT, NetApi.LIGHT_OPEN);
        } else if (content.contains("走廊") && content.contains("关闭")) {
            text = "已经为你关闭走廊灯";
            sendDeviceEvent(NetApi.RIGHT_LIGHT, NetApi.LIGHT_CLOSE);
        } else if (content.contains("插座") && content.contains("打开")) {
            text = "已经为你打开插座";
            sendDeviceEvent(NetApi.BORD, NetApi.SWITCH_OPEN);
        } else if (content.contains("插座") && content.contains("关闭")) {
            text = "已经为你关闭插座";
            sendDeviceEvent(NetApi.BORD, NetApi.SWITCH_CLOSE);
        } else if (content.contains("打开所有灯")) {
            text = "已经为你打开所有灯";
            sendDeviceEvent(NetApi.TIAO_GUANG_SWITCH, 100);
            sendDeviceEvent(NetApi.LEFT_LIGHT, NetApi.LIGHT_OPEN);
            sendDeviceEvent(NetApi.RIGHT_LIGHT, NetApi.LIGHT_OPEN);
        } else if (content.contains("退出") && content.contains("控制")) {
            text = "已经退出控制中心";
            chooseFragment(Constant.FRAGMENT_MAIN);
        } else if (content.contains("退出") || content.contains("返回")
                || content.contains("cancel") || content.contains("back")) {
            checkOut = false;
            text = getString(R.string.answer_okay);
            chooseFragment(Constant.FRAGMENT_MAIN);
        } else {
            if (checkOut) {
                text = getString(R.string.answer_error);
            } else {
                text = getString(R.string.answer_error);
                chooseFragment(Constant.FRAGMENT_MAIN);
            }
        }
        if (!TextUtils.isEmpty(text)) {
            startSpeak(text);
        }
        if (!TextUtils.isEmpty(content)) {
            showChat(content);
        }
        lottieAnimationView.cancelAnimation();
    }

    private void sendDeviceEvent(int device, int state) {
        if (device == NetApi.TIAO_GUANG_SWITCH) {
            int tempState = NetApi.INSTANCE.getTiaoGuangSwitch() + state;
            if (tempState >= 100) {
                tempState = 100;
            } else if (tempState <= 0) {
                tempState = 0;
            }
            NetApi.INSTANCE.setTiaoGuangSwitch(tempState);
            state = tempState;
        }
        NetApi.INSTANCE.changeStatus(device, state);
        EventBus.getDefault().post(new DeviceEvent(device, state));
    }

    @Override
    public void sttFailure() {
        lottieAnimationView.cancelAnimation();
    }

    public void chooseFragment(String itemTitle) {
        chooseFragment(itemTitle, 0);
    }

    public void chooseFragment(String itemTitle, int status) {
        switch (itemTitle) {
            case Constant.FRAGMENT_WELCOME:
                if (welcomeFragment == null)
                    welcomeFragment = WelcomeFragment.newInstance();
                fragmentHelper.switchFragment(welcomeFragment);
                break;
            case Constant.FRAGMENT_MAIN:
                if (mainFragment == null)
                    mainFragment = MainFragment.newInstance();
                fragmentHelper.switchFragment(mainFragment);
                break;
            case Constant.FRAGMENT_MEAL:
                if (mealFragment == null)
                    mealFragment = MealFragment.newInstance();
                fragmentHelper.switchFragment(mealFragment);
                break;
            case Constant.FRAGMENT_CLEAN_DNDST:
                if (cleanAndDndstFragment == null) {
                    cleanAndDndstFragment = CleanAndDndstFragment.newInstance(status);
                } else {
                    cleanAndDndstFragment.setStatus(status);
                }
                fragmentHelper.switchFragment(cleanAndDndstFragment);
                break;
            case Constant.FRAGMENT_CHECK_OUT:
                if (checkOutFragment == null) {
                    checkOutFragment = CheckOutFragment.newInstance();
                }
                fragmentHelper.switchFragment(checkOutFragment);
                break;
            case Constant.FRAGMENT_WEATHER:
                if (weatherFragment == null)
                    weatherFragment = WeatherFragment.newInstance(mWeather, mTemperature, mWind, timestamp);
                fragmentHelper.switchFragment(weatherFragment);
                break;
            case Constant.FRAGMENT_UNSUBSCRIBE:
                if (unsubscribeFragment == null)
                    unsubscribeFragment = UnsubscribeFragment.newInstance();
                fragmentHelper.switchFragment(unsubscribeFragment);
                break;
            case Constant.FRAGMENT_PAY_GOODS:
                if (payGoodsFragment == null) {
                    payGoodsFragment = PayGoodsFragment.newInstance();
                }
                fragmentHelper.switchFragment(payGoodsFragment);
                break;
            case Constant.FRAGMENT_VIDEO:
                if (videoFragment == null) {
                    videoFragment = VideoFragment.newInstance();
                }
                fragmentHelper.switchFragment(videoFragment);
                break;
            case Constant.FRAGMENT_JKDEMO:
                if (jkDemoFragment == null) {
                    jkDemoFragment = new JkDemoFragment();
                }
                fragmentHelper.switchFragment(jkDemoFragment);
                break;
        }
    }

    @Override
    public void wakeupSuccess() {
        Utils.wakeUpAndUnlock(getApplicationContext());
        ivSleep.setVisibility(View.INVISIBLE);
        lottieAnimationView.playAnimation();
        cancelTimer();
    }

    @Override
    public void onCompleted() {
        if (isWelcome) {
            isWelcome = false;
        } else if (isSleep) {
            isSleep = false;
            Utils.screenOff(this);
        }
        hideChat();
        toMainFragment();
    }


    private boolean isWelcome = false;

    public void setWelcome(boolean isWelcome) {
        this.isWelcome = isWelcome;
    }

    private boolean isSleep = false;

    @Override
    public void showChat(String context) {
        tvToast.setText(context);
        tvToast.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideChat() {
        tvToast.postDelayed(new Runnable() {
            @Override
            public void run() {
                tvToast.setVisibility(View.INVISIBLE);
            }
        }, 2000);
    }

    private CountDownTimer countDownTimer;

    private void toMainFragment() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(300000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                //                Log.e(TAG, "millisUntilFinished " + millisUntilFinished);
                if (millisUntilFinished / 1000 < 1) {
                    chooseFragment(Constant.FRAGMENT_MAIN);
                }
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    private void cancelTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private String mWeatherInfo;
    private String mWeather;
    private String mWind;
    private String mTemperature;
    private long timestamp;

    private void queryWeather() {
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(5000, TimeUnit.MILLISECONDS).build();
        final Request request = new Request.Builder().get().url("http://v.juhe.cn/weather/index?format=2&cityname=南京&key=5fa34b41f79b752cf943d9c86399fbfc").build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("weather", "网络错误");
                mWeatherInfo = "南京天气,小雨,东北风4-5级,温度,4℃~8℃";
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        WeatherBean weather = (WeatherBean) JsonTools.stringToObject(response.body().string(), WeatherBean.class);
                        if (weather.getError_code() == 0 && TextUtils.equals(weather.getResultcode(), "200")) {
                            Log.e("weather", weather.getResult().getToday().getDressing_advice());
                            mWeatherInfo = "南京天气" + "," + weather.getResult().getFuture().get(0).getWeather() + ","
                                    + weather.getResult().getFuture().get(0).getWind() + ","
                                    + "温度" + ","
                                    + weather.getResult().getFuture().get(0).getTemperature();

                            mWeather = weather.getResult().getFuture().get(0).getWeather();
                            mWind = weather.getResult().getFuture().get(0).getWind();
                            mTemperature = weather.getResult().getFuture().get(0).getTemperature();
                            timestamp = stringToTimestamp(weather.getResult().getFuture().get(0).getDate());
                        } else {
                            mWeatherInfo = "南京天气,小雨,东北风4-5级,温度,4℃~8℃";
                            Log.e("weather", "网络错误");
                        }
                    }
                } else {
                    mWeatherInfo = "南京天气,小雨,东北风4-5级,温度,4℃~8℃";
                    Log.e("weather", "网络错误");
                }
                if (response.body() != null) {
                    response.body().close();
                }

            }
        });
    }

    private long stringToTimestamp(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try {
            return sdf.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis();
    }
}
