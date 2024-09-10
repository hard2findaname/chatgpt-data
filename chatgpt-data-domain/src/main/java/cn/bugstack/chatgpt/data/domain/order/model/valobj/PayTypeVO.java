package cn.bugstack.chatgpt.data.domain.order.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author atticus
 * @Date 19/8/2024 21:55
 * @description: 支付类型值
 */
@Getter
@AllArgsConstructor
public enum PayTypeVO {
    WEIXIN_NATIVE(0, "WeixinNativePay"),
    ALIPAY(1, "AlipaySandbox"),
    ;

    private final Integer code;
    private final String desc;

    public static PayTypeVO get(Integer code) {
        switch (code) {
            case 1:
                return PayTypeVO.ALIPAY;
            case 0:
            default:
                return PayTypeVO.WEIXIN_NATIVE;
        }
    }

    public static PayTypeVO get(String payMethod) {
        switch (payMethod) {
            case "AlipaySandbox":
                return PayTypeVO.ALIPAY;
            case "WeixinNativePay":
            default:
                return PayTypeVO.WEIXIN_NATIVE;
        }
    }

}
