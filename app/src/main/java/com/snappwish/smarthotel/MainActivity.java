package com.snappwish.smarthotel;

import android.Manifest;
import android.support.annotation.NonNull;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.snappwish.base_core.basemvp.FragmentManagerHelper;
import com.snappwish.base_core.permission.PermissionFailure;
import com.snappwish.base_core.permission.PermissionHelper;
import com.snappwish.base_core.permission.PermissionSuccess;
import com.snappwish.smarthotel.base.MyBaseActivity;
import com.snappwish.smarthotel.speech.RobotManager;
import com.snappwish.smarthotel.speech.STTListener;
import com.snappwish.smarthotel.speech.TTSEngine;
import com.snappwish.smarthotel.speech.WakeupListener;
import com.snappwish.smarthotel.speech.WakeupManager;

import butterknife.BindView;

/**
 * Created by jinjin on 2018/12/22.
 * description:
 */

public class MainActivity extends MyBaseActivity implements STTListener, WakeupListener, TTSEngine.TtsListener {

    private static final String TAG = "MainActivity";
    @BindView(R.id.fragment)
    FrameLayout fragment;
    @BindView(R.id.rl_root)
    RelativeLayout rlRoot;
    @BindView(R.id.lottie_view)
    LottieAnimationView lottieAnimationView;

    private static final int PERMISSION_RECORD = 100;


    private FragmentManagerHelper fragmentHelper;
    private WelcomeFragment welcomeFragment;
    private MainFragment mainFragment;
    private MealFragment mealFragment;
    private CleanAndDndstFragment cleanAndDndstFragment;
    private LightOutFragment lightOutFragment;
    private CheckOutFragment checkOutFragment;
    private WeatherFragment weatherFragment;
    private UnsubscribeFragment unsubscribeFragment;
    private PayGoodsFragment payGoodsFragment;
    private VideoFragment videoFragment;


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
    }

    @Override
    protected void initData() {
        PermissionHelper.with(this)
                .requestCode(PERMISSION_RECORD)
                .permissions(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request();

        RobotManager.getInstance().initSTTEngine(this, false);
        RobotManager.getInstance().initTTSEngine(this, this);
        chooseFragment(Constant.FRAGMENT_MAIN);
    }

    @Override
    protected void destroyData() {

    }

    @PermissionSuccess(requestCode = PERMISSION_RECORD)
    private void callSuccess() {
        WakeupManager.getInstance(this).startWakeup(this);
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
    public void sttSuccess(String content) {
        if (content.contains("天气")) {
            RobotManager.getInstance().startSpeaking("明天南京天气晴朗，有微风，温度2~9摄氏度");
            chooseFragment(Constant.FRAGMENT_WEATHER);
        } else if (content.contains("早餐") || content.contains("吃")) {
            RobotManager.getInstance().startSpeaking(getString(R.string.answer_meal));
            chooseFragment(Constant.FRAGMENT_MEAL);
        } else if (content.contains("新闻")) {
            RobotManager.getInstance().startSpeaking(getString(R.string.answer_video));
            chooseFragment(Constant.FRAGMENT_VIDEO);
        } else if (content.contains("打扫") || content.contains("卫生")) {
            RobotManager.getInstance().startSpeaking(getString(R.string.answer_clean_hotel));
            chooseFragment(Constant.FRAGMENT_CLEAN_DNDST, 1);
        } else if (content.contains("请勿打扰")) {
            RobotManager.getInstance().startSpeaking(getString(R.string.answer_not_dndst));
            chooseFragment(Constant.FRAGMENT_CLEAN_DNDST, 2);
        } else if (content.contains("关灯")) {
            RobotManager.getInstance().startSpeaking(getString(R.string.ansewer_good_night));
            chooseFragment(Constant.FRAGMENT_LIGHT_OUT);
        } else if (content.contains("退房")) {
            checkOut = true;
            RobotManager.getInstance().startSpeaking(getString(R.string.answer_checkout));
            chooseFragment(Constant.FRAGMENT_CHECK_OUT);
        } else if (content.contains("确认") && checkOut) {
            RobotManager.getInstance().startSpeaking(getString(R.string.answer_pay_goods));
            chooseFragment(Constant.FRAGMENT_PAY_GOODS);
        } else if (content.contains("没有") && checkOut) {
            RobotManager.getInstance().startSpeaking(getString(R.string.answer_unsubscribe));
            chooseFragment(Constant.FRAGMENT_UNSUBSCRIBE);
        } else if ((content.contains("一分")
                || content.contains("二分")
                || content.contains("两分")
                || content.contains("三分")
                || content.contains("四分")
                || content.contains("五分")) && checkOut) {
            RobotManager.getInstance().startSpeaking(getString(R.string.answer_unsubscribe_end));
            checkOut = false;
            chooseFragment(Constant.FRAGMENT_MAIN);
        } else if (content.contains("谢谢")) {
            RobotManager.getInstance().startSpeaking("不客气");
        } else {
            RobotManager.getInstance().startSpeaking(getString(R.string.answer_error));
        }
        WakeupManager.getInstance(this).startWakeup(this);
        lottieAnimationView.cancelAnimation();
    }

    @Override
    public void sttFailed(String errorMsg) {
        lottieAnimationView.cancelAnimation();
        WakeupManager.getInstance(this).startWakeup(this);
    }

    public void startSpeak(String content) {
        RobotManager.getInstance().startSpeaking(content);
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
                if (cleanAndDndstFragment == null)
                    cleanAndDndstFragment = CleanAndDndstFragment.newInstance(status);
                fragmentHelper.switchFragment(cleanAndDndstFragment);
                break;
            case Constant.FRAGMENT_LIGHT_OUT:
                if (lightOutFragment == null)
                    lightOutFragment = LightOutFragment.newInstance();
                fragmentHelper.switchFragment(lightOutFragment);
                break;
            case Constant.FRAGMENT_CHECK_OUT:
                if (checkOutFragment == null) {
                    checkOutFragment = CheckOutFragment.newInstance();
                }
                fragmentHelper.switchFragment(checkOutFragment);
                break;
            case Constant.FRAGMENT_WEATHER:
                if (weatherFragment == null) weatherFragment = WeatherFragment.newInstance();
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

        }
    }

    @Override
    public void wakeupSuccess() {
        lottieAnimationView.playAnimation();
        WakeupManager.getInstance(this).cancelWakeup();
        RobotManager.getInstance().setVoiceState(true);
        RobotManager.getInstance().setVoiceType("xiaoyan");
        RobotManager.getInstance().startRecognizing(this);
    }

    @Override
    public void wakeupFailed(String failedMsg) {

    }

    @Override
    public void onCompleted() {
        if (isWelcome) {
            isWelcome = false;
            WakeupManager.getInstance(this).startWakeup(this);
        }
    }


    private boolean isWelcome = false;

    public void setWelcome(boolean isWelcome) {
        this.isWelcome = isWelcome;
    }

}
