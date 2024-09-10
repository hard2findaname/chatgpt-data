package cn.bugstack.chatgpt.data.types.enums.channel;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author atticus
 * @Date 2024/08/31 16:07
 * @description: 支付方式枚举
 */
@Getter
@AllArgsConstructor
public enum PayMethodChannel {
    ALIPAY_SANDBOX("AlipaySandbox"),
    WEIXIN_NATIVE_PAY("WeixinNativePay");

    private final String code;

    public static PayMethodChannel getChannel(String payMethod) {
        switch (payMethod) {
            case "AlipaySandbox":
                return ALIPAY_SANDBOX;
            case "WeixinNativePay":
                return WEIXIN_NATIVE_PAY;
            default:
                return null;
        }
    }
}
