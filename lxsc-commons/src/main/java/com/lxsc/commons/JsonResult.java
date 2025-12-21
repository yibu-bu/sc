package com.lxsc.commons;

/**
 * 请求响应的Json封装对象，这么封装并不是必须的，而是一种推荐的解决方案。
 * 不是说不封装JsonResult返回的就不是JSON格式数据了，返回的一样是JSON格式数据。
 * 这么做可以携带更多信息，并且让前后端交互的JSON格式更统一。
 *
 * @param <T> 具体响应数据泛型
 */
public class JsonResult<T> {

    private Code code;

    private String codeStr;       // 状态码

    private String msg;           // 详细信息

    private T result;             // 返回的具体数据

    public JsonResult() {
    }

    public JsonResult(Code code, T result) {
        this(code, code.getMsg(), result);
    }

    public JsonResult(Code code, String msg, T result) {
        this.codeStr = code.getCode();
        this.msg = msg;
        this.result = result;
    }

    public String getCode() {
        return codeStr;
    }

    public void setCode(String code) {
        this.codeStr = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

}