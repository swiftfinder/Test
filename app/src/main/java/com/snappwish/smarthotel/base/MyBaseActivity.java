package com.snappwish.smarthotel.base;

import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import com.baidu.duer.dcs.api.DcsSdkBuilder;
import com.baidu.duer.dcs.api.IConnectionStatusListener;
import com.baidu.duer.dcs.api.IDcsSdk;
import com.baidu.duer.dcs.api.IDialogStateListener;
import com.baidu.duer.dcs.api.IDirectiveIntercepter;
import com.baidu.duer.dcs.api.IFinishedDirectiveListener;
import com.baidu.duer.dcs.api.IMessageSender;
import com.baidu.duer.dcs.api.IVoiceRequestListener;
import com.baidu.duer.dcs.api.config.DcsConfig;
import com.baidu.duer.dcs.api.config.DefaultSdkConfigProvider;
import com.baidu.duer.dcs.api.config.SdkConfigProvider;
import com.baidu.duer.dcs.api.player.ITTSPositionInfoListener;
import com.baidu.duer.dcs.api.recorder.AudioRecordImpl;
import com.baidu.duer.dcs.api.recorder.BaseAudioRecorder;
import com.baidu.duer.dcs.api.wakeup.BaseWakeup;
import com.baidu.duer.dcs.api.wakeup.IWakeupAgent;
import com.baidu.duer.dcs.api.wakeup.IWakeupProvider;
import com.baidu.duer.dcs.componentapi.AbsDcsClient;
import com.baidu.duer.dcs.devicemodule.custominteraction.CustomUserInteractionDeviceModule;
import com.baidu.duer.dcs.framework.DcsSdkImpl;
import com.baidu.duer.dcs.framework.ILoginListener;
import com.baidu.duer.dcs.framework.InternalApi;
import com.baidu.duer.dcs.framework.internalapi.IDirectiveReceivedListener;
import com.baidu.duer.dcs.framework.internalapi.IErrorListener;
import com.baidu.duer.dcs.oauth.api.code.OauthCodeImpl;
import com.baidu.duer.dcs.systeminterface.IOauth;
import com.baidu.duer.dcs.tts.TtsImpl;
import com.baidu.duer.dcs.util.DcsErrorCode;
import com.baidu.duer.dcs.util.HttpProxy;
import com.baidu.duer.dcs.util.api.IDcsRequestBodySentListener;
import com.baidu.duer.dcs.util.dispatcher.DialogRequestIdHandler;
import com.baidu.duer.dcs.util.message.DcsRequestBody;
import com.baidu.duer.dcs.util.message.Directive;
import com.baidu.duer.dcs.util.message.Payload;
import com.baidu.duer.dcs.util.util.LogUtil;
import com.baidu.duer.dcs.util.util.StandbyDeviceIdUtil;
import com.baidu.duer.kitt.KittWakeUpServiceImpl;
import com.baidu.duer.kitt.WakeUpConfig;
import com.baidu.duer.kitt.WakeUpWord;
import com.snappwish.base_core.basemvp.BaseActivity;
import com.snappwish.base_core.dialog.AlertDialog;
import com.snappwish.smarthotel.BuildConfig;
import com.snappwish.smarthotel.devicemodule.screen.ScreenDeviceModule;
import com.snappwish.smarthotel.devicemodule.screen.extend.card.ScreenExtendDeviceModule;
import com.snappwish.smarthotel.devicemodule.screen.extend.card.message.RenderAudioListPlayload;
import com.snappwish.smarthotel.devicemodule.screen.extend.card.message.RenderPlayerInfoPayload;
import com.snappwish.smarthotel.devicemodule.screen.message.HtmlPayload;
import com.snappwish.smarthotel.devicemodule.screen.message.RenderCardPayload;
import com.snappwish.smarthotel.devicemodule.screen.message.RenderHintPayload;
import com.snappwish.smarthotel.devicemodule.screen.message.RenderVoiceInputTextPayload;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinjin on 2018/12/22.
 * description:
 */

public abstract class MyBaseActivity extends BaseActivity {

    public static final String TAG = "DCS-SDK";
    // demo使用的CLIENT_ID，正式产品请用自己申请的CLIENT_ID、PID
    public static final String CLIENT_ID = BuildConfig.CLIENT_ID;
    public static final int PID = BuildConfig.PID;
    public static final String APP_KEY = BuildConfig.APP_KEY;
    // 唤醒配置
    // 格式必须为：浮点数，用','分隔，每个模型对应3个灵敏度
    // 例如有2个模型,就需要6个灵敏度，0.35,0.35,0.40,0.45,0.45,0.55
    private static final String WAKEUP_RES_PATH = "snowboy/common.res";
    private static final String WAKEUP_UMDL_PATH = "snowboy/xiaoduxiaodu_all_11272017.umdl";
    private static final String WAKEUP_SENSITIVITY = "0.35,0.35,0.40";
    private static final String WAKEUP_HIGH_SENSITIVITY = "0.45,0.45,0.55";
    // 唤醒成功后是否需要播放提示音
    private static final boolean ENABLE_PLAY_WARNING = true;
    private static final int REQUEST_CODE = 123;
    private IDialogStateListener.DialogState currentDialogState = IDialogStateListener.DialogState.IDLE;
    private IDcsSdk dcsSdk;
    private ScreenDeviceModule screenDeviceModule;
    private IDialogStateListener dialogStateListener;

    @Override
    protected AlertDialog getLoadingDialog() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this instanceof IEventBus) {
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
        }
        initSdk();
        sdkRun();
        initListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this instanceof IEventBus) {
            if (EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().unregister(this);
            }
        }

        if (screenDeviceModule != null) {
            screenDeviceModule.removeScreenListener(screenListener);
        }
        screenListener = null;

        dcsSdk.getVoiceRequest().removeDialogStateListener(dialogStateListener);
        dialogStateListener = null;

        dcsSdk.removeConnectionStatusListener(connectionStatusListener);
        connectionStatusListener = null;

        getInternalApi().removeErrorListener(errorListener);
        errorListener = null;

        getInternalApi().removeRequestBodySentListener(dcsRequestBodySentListener);
        dcsRequestBodySentListener = null;

        // 第3步，释放sdk
        dcsSdk.release();
    }


    protected void initListener() {
        // 设置各种监听器
        dcsSdk.addConnectionStatusListener(connectionStatusListener);
        // 错误
        getInternalApi().addErrorListener(errorListener);
        // event发送
        getInternalApi().addRequestBodySentListener(dcsRequestBodySentListener);
        // 对话状态
        initDialogStateListener();
        // 语音文本同步
        initTTSPositionInfoListener();
        // 唤醒
        initWakeUpAgentListener();
        // 所有指令透传，建议在各自的DeviceModule中处理
        addDirectiveReceivedListener();
        // 指令执行完毕回调
        initFinishedDirectiveListener();
        // 语音音量回调监听
        initVolumeListener();
        initVoiceErrorListener();
        initDirectiveIntercepter();
    }

    private ScreenDeviceModule.IScreenListener screenListener = new ScreenDeviceModule.IScreenListener() {
        @Override
        public void onRenderVoiceInputText(RenderVoiceInputTextPayload payload) {
            handleRenderVoiceInputTextPayload(payload);
        }

        @Override
        public void onHtmlPayload(HtmlPayload htmlPayload) {

        }

        @Override
        public void onRenderCard(RenderCardPayload renderCardPayload) {

        }

        @Override
        public void onRenderHint(RenderHintPayload renderHintPayload) {

        }
    };

    private IDcsRequestBodySentListener dcsRequestBodySentListener = new IDcsRequestBodySentListener() {

        @Override
        public void onDcsRequestBody(DcsRequestBody dcsRequestBody) {
            String eventName = dcsRequestBody.getEvent().getHeader().getName();
            Log.v(TAG, "eventName:" + eventName);
            //            if (eventName.equals("PlaybackStopped") || eventName.equals("PlaybackFinished")
            //                    || eventName.equals("PlaybackFailed")) {
            //                playButton.setText("等待音乐");
            //                isPlaying = false;
            //            } else if (eventName.equals("PlaybackPaused")) {
            //                playButton.setText("暂停中");
            //                isPlaying = false;
            //            } else if (eventName.equals("PlaybackStarted") || eventName.equals("PlaybackResumed")) {
            //                playButton.setText("播放中...");
            //                isPlaying = true;
            //            }
        }
    };
    private IErrorListener errorListener = new IErrorListener() {
        @Override
        public void onErrorCode(DcsErrorCode errorCode) {
            if (errorCode.error == DcsErrorCode.VOICE_REQUEST_EXCEPTION) {
                if (errorCode.subError == DcsErrorCode.NETWORK_UNAVAILABLE) {
                    Log.d(TAG, "网络不可用");
                } else {
                    Log.d(TAG, "识别失败，请稍后再试");
                }

            } else if (errorCode.error == DcsErrorCode.LOGIN_FAILED) {
                // 未登录
                Log.d(TAG, "未登录");
            } else if (errorCode.subError == DcsErrorCode.UNAUTHORIZED_REQUEST) {
                // 以下仅针对 passport 登陆情况下的账号刷新，非 passport 刷新请参看文档。
            }
        }
    };

    private IConnectionStatusListener connectionStatusListener = new IConnectionStatusListener() {
        @Override
        public void onConnectStatus(ConnectionStatus connectionStatus) {
            Log.d(TAG, "onConnectionStatusChange: " + connectionStatus);
        }
    };

    /**
     * tts文字同步
     */
    private void initTTSPositionInfoListener() {
        getInternalApi().addTTSPositionInfoListener(new ITTSPositionInfoListener() {
            @Override
            public void onPositionInfo(long pos, long playTimeMs, long mark) {
                Log.d(TAG, "pos : " + pos + " , playTimeMs : " + playTimeMs + " , mark : " + mark);
            }
        });
    }

    /**
     * 语音音量回调监听
     */
    private void initVolumeListener() {
        getInternalApi().getDcsClient().addVolumeListener(new AbsDcsClient.IVolumeListener() {
            @Override
            public void onVolume(int volume, int percent) {
                Log.d(TAG, "volume  ----->" + volume);
                Log.d(TAG, "percent ----->" + percent);
            }
        });
    }

    /**
     * 语音错误回调监听
     */
    private void initVoiceErrorListener() {
        getInternalApi().getDcsClient().addVoiceErrorListener(new AbsDcsClient.IVoiceErrorListener() {
            @Override
            public void onVoiceError(int error, int subError) {
                Log.d(TAG, "onVoiceError:" + error + " " + subError);
            }
        });
    }

    protected void initSdk() {
        // 第一步初始化sdk
        // BaseAudioRecorder audioRecorder = new PcmAudioRecorderImpl(); pcm 输入方式
        BaseAudioRecorder audioRecorder = new AudioRecordImpl();
        IOauth oauth = getOauth();
        // 唤醒单独开启唤醒进程；  如果不需要将唤醒放入一个单独进程，可以使用KittWakeUpImpl
        final BaseWakeup wakeup = new KittWakeUpServiceImpl(audioRecorder);
        // 百度语音团队的离线asr和百度语音团队的唤醒，2个so库冲突，暂时不要用WakeupImpl实现的唤醒功能！！
        //        final BaseWakeup wakeup = new WakeupImpl();
        final IWakeupProvider wakeupProvider = new IWakeupProvider() {
            @Override
            public WakeUpConfig wakeUpConfig() {
                // 添加多唤醒词和索引
                // 此处传入的index需要和Snowboy唤醒模型文件一致
                // 例：模型文件中有3个唤醒词，分别为不同语速的"小度小度"，index分别为1-3，则需要按照以下格式添加
                // 唤醒成功后，回调中会包含被唤醒的WakeUpWord
                List<WakeUpWord> wakeupWordList = new ArrayList<>();
                wakeupWordList.add(new WakeUpWord(1, "管家管家"));
                wakeupWordList.add(new WakeUpWord(2, "管家管家"));
                wakeupWordList.add(new WakeUpWord(3, "管家管家"));
                final List<String> umdlPaths = new ArrayList<>();
                umdlPaths.add(WAKEUP_UMDL_PATH);
                return new WakeUpConfig.Builder()
                        .resPath(WAKEUP_RES_PATH)
                        .umdlPath(umdlPaths)
                        .sensitivity(WAKEUP_SENSITIVITY)
                        .highSensitivity(WAKEUP_HIGH_SENSITIVITY)
                        .wakeUpWords(wakeupWordList)
                        .build();
            }

            @Override
            public boolean enableWarning() {
                return ENABLE_PLAY_WARNING;
            }

            @Override
            public String warningSource() {
                // 每次在播放唤醒提示音前调用该方法
                // assets目录下的以assets://开头
                // 文件为绝对路径
                return "assets://ding.wav";
            }

            @Override
            public float volume() {
                // 每次在播放唤醒提示音前调用该方法
                // [0-1]
                return 0.8f;
            }

            @Override
            public boolean wakeAlways() {
                return true;
            }

            @Override
            public BaseWakeup wakeupImpl() {
                return wakeup;
            }

            @Override
            public int audioType() {
                // 用户自定义类型
                return AudioManager.STREAM_MUSIC;
            }
        };


        // proxyIp 为代理IP
        // proxyPort  为代理port
        HttpProxy httpProxy = new HttpProxy("172.24.194.28", 8888);

        // SDK配置，ClientId、语音PID、代理等
        SdkConfigProvider sdkConfigProvider = getSdkConfigProvider();
        // 构造dcs sdk
        DcsSdkBuilder builder = new DcsSdkBuilder();
        dcsSdk = builder.withSdkConfig(sdkConfigProvider)
                .withWakeupProvider(wakeupProvider)
                .withOauth(oauth)
                .withAudioRecorder(audioRecorder)
                // 1.withDeviceId设置设备唯一ID
                // 2.强烈建议！！！！
                //   如果开发者清晰的知道自己设备的唯一id，可以按照自己的规则传入
                //   需要保证设置正确，保证唯一、刷机和升级后不变
                // 3.sdk提供的方法，但是不保证所有的设别都是唯一的
                //   StandbyDeviceIdUtil.getStandbyDeviceId()
                //   该方法的算法是MD5（android_id + imei + Mac地址）32位  +  32位UUID总共64位
                //   生成：首次按照上述算法生成ID，生成后依次存储apk内部->存储系统数据库->存储外部文件
                //   获取：存储apk内部->存储系统数据库->存储外部文件，都没有则重新生成
                .withDeviceId(StandbyDeviceIdUtil.getStandbyDeviceId())
                // 设置音乐播放器的实现，sdk 内部默认实现为MediaPlayerImpl
                // .withMediaPlayer(new MediaPlayerImpl(AudioManager.STREAM_MUSIC))
                .build();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                TtsImpl impl = getInternalApi().initLocalTts(getApplicationContext(), null, null,
                        BuildConfig.TTS_APIKEY,
                        BuildConfig.TTS_SERCERTKEY, BuildConfig.TTS_APPID, null);
                impl.setSpeaker(2);
                String textFile = getApplicationInfo().nativeLibraryDir + "/libbd_etts_text.dat.so";
                String speechMode = getApplicationInfo().nativeLibraryDir + "/" + TtsImpl.SPEECH_MODEL_NAME_GEZI;
                impl.loadSpeechModel(speechMode, textFile);
                getInternalApi().setVolume(0.8f);
            }
        }, 200);

        // 设置Oneshot
        getInternalApi().setSupportOneshot(true);
        // ！！！！临时配置需要在run之前设置！！！！
        // 临时配置开始
        // 暂时没有定的API接口，可以通过getInternalApi设置后使用
        // 设置唤醒参数后，初始化唤醒
        getInternalApi().initWakeUp();
        //        getInternalApi().setOnPlayingWakeUpSensitivity(WAKEUP_ON_PLAYING_SENSITIVITY);
        //        getInternalApi().setOnPlayingWakeUpHighSensitivity(WAKEUP_ON_PLAYING_HIGH_SENSITIVITY);
        getInternalApi().setAsrMode(DcsConfig.ASR_MODE_ONLINE);
        // 测试数据，具体bduss值
        // getInternalApi().setBDuss("");
        // 临时配置结束
        // dbp平台
        // getInternalApi().setDebugBot("f15be387-1348-b71b-2ae5-8f19f2375ea1");

        // 第二步：可以按需添加内置端能力和用户自定义端能力（需要继承BaseDeviceModule）
        // 屏幕展示
        IMessageSender messageSender = getInternalApi().getMessageSender();

        // 上屏
        screenDeviceModule = new ScreenDeviceModule(messageSender);
        screenDeviceModule.addScreenListener(screenListener);
        dcsSdk.putDeviceModule(screenDeviceModule);

        ScreenExtendDeviceModule screenExtendDeviceModule = new ScreenExtendDeviceModule(messageSender);
        screenExtendDeviceModule.addExtensionListener(mScreenExtensionListener);
        dcsSdk.putDeviceModule(screenExtendDeviceModule);

        // 在线返回文本的播报，eg:你好，返回你好的播报
        DialogRequestIdHandler dialogRequestIdHandler =
                ((DcsSdkImpl) dcsSdk).getProvider().getDialogRequestIdHandler();
        CustomUserInteractionDeviceModule customUserInteractionDeviceModule =
                new CustomUserInteractionDeviceModule(messageSender, dialogRequestIdHandler);
        dcsSdk.putDeviceModule(customUserInteractionDeviceModule);

        // 扩展自定义DeviceModule,eg...
        addOtherDeviceModule(dcsSdk, messageSender);
        // 获取设备列表
        // getInternalApi().getSmartHomeManager().getDeviceList(null, null);
    }


    protected void addOtherDeviceModule(IDcsSdk dcsSdk, IMessageSender messageSender) {

    }

    protected SdkConfigProvider getSdkConfigProvider() {
        return new DefaultSdkConfigProvider() {
            @Override
            public String clientId() {
                return CLIENT_ID;
            }

            @Override
            public int pid() {
                return PID;
            }

            @Override
            public String appKey() {
                return APP_KEY;
            }
        };
    }

    private String mRenderPlayerInfoToken = null;
    private String mPlayToken = null;
    private ScreenExtendDeviceModule.IScreenExtensionListener mScreenExtensionListener = new ScreenExtendDeviceModule
            .IScreenExtensionListener() {


        @Override
        public void onRenderPlayerInfo(RenderPlayerInfoPayload renderPlayerInfoPayload) {
            // handleRenderPlayerInfoPayload(renderPlayerInfoPayload);
        }

        @Override
        public void onRenderAudioList(RenderAudioListPlayload renderAudioListPlayload) {

        }
    };

    protected void sdkRun() {
        // 第三步，将sdk跑起来
        ((DcsSdkImpl) dcsSdk).getInternalApi().login(new ILoginListener() {
            @Override
            public void onSucceed(String accessToken) {
                dcsSdk.run(null);
                Log.d(TAG, "登录成功");
            }

            @Override
            public void onFailed(String errorMessage) {
                Log.e(TAG, "login onFailed. ");
                finish();
            }

            @Override
            public void onCancel() {
                Log.e(TAG, "login onCancel. ");
                finish();
            }
        });
    }

    public InternalApi getInternalApi() {
        return ((DcsSdkImpl) dcsSdk).getInternalApi();
    }

    private IWakeupAgent.IWakeupAgentListener wakeupAgentListener = new IWakeupAgent.SimpleWakeUpAgentListener() {
        @Override
        public void onWakeupSucceed(WakeUpWord wakeUpWord) {
            Log.d(TAG, "唤醒成功");
            wakeupSuccess();
        }

    };

    private void initWakeUpAgentListener() {
        IWakeupAgent wakeupAgent = getInternalApi().getWakeupAgent();
        if (wakeupAgent != null) {
            wakeupAgent.addWakeupAgentListener(wakeupAgentListener);
        }
    }

    private void beginVoiceRequest(final boolean vad) {
        // 必须先调用cancel
        dcsSdk.getVoiceRequest().cancelVoiceRequest(new IVoiceRequestListener() {
            @Override
            public void onSucceed() {
                dcsSdk.getVoiceRequest().beginVoiceRequest(vad);
            }
        });
    }

    private void initDialogStateListener() {
        // 添加会话状态监听
        dialogStateListener = new IDialogStateListener() {
            @Override
            public void onDialogStateChanged(final DialogState dialogState) {
                currentDialogState = dialogState;
                Log.d(TAG, "onDialogStateChanged: " + dialogState);
                switch (dialogState) {
                    case IDLE:
                        //                        voiceButton.setText(getResources().getString(R.string.stop_record));
                        break;
                    case LISTENING:
                        hideChat();
                        //                        voiceButton.setText(getResources().getString(R.string.start_record));
                        break;
                    case SPEAKING:
                        //                        voiceButton.setText(getResources().getString(R.string.speaking));
                        break;
                    case THINKING:
                        //                        voiceButton.setText(getResources().getString(R.string.think));
                        break;
                    default:
                        break;
                }
            }
        };
        dcsSdk.getVoiceRequest().addDialogStateListener(dialogStateListener);
    }

    private void addDirectiveReceivedListener() {
        getInternalApi().addDirectiveReceivedListener(new IDirectiveReceivedListener() {
            @Override
            public void onDirective(Directive directive) {
                if (directive == null) {
                    return;
                }
                if (directive.getName().equals("Play")) {
                    Payload mPayload = directive.getPayload();
                    if (mPayload instanceof com.baidu.duer.dcs.devicemodule.audioplayer.message.PlayPayload) {
                        com.baidu.duer.dcs.devicemodule.audioplayer.message.PlayPayload.Stream stream =
                                ((com.baidu.duer.dcs.devicemodule.audioplayer.message.PlayPayload) mPayload)
                                        .audioItem.stream;
                        if (stream != null) {
                            mPlayToken = ((com.baidu.duer.dcs.devicemodule.audioplayer.message.PlayPayload) mPayload)
                                    .audioItem.stream.token;
                            Log.i(TAG, "  directive mToken = " + mPlayToken);
                        }
                    }
                } else if (directive.getName().equals("RenderPlayerInfo")) {
                    Payload mPayload = directive.getPayload();
                    if (mPayload instanceof RenderPlayerInfoPayload) {
                        mRenderPlayerInfoToken = ((RenderPlayerInfoPayload) mPayload).getToken();
                    }
                }
            }
        });
    }

    private void initDirectiveIntercepter() {
        getInternalApi().setDirectiveIntercepter(new IDirectiveIntercepter() {
            @Override
            public boolean onInterceptDirective(Directive directive) {
                return false;
            }
        });
    }

    private void initFinishedDirectiveListener() {
        // 所有指令执行完毕的回调监听
        getInternalApi().addFinishedDirectiveListener(new IFinishedDirectiveListener() {
            @Override
            public void onFinishedDirective() {
                Log.d(TAG, "所有指令执行完毕");
                onCompleted();
            }
        });
    }

    private void handleRenderVoiceInputTextPayload(RenderVoiceInputTextPayload payload) {
        if (payload.type == RenderVoiceInputTextPayload.Type.FINAL) {
            Log.d(TAG, "chat -> " + payload.text);
            sttSuccess(payload.text);
        }
    }

    private void cancelVoiceRequest() {
        dcsSdk.getVoiceRequest().cancelVoiceRequest(new IVoiceRequestListener() {
            @Override
            public void onSucceed() {
                Log.d(TAG, "cancelVoiceRequest onSucceed");
            }
        });
    }

    protected IOauth getOauth() {
        return new OauthCodeImpl(CLIENT_ID, this);
    }

    public abstract void showChat(String context);

    public abstract void hideChat();

    public void startSpeak(String content) {
        getInternalApi().speakRequest(content);
    }

    public abstract void sttSuccess(String speaker);

    public abstract void sttFailure();

    public abstract void onCompleted();

    public abstract void wakeupSuccess();
}
