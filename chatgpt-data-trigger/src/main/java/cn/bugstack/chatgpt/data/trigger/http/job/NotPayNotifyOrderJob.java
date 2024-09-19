package cn.bugstack.chatgpt.data.trigger.http.job;

import cn.bugstack.chatgpt.data.domain.order.model.entity.NotifyOrderEntity;
import cn.bugstack.chatgpt.data.domain.order.model.valobj.PayTypeVO;
import cn.bugstack.chatgpt.data.domain.order.service.IOrderService;
import cn.bugstack.chatgpt.data.domain.order.service.channel.PayMethodGroupService;
import cn.bugstack.chatgpt.data.domain.order.service.channel.imp.AlipaySandboxService;
import cn.bugstack.chatgpt.data.domain.order.service.channel.imp.WeixinNativePayService;
import cn.bugstack.chatgpt.data.types.common.Constants;
import cn.bugstack.chatgpt.data.types.enums.channel.PayMethodChannel;

import com.google.common.eventbus.EventBus;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;



import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author atticus
 * @Date 28/8/2024 18:31
 * @description: 检测未接收到或未正确处理的支付回调通知
 */
@Slf4j
@Component()
public class NotPayNotifyOrderJob {
    private final Map<PayMethodChannel, PayMethodGroupService> payMethodGroup = new HashMap<>();

    public NotPayNotifyOrderJob(WeixinNativePayService weixinNativePayService, AlipaySandboxService alipaySandboxService) {
        payMethodGroup.put(PayMethodChannel.WEIXIN_NATIVE_PAY, weixinNativePayService);
        payMethodGroup.put(PayMethodChannel.ALIPAY_SANDBOX, alipaySandboxService);
    }

    @Resource
    private IOrderService orderService;

    @Autowired(required = false)
    private EventBus eventBus;


    @Timed(value = "no_pay_notify_order_job", description = "定时任务，订单状态更新")
    @Scheduled(cron = "0/10 * * * * ?")
    public void exec() {
        try {
            List<String> orderIds = orderService.queryNoPayNotifyOrder();
            if (orderIds.isEmpty()) {
                log.info("定时任务，订单支付状态更新，暂无未更新订单 orderId is null");
                return;
            }

            // 已创建订单, 但未支付
            for (String orderId : orderIds) {
                // 查询订单支付方式
                PayMethodChannel payMethod = PayMethodChannel.getChannel(PayTypeVO.get("AlipaySandbox").getDesc());

                NotifyOrderEntity notifyOrderEntity = payMethodGroup.get(payMethod).checkNoPayNotifyOrder(orderId);

                // 更新订单
                if (Constants.ResponseCode.SUCCESS.equals(notifyOrderEntity.getTradeStatus())) {
                    boolean isSuccess = orderService.changeOrderPaySuccess(notifyOrderEntity.getOrderId(), notifyOrderEntity.getTransactionId(), notifyOrderEntity.getTotalAmount(), notifyOrderEntity.getSuccessTime());

                    if (isSuccess) {
                        log.info("订单: {} 检测到成功支付, 更新订单状态", orderId);
                        // 发布消息
                        eventBus.post(orderId);
                    }
                } else {
                    log.info("订单: {} 尚未检测到支付成功状态.", orderId);
                }
            }
        } catch (Exception e) {
            log.error("定时任务，订单支付状态更新失败", e);
        }
    }



}
