package com.snappwish.smarthotel;

import android.Manifest;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.TextView;

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
    private static final int PERMISSION_RECORD = 100;


    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void initView() {
        RobotManager.getInstance().initSTTEngine(this);
        RobotManager.getInstance().initTTSEngine(this);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void destroyData() {

    }

    @OnClick(R.id.btn_speaker)
    public void onSTTClick() {
        PermissionHelper.with(this)
                .requestCode(PERMISSION_RECORD)
                .permissions(Manifest.permission.RECORD_AUDIO)
                .request();

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
    public void SttSuccess(String content) {
        tvContent.setText(content);
        RobotManager.getInstance().setVoiceState(true);
        RobotManager.getInstance().setVoiceType("xiaoyan");
        RobotManager.getInstance().startSpeaking(content);
    }

    @Override
    public void SttFailed() {

    }
}
