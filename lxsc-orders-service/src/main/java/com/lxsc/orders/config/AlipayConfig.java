package com.lxsc.orders.config;

// 开源的缘故，部分配置没有提供
public class AlipayConfig {

    // 商户appid项目上线以后替换成应用的AppID
    public static String APPID = "";

    // 私钥pkcs8格式的
    public static String RSA_PRIVATE_KEY = "";

    // 服务器异步通知页面路径需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://localhost:8083/notify_url";

    // 页面跳转同步通知页面路径需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问商户可以自定义同步跳转地址
    public static String return_url = "http://localhost:8083/paySuccess";

    // 请求网关地址，开始时候的沙箱环境的支付地址（非真实支付）。项目上线以后使用的真实支付地址
    public static String URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    // public static String URL = "";  // 项目上线时改成真实地址

    // 编码
    public static String CHARSET = "UTF-8";

    // 返回格式
    public static String FORMAT = "json";

    // 支付宝公钥
    public static String ALIPAY_PUBLIC_KEY = "";

    // 日志记录目录
    public static String log_path = "/log";

    // RSA2
    public static String SIGNTYPE = "RSA2";

}
