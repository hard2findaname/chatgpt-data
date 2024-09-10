package cn.bugstack.chatgpt.data.domain.order.model.entity;

import cn.bugstack.chatgpt.data.types.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author atticus
 * @Date 2024/08/31 15:56
 * @description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CloseOrderEntity {
    /**
     * 交易成功与否
     */
    private Constants.ResponseCode tradeStatus;

    /**
     * 订单 id
     */
    private String orderId;

    /**
     * 交易 id
     */
    private String transactionId;

}
