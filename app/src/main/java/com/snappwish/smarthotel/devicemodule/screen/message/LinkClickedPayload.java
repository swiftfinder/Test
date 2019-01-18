/*
 * Copyright (c) 2017 Baidu, Inc. All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.snappwish.smarthotel.devicemodule.screen.message;

import android.os.Parcel;

import com.baidu.duer.dcs.util.message.Payload;

/**
 * LinkClicked事件对应的payload结构
 * <p>
 * Created by wuruisheng on 2017/6/5.
 */
public class LinkClickedPayload extends Payload {
    public String url;
    public String token;

    public LinkClickedPayload() {

    }
    public LinkClickedPayload(String url, String token) {
        this.url = url;
        this.token = token;
    }

    protected LinkClickedPayload(Parcel in) {
        super(in);
        url = in.readString();
        token = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(url);
        dest.writeString(token);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LinkClickedPayload> CREATOR = new Creator<LinkClickedPayload>() {
        @Override
        public LinkClickedPayload createFromParcel(Parcel in) {
            return new LinkClickedPayload(in);
        }

        @Override
        public LinkClickedPayload[] newArray(int size) {
            return new LinkClickedPayload[size];
        }
    };


}