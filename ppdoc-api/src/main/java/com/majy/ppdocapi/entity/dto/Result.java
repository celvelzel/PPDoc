package com.majy.ppdocapi.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T>
{

    private int code;
    private String msg;
    private T data;

    public static Result createSuccess()
    {
        return new Result(200, "create success", null);
    }

    public static Result createFailure()
    {
        return new Result(400, "create failure", null);
    }

    public static Result deleteSuccess()
    {
        return new Result(204, "delete success", null);
    }

    public static Result deleteFailure()
    {
        return new Result(404, "delete failure");
    }

    public static Result updateSuccess()
    {
        return new Result(200, "update success");
    }

    public static <T> Result<T> updateSuccess(T Data)
    {
        return new Result(200, "update success", Data);
    }

    public static Result updateFailure()
    {
        return new Result(400, "update failure");
    }

    public static <T> Result<T> selectSuccess(T Data)
    {
        return new Result(200, "select success", Data);
    }

    public static Result selectFailure()
    {
        return new Result(500, "select failure");
    }

    public Result(T data)
    {
        this.code = 200;
        this.msg = "success";
        this.data = data;
    }

    public Result(T data, boolean success, String msg)
    {
        if (success)
        {
            this.code = 200;
            this.msg = "success";
        }
        else
        {
            this.code = 500;
            this.msg = msg;
        }
        this.data = data;
    }

    public Result(int code, String msg)
    {
        this.code = code;
        this.msg = msg;
        this.data = null;
    }

    public static <T> Result<T> success(T data)
    {
        return new Result<>(data);
    }

    public static <T> Result<T> fail(String message)
    {
        return new Result<>(500, message);
    }

    public static <T> Result<T> fail(int code, String message)
    {
        return new Result<>(code, message);
    }

    /**
     * 构建并返回一个成功结果的响应体。
     *
     * @param urlResult 表示结果中的URL对象。
     * @param dataMap   包含结果数据的键值对映射。
     * @return 返回一个封装了结果数据的成功响应对象。
     */
    public static Result getSuccessResult(URL urlResult, Map<String, String> dataMap)
    {
        // 构建响应结果的映射
        Map<String, Object> responseMap = new HashMap();
        responseMap.put("url", urlResult); // 将URL对象放入响应映射中
        responseMap.put("data", dataMap); // 将数据映射放入响应映射中
        // 返回一个表示成功的Result对象，其中包含了构建的响应映射
        return Result.success(responseMap);
    }

}


