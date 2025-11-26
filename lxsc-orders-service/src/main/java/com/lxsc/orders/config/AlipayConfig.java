package com.lxsc.orders.config;

public class AlipayConfig {

    // 商户appid项目上线以后替换成应用的AppID
    public static String APPID = "9021000152601136";

    // 私钥pkcs8格式的
    public static String RSA_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC1oxqQi/hXA7yzEJNGv18Om3qj3oObT1pS8Z0Cs5sq6TMsC3JQ1EW2CvxXOuuHCCQwF2hyMG6a3kQRqBt+mqz/sdSocpyXXRD3ZzReEL1dSLSfPUVPF6Dh5H4LVi/BxkKJy/75dpfP6JxA9gzg7RgwS543WUvVxnuoNiMM8JfJ9Do+pNawTq9ohFYld2niOokUFpMIqNI69oO3oU4vVZRSi8GnhYrOX35ovni9/6FC1Bc/LH1wHLYLbvhMkPNoIHSLZUy+8Jx/h+ZGddQsC/tEgkK+tdK4iBDNds5/rEQC8LwAyh3+UjqBZFb5XQIJoYjGG9yb7kgl9b/zSfa276M5AgMBAAECggEAYN4OQb5T/9vfW8PgN1/CCyFCA3icyUxFiGyQZqtwei8J+lhUa+T5FH/BWafzifz+h6NEMXnA0tjshloX56oHzwmAtyEAf/zDGp4woGXfB4vJwA7GEsswIqhfzB33tCjOvXxMi7ACgH+2LTLLZX0sZD0/sJbvyZ9jvS65/KGm4a126tWQk6iuvsjGAIZLKugqY36aHzAemx23YBEHTIYwSedWShT6HLNJi+cIJGhqg/p1UyiBzeNvBQumOR9ztmzWiVuoaofBTTiTmzz/ZVwdetBWXfDSYoQUV6Ig0WCKvWGCgo1XPzRXKy/YXG2nkM9wA/bSydT4h2t81Awbqk5Z8QKBgQDfN0GPIEVLmzzSEgnfmTgKpbkuPFQDdGEeJ+1/U6XQzGX4qom92yZ0Vx8RXrAmnFEYU+7D/ifdlIxKv0CNSJJyVfP+2TLApa/OROgfZy9hss2ytG+q6AA/iJKjjQVADSuu7C9BVtiiq3tqQzEMSUBpOtMU6NudFtv2Ko9p2qHwpQKBgQDQUIUSa+U/Y166nLtHHEEd1IrPBwsDtVx5YDUEf8CxMKTGzWxkWccxM9TUI3Uil4iAaYoVP371MA9AzIUXxge4hb9Xcn7+Vca2wm79XCG3JWuwgf4FZOK87lOQOlelWXoT8XDfLzskLz9G52v721OLd949N4n7eOVx+x1cQlswBQKBgHi7E0uNoc1WBpx8wzhcUBJ6NmHKoYpIzSqZX/ypSptXoqPP4bZ9rRvp71TeyRnpHHezFgaOFhPj/8mCDjEcmpEa6mDiK3BFbYR8gqxRjDAknNtWeruOxmuWu/GYGcneFc2D079ccMnzREpiU0oY6KzB819w3y/lKE3KEU2owkQ9AoGBAI7xoc7ZQO81VeyB3jTWm4xHq5SShaleyvzy2o6ix2wPMBLHJYja3kfSBEopppg9GUrcoQchkhHXj6/sZ6u/lM624Gy2QZRu8bO5SgYLMYuGXxY4rUGwT7JLbAX9MuVgt9d1hvlvZIrY7PfPl5a0kuiSMWsacFGiTPfg0+3aDs6tAoGAHrw4KzSpJzpfZtxdZfqw+agp7SIXUhWk2AeH5ZCmD6iK/zt7T1akd0EIOFHb9a5MNImAK9S1O0KG7w+AIQEZH1FimmZEzmbsU6pTYHexOTC99ICHQJrihrBtLsKIMC7Iq7lcH5kg206n59fs8xWsuGSkg03cLZNEJbLQ3TlRsdI=";

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
    public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvwMyP4qcuI3KLz3rEBirC8vFGpUrut8lENsQbXe3OyAi0oGTqKNi4MU/e3OLMiD8HNfGkc1qH35C0POr8FttXC9+YG/Wh6y+O3dh2Q5f8Z5pBLiOSAxKMlEit/imNOURjbjKw3I9dnAbNQRBf8Pz2yOxcOVTQwuv+hxYGTJjQ+TMn0/vMC7Cd2hVEUjv7eTkTb1FY8c9leN1DVdx+N3jkzgqAjuNwVq8HCs0GL10JJrnuSRKg9I/Y0gTdy1jLMTmEMzRc/jqx1dwXOQwWz14huilxTTza1d6+J8YrSNWu4Iu27j4SNAHl2uGW2Z9ywbIH2QcfmqpmwYva76k7RVNAwIDAQAB";

    // 日志记录目录
    public static String log_path = "/log";

    // RSA2
    public static String SIGNTYPE = "RSA2";

}
