package com.snappwish.smarthotel.devicemodule.screen.extend.card.message;

import android.os.Parcel;
import android.os.Parcelable;

import com.snappwish.smarthotel.devicemodule.screen.TokenPayload;


public class RenderDatePayload extends TokenPayload {
    public String datetime;
    public String timeZoneName;
    public String day;

    public RenderDatePayload() {

    }

    protected RenderDatePayload(Parcel in) {
        super(in);
        datetime = in.readString();
        timeZoneName = in.readString();
        day = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(datetime);
        dest.writeString(timeZoneName);
        dest.writeString(day);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<RenderDatePayload> CREATOR = new Parcelable.Creator<RenderDatePayload>() {
        @Override
        public RenderDatePayload createFromParcel(Parcel in) {
            return new RenderDatePayload(in);
        }

        @Override
        public RenderDatePayload[] newArray(int size) {
            return new RenderDatePayload[size];
        }
    };
}
