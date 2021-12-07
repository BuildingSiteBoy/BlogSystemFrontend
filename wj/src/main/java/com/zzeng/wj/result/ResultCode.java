package com.zzeng.wj.result;

/**
 * 返回结果的枚举
 * */
public enum ResultCode {
    SUCCESS(200),               //成功
    FAIL(400),                  //请求错误
    UNAUTHORIZED(401),          //请求授权失败
    NOT_FOUND(404),             //未找到路径/查询/文件等
    INTERNAL_SERVER_ERROR(500); //服务器产生内部错误

    public int code;

    ResultCode(int code) {
        this.code = code;
    }
}
