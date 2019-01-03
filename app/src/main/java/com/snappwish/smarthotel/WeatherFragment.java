package com.snappwish.smarthotel;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.snappwish.smarthotel.base.MyBaseFragment;
import com.snappwish.smarthotel.util.DateUtils;

import butterknife.BindView;

/**
 * @author lishibo
 * @date 2018/12/25
 * email : andy_li@swift365.com.cn
 */
public class WeatherFragment extends MyBaseFragment {
    private static final String WEATHER = "weather";
    private static final String TEMPERATURE = "temperature";
    private static final String WIND = "wind";
    private static final String TIMESTAMP = "timestamp";
    @BindView(R.id.tv_temperature)
    TextView tvTemperature;
    @BindView(R.id.tv_weather)
    TextView tvWeather;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.iv_weather)
    ImageView ivWeather;
    @BindView(R.id.tv_windy)
    TextView tvWindy;

    public static WeatherFragment newInstance(String weather, String temperature, String wind, long timestamp) {
        Bundle args = new Bundle();
        args.putString(WEATHER, weather);
        args.putString(TEMPERATURE, temperature);
        args.putString(WIND, wind);
        args.putLong(TIMESTAMP, timestamp);
        WeatherFragment fragment = new WeatherFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_weather;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        tvDate.setText(DateUtils.nowTimeDetail2plus(getResources().getConfiguration().locale.getLanguage()));
        if (getResources().getConfiguration().locale.getLanguage().contains("en")) {
            return;
        }
        if (getArguments() == null) {
            return;
        }
        String temperature = getArguments().getString(TEMPERATURE);
        String wind = getArguments().getString(WIND);
        String weather = getArguments().getString(WEATHER);
        long timestamp = getArguments().getLong(TIMESTAMP);
        ivWeather.setImageResource(getWeatherIcon(weather));
        tvWeather.setText(weather);
        tvWindy.setText(wind);
        tvTemperature.setText(TextUtils.isEmpty(temperature) ? "-1~6" : temperature.replace("℃", ""));
    }

    @Override
    protected void destroyData() {

    }

    public int getWeatherIcon(String weather) {
        if (TextUtils.isEmpty(weather)) {
            return R.drawable.icon_sunny;
        }
        if (weather.contains("雪")) {
            return R.drawable.icon_snow;
        } else if (weather.contains("晴")) {
            return R.drawable.icon_sunny;
        } else if (weather.contains("阴")) {
            return R.drawable.icon_overcast;
        } else if (weather.contains("多云")) {
            return R.drawable.icon_cloudy;
        } else if (weather.contains("雨")) {
            return R.drawable.icon_rain;
        }

        return R.drawable.icon_sunny;
    }

}
