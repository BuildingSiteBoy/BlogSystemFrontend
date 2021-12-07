package com.zzeng.wj.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class Result {
    //响应码
    private int code;
    private String message;
    private Object result;

    public Result(int code) {
        this.code = code;
    }
}
