package cn.bugstack.chatgpt.data.domain.order.model.aggregates;

import cn.bugstack.chatgpt.data.domain.order.model.entity.OrderEntity;
import cn.bugstack.chatgpt.data.domain.order.model.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author atticus
 * @Date 19/8/2024 21:51
 * @description: 订单信息聚合对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderAggregate {
    /** 用户ID；微信用户唯一标识 */
    private String openid;
    /** 商品 */
    private ProductEntity product;
    /** 订单 */
    private OrderEntity order;

}
