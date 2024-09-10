package cn.bugstack.chatgpt.data.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author atticus
 * @Date 19/8/2024 23:08
 * @description:
 */
@Data
@ConfigurationProperties(prefix = "alipay", ignoreInvalidFields = true)
public class AlipayConfigProperties {
    // 「沙箱环境」应用ID - APPID，收款账号既是你的APPID对应支付宝账号。
    private String app_id;
    // 「沙箱环境」商户私钥，PKCS8格式RSA2私钥
    private String merchant_private_key;
    // 「沙箱环境」支付宝公钥
    private String alipay_public_key;
    // 「沙箱环境」服务器异步通知页面路径
    private String notify_url;
    // 「沙箱环境」页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    private String return_url;
    // 「沙箱环境」
    private String gatewayUrl;
    // 签名方式
    private String sign_type = "RSA2";
    // 字符编码格式
    private String charset = "utf-8";
    // 传输格式
    private String format = "json";


}
