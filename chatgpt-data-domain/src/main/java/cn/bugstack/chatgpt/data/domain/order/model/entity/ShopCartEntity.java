package cn.bugstack.chatgpt.data.domain.order.model.entity;

import cn.bugstack.chatgpt.data.types.enums.channel.PayMethodChannel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author atticus
 * @Date 19/8/2024 21:54
 * @description: 购物车实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShopCartEntity {
    /**
     * 用户微信唯一ID
     */
    private String openid;

    /**
     * 商品ID
     */
    private Integer productId;
    /**
     * 支付方式
     */
    private String payMethod;
    public PayMethodChannel getPayMethodChannel() {
        return PayMethodChannel.getChannel(this.payMethod);
    }
    @Override
    public String toString() {
        return "ShopCartEntity{" +
                "openid='" + openid + '\'' +
                ", productId=" + productId +
                '}';
    }

}
