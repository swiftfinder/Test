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
import com.snappwish.smarthotel.speech.WakeupListener;
import com.snappwish.smarthotel.speech.WakeupManager;

import butterknife.BindView;

/**
 * Created by jinjin on 2018/12/22.
 * description:
 */

public class MainActivity extends MyBaseActivity implements STTListener, WakeupListener {

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
                .permissions(Manifest.permission.RECORD_AUDIO)
                .request();

        RobotManager.getInstance().initSTTEngine(this);
        chooseFragment(Constant.FRAGMENT_MAIN);

    }

    @Override
    protected void destroyData() {

    }

    public void onSTTClick() {

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


    @Override
    public void sttSuccess(String content) {
        RobotManager.getInstance().startSpeaking(content);
    }

    @Override
    public void sttFailed() {

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
        }
    }

    @Override
    public void wakeupSuccess() {
        lottieAnimationView.playAnimation();
        RobotManager.getInstance().setVoiceState(true);
        RobotManager.getInstance().setVoiceType("xiaoyan");
        RobotManager.getInstance().startRecognizing(new STTListener() {
            @Override
            public void sttSuccess(String content) {
                if (content.contains("天气")) {
                    RobotManager.getInstance().startSpeaking("明天南京天气很好，很晴朗");
                    chooseFragment(Constant.FRAGMENT_WEATHER);
                } else if (content.contains("早餐") || content.contains("吃")) {
                    RobotManager.getInstance().startSpeaking("好的，已为您找到餐厅位置");
                    chooseFragment(Constant.FRAGMENT_MEAL);
                } else if (content.contains("新闻")) {
//                    chooseFragment();
                } else if (content.contains("打扫") || content.contains("卫生")) {
                    chooseFragment(Constant.FRAGMENT_CLEAN_DNDST, 1);
                } else if (content.contains("请勿打扰")) {
                    chooseFragment(Constant.FRAGMENT_CLEAN_DNDST, 2);
                } else if (content.contains("关灯")) {
                    chooseFragment(Constant.FRAGMENT_LIGHT_OUT);
                } else if (content.contains("退房")) {
                    chooseFragment(Constant.FRAGMENT_CHECK_OUT);

                }
            }

            @Override
            public void sttFailed() {

            }
        });
    }

    @Override
    public void wakeupFailed(String failedMsg) {

    }
}
