package cn.bugstack.chatgpt.data.trigger.mq;

import cn.bugstack.chatgpt.data.domain.order.service.IOrderService;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author atticus
 * @Date 28/8/2024 18:27
 * @description: 消息队列，订单支付成功监听。使用eventBus，考虑使用RocketMQ
 */
@Slf4j
@Component

public class OrderPaySuccessListener {

    @Resource
    private IOrderService orderService;

    @Subscribe
    public void handleEvent(String orderId) {
        try {
            log.info("支付完成，发货并记录，开始。订单：{}", orderId);
            orderService.deliverGoods(orderId);
        } catch (Exception e) {
            log.error("支付完成，发货并记录，失败。订单：{}", orderId, e);
        }
    }

}
