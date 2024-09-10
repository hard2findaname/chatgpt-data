package cn.bugstack.chatgpt.data.domain.order.model.entity;

import cn.bugstack.chatgpt.data.types.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author atticus
 * @Date 2024/08/31 15:56
 * @description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotifyOrderEntity {
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

    /**
     * 交易金额
     */
    private BigDecimal totalAmount;

    /**
     * 交易成功时间
     */
    private Date successTime;
}
