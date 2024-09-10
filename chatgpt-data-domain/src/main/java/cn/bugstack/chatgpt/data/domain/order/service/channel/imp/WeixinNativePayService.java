package cn.bugstack.chatgpt.data.domain.order.service.channel.imp;

import cn.bugstack.chatgpt.data.domain.order.model.entity.CloseOrderEntity;
import cn.bugstack.chatgpt.data.domain.order.model.entity.NotifyOrderEntity;
import cn.bugstack.chatgpt.data.domain.order.model.entity.PayOrderEntity;
import cn.bugstack.chatgpt.data.domain.order.service.channel.PayMethodGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @Author atticus
 * @Date 31/8/2024 15:52
 * @description: 微信原生支付,暂未实现
 */
@Slf4j
@Service
public class WeixinNativePayService implements PayMethodGroupService {
    @Override
    public PayOrderEntity doPrepayOrder(String openid, String orderId, String productName, BigDecimal amountTotal) throws Exception {
        return null;
    }

    @Override
    public NotifyOrderEntity checkNoPayNotifyOrder(String orderId) throws Exception {
        return null;
    }

    @Override
    public CloseOrderEntity changeOrderClose(String orderId) throws Exception {
        return null;
    }
}
