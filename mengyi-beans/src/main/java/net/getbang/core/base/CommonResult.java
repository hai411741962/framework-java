//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package net.getbang.core.base;

import net.getbang.core.enums.ResponseCode;

import java.io.Serializable;

/**
 * @author C2M
 */
public class CommonResult<T> implements Serializable {
    private int code;
    private String msg;
    private T data;

    public CommonResult() {
        this.code = ResponseCode.success.code();
        this.msg = "";
        this.data = null;
    }

    public CommonResult(T t) {
        this(ResponseCode.success.code(), (String)null, t);
    }

    public CommonResult(int code, T t) {
        this(code, (String)null, t);
    }

    public CommonResult(int code, String msg, T t) {
        this.code = code;
        this.msg = msg;
        this.data = t;
    }
//    public static CommonResult data(Object data) {
//        return new CommonResult(ResponseCode.SUCCESS.code(), ResponseCode.SUCCESS.getDesc(), data);
//    }
    public static <T> CommonResult<T> success(T data) {
        CommonResult<T> result = new CommonResult<>();
        result.code = ResponseCode.SUCCESS.getCode();
        result.data = data;
        //result.msg = message;
        return result;
    }

    public CommonResult success() {
        return this;
    }

//    public CommonResult success(T t) {
//        this.msg = "请求成功";
//        this.data = t;
//        return this;
//    }

    public CommonResult success(int code, T t) {
        this.code = code;
        this.msg = "请求成功";
        this.data = t;
        return this;
    }

    public CommonResult success(int code, String msg, T t) {
        this.code = code;
        this.msg = msg;
        this.data = t;
        return this;
    }

    public CommonResult failed() {
        this.code = ResponseCode.error.code();
        this.msg = "请求成功,未查询到数据";
        return this;
    }

    public CommonResult failed(int code, T t) {
        this.code = code;
        this.msg = "请求成功";
        return this;
    }

    public CommonResult failed(int code, String msg, T t) {
        this.code = code;
        this.msg = msg;
        this.data = t;
        return this;
    }

    public CommonResult failed(String msg) {
        this.code = ResponseCode.error.code();
        this.msg = msg;
        return this;
    }

    public CommonResult saveSuccess(int num) {
        this.msg = String.format("成功保存%s条数据", num);
        return this;
    }

    public CommonResult editSuccess(int num) {
        this.msg = String.format("成功修改%s条数据", num);
        return this;
    }

    public CommonResult deleteSuccess(int num) {
        this.msg = String.format("成功删除%s条数据", num);
        return this;
    }

    public static CommonResult error(String msg) {
        return new CommonResult(ResponseCode.error.code(), msg, (Object)null);
    }

    public static CommonResult build(ResponseCode responseCode) {
        return new CommonResult(responseCode.code(), responseCode.desc(), (Object)null);
    }

    public CommonResult buildByResponseCode(ResponseCode responseCode) {
        this.code = responseCode.getCode();
        this.msg = responseCode.getDesc();
        return this;
    }

    public static CommonResult error(String msg, String exception) {
        return new CommonResult(ResponseCode.error.code(), msg, exception);
    }

    public static CommonResult error(Integer code, String msg) {
        return new CommonResult(code, msg, (Object)null);
    }


    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
