package com.snappwish.smarthotel.net

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author lishibo
 * @date 2019/1/12
 * email : andy_li@swift365.com.cn
 */
class HttpApiHelper {
    companion object {
        private const val URL: String = "http://192.168.0.113:8080/"
        private fun create(url: String): Retrofit {
            val level: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY

            val logInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
                Log.e("JKDEMO:", message)
            })
            logInterceptor.level = level
            val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
            okHttpClientBuilder.addNetworkInterceptor(logInterceptor)

            okHttpClientBuilder.connectTimeout(30, TimeUnit.SECONDS)
            okHttpClientBuilder.readTimeout(30, TimeUnit.SECONDS)

            return Retrofit.Builder()
                    .baseUrl(url)
                    .client(okHttpClientBuilder.build())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

        }

        val apiService: ApiService = HttpApiHelper.getApiService(URL, ApiService::class.java)
        private fun <T> getApiService(url: String, service: Class<T>): T {
            return create(url).create(service)
        }
    }
}