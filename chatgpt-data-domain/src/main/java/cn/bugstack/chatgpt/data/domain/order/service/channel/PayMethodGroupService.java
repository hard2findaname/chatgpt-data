package cn.bugstack.chatgpt.data.domain.order.service.channel;

import cn.bugstack.chatgpt.data.domain.order.model.entity.CloseOrderEntity;
import cn.bugstack.chatgpt.data.domain.order.model.entity.NotifyOrderEntity;
import cn.bugstack.chatgpt.data.domain.order.model.entity.PayOrderEntity;

import java.math.BigDecimal;

/**
 * @Author atticus
 * @Date 31/8/2024 15:49
 * @description: 支付方式服务组
 */

public interface PayMethodGroupService {
    PayOrderEntity doPrepayOrder(String openid, String orderId, String productName, BigDecimal amountTotal) throws Exception;

    NotifyOrderEntity checkNoPayNotifyOrder(String orderId) throws Exception;

    CloseOrderEntity changeOrderClose(String orderId) throws Exception;
}
