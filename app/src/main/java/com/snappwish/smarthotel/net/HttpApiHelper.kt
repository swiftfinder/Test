package com.snappwish.smarthotel.net

import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager
import java.net.CookiePolicy.ACCEPT_ORIGINAL_SERVER
import java.util.concurrent.TimeUnit


/**
 * @author lishibo
 * @date 2019/1/12
 * email : andy_li@swift365.com.cn
 */
class HttpApiHelper {
    companion object {
        private const val BASE_URL: String = "http://47.101.51.107:8081"
        private fun create(url: String): Retrofit {
            val level: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY

            val logInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
                Log.d("JKDEMO:", message)
            })
            logInterceptor.level = level

            val cookieHandler = CookieManager(null, ACCEPT_ORIGINAL_SERVER)

            val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
            okHttpClientBuilder.addNetworkInterceptor(logInterceptor)
            okHttpClientBuilder.cookieJar(JavaNetCookieJar(cookieHandler))

            okHttpClientBuilder.connectTimeout(30, TimeUnit.SECONDS)
            okHttpClientBuilder.readTimeout(30, TimeUnit.SECONDS)


            val gson = GsonBuilder()
                    .setLenient()
                    .create()

            return Retrofit.Builder()
                    .baseUrl(url)
                    .client(okHttpClientBuilder.build())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()

        }

        val apiService: ApiService = HttpApiHelper.getApiService(BASE_URL, ApiService::class.java)
        private fun <T> getApiService(url: String, service: Class<T>): T {
            return create(url).create(service)
        }
    }
}