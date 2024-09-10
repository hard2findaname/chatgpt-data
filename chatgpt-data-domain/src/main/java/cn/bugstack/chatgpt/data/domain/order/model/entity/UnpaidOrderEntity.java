package cn.bugstack.chatgpt.data.domain.order.model.entity;

import cn.bugstack.chatgpt.data.domain.order.model.valobj.PayStatusVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author atticus
 * @Date 19/8/2024 22:08
 * @description: 未支付的订单,与已经支付的订单做区别，因为需要返回其他的内容
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UnpaidOrderEntity{
    /**
     * 用户ID；微信分配的唯一ID编码
     */
    private String openid;
    /**
     * 订单ID
     */
    private String orderId;
    /**
     * 订单金额
     */
    private BigDecimal totalAmount;
    /**
     * 商品名称
     */
    private String productName;
    /**
     * 支付地址；创建支付后，获得的URL地址
     */
    private String payUrl;
    /**
     * 支付状态；0-等待支付、1-支付完成、2-支付失败、3-放弃支付
     */
    private PayStatusVO payStatus;


}
