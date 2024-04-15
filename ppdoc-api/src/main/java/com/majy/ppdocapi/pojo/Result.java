package com.majy.ppdocapi.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    private int code;
    private String msg;
    private T data;

    public static Result success(){
        return new Result(200,"success",null);
    }

    public Result(T data) {
        this.code = 200;
        this.msg = "success";
        this.data = data;
    }

    public Result(T data, boolean success, String msg) {
        if (success) {
            this.code = 200;
            this.msg = "success";
        } else {
            this.code = 500;
            this.msg = msg;
        }
        this.data = data;
    }

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.data = null;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(data);
    }

    public static <T> Result<T> fail(String message) {
        return new Result<>(500, message);
    }

    public static <T> Result<T> fail(int code, String message) {
        return new Result<>(code, message);
    }
}


