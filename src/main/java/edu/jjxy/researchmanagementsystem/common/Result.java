package edu.jjxy.researchmanagementsystem.common;

import lombok.Data;

@Data
public class Result<T> {
    private int code;
    private String msg;
    private T data;

    public static <T> Result<T> ok(T data) {
        Result<T> r = new Result<>();
        r.code = 0; r.msg = "ok"; r.data = data;
        return r;
    }
}
