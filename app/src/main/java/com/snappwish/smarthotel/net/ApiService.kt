package com.snappwish.smarthotel.net

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 * @author lishibo
 * @date 2019/1/12
 * email : andy_li@swift365.com.cn
 */
interface ApiService {
    /**
     * login
     */
    @GET("/login")
    fun login(): Observable<LoginResponse>

    @GET("/device")
    fun changeStatus(@QueryMap map: Map<String, Int>): Observable<ChangeStatusResponse>

}