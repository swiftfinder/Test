package com.snappwish.smarthotel;

import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.snappwish.base_core.basemvp.FragmentManagerHelper;
import com.snappwish.base_core.permission.PermissionFailure;
import com.snappwish.base_core.permission.PermissionHelper;
import com.snappwish.base_core.permission.PermissionSuccess;
import com.snappwish.smarthotel.base.MyBaseActivity;
import com.snappwish.smarthotel.speech.RobotManager;
import com.snappwish.smarthotel.speech.STTListener;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jinjin on 2018/12/22.
 * description:
 */

public class MainActivity extends MyBaseActivity implements STTListener {

    private static final String TAG = "MainActivity";
    @BindView(R.id.btn_speaker)
    Button btnSpeaker;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.fragment)
    FrameLayout fragment;

    private static final int PERMISSION_RECORD = 100;
    private FragmentManagerHelper fragmentHelper;
    private WelcomeFragment welcomeFragment;
    private MainFragment mainFragment;


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
        RobotManager.getInstance().initSTTEngine(this);
        chooseFragment(Constant.FRAGMENT_WELCOME);
    }

    @Override
    protected void destroyData() {

    }

    @OnClick(R.id.btn_speaker)
    public void onSTTClick() {
//        PermissionHelper.with(this)
//                .requestCode(PERMISSION_RECORD)
//                .permissions(Manifest.permission.RECORD_AUDIO)
//                .request();
        chooseFragment(Constant.FRAGMENT_MAIN);
    }


    @PermissionSuccess(requestCode = PERMISSION_RECORD)
    private void callSuccess() {
        RobotManager.getInstance().startRecognizing(this);
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
        tvContent.setText(content);
        RobotManager.getInstance().startSpeaking(content);
    }

    @Override
    public void sttFailed() {

    }

    private void chooseFragment(String itemTitle) {
        switch (itemTitle) {
            case Constant.FRAGMENT_WELCOME:
                if (welcomeFragment == null) welcomeFragment = WelcomeFragment.newInstance();
                fragmentHelper.switchFragment(welcomeFragment);
                break;
            case Constant.FRAGMENT_MAIN:
                if (mainFragment == null) mainFragment = MainFragment.newInstance();
                fragmentHelper.switchFragment(mainFragment);
                break;
        }
    }
}
