package com.lxsc.commons;

/**
 * 请求响应状态码
 */
public enum Code {

    OK("10000", "请求成功"),
    ERROR("10001", "请求失败"),
    NO_LOGIN("10002", "没有登陆"),
    NO_GOODS_STORE("10003", "没有库存"),
    NO_CONFIRM_ORDERS("11000", "没有确认订单");

    private String code;        // 状态码

    private String msg;         // 详细信息

    Code(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


}