package cn.bugstack.chatgpt.data.types.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Author Atticus
 * @date 15/8/2024 19:35
 * @description:
 */
public class Constants {

    public final static String SPLIT = ",";

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public enum ResponseCode {
        SUCCESS("0000", "成功"),
        UN_ERROR("0001", "未知失败"),
        ILLEGAL_PARAMETER("0002", "非法参数"),
        TOKEN_ERROR("0003", "权限拦截"),
        UNABLE_CONFIG("0004", "未配置服务"),
        ORDER_PRODUCT_ERR("OE001", "所购商品已下线，请重新选择下单商品"),
        ;

        private String code;
        private String info;

    }

}
