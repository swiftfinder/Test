package com.snappwish.smarthotel.net

/**
 * @author lishibo
 * @date 2019/1/12
 * email : andy_li@swift365.com.cn
 */
data class LoginResponse(
        val message: String,
        val result: Result,
        val status: String
)

data class Result(
        val accessToken: String,
        val authorizationUserCode: String,
        val phoneType: Any,
        val userAddr: Any,
        val userAge: Any,
        val userCode: String,
        val userEmail: Any,
        val userId: Int,
        val userName: String,
        val userPhone: Any,
        val userPwd: String,
        val userSex: Any
)

data class ChangeStatusResponse(
    val message: String,
    val result: Any,
    val status: String
)