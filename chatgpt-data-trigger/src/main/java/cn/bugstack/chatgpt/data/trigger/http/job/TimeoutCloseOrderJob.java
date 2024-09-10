package cn.bugstack.chatgpt.data.trigger.http.job;

import cn.bugstack.chatgpt.data.domain.order.model.entity.CloseOrderEntity;
import cn.bugstack.chatgpt.data.domain.order.model.valobj.PayTypeVO;
import cn.bugstack.chatgpt.data.domain.order.service.IOrderService;
import cn.bugstack.chatgpt.data.domain.order.service.channel.PayMethodGroupService;
import cn.bugstack.chatgpt.data.domain.order.service.channel.imp.AlipaySandboxService;
import cn.bugstack.chatgpt.data.domain.order.service.channel.imp.WeixinNativePayService;
import cn.bugstack.chatgpt.data.types.common.Constants;
import cn.bugstack.chatgpt.data.types.enums.channel.PayMethodChannel;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.response.AlipayTradeCloseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author atticus
 * @Date 28/8/2024 18:32
 * @description: 超时关单任务
 */
@Slf4j
@Component
public class TimeoutCloseOrderJob {
    private final Map<PayMethodChannel, PayMethodGroupService> payMethodGroup = new HashMap<>();

    public TimeoutCloseOrderJob(WeixinNativePayService weixinNativePayService, AlipaySandboxService alipaySandboxService) {
        payMethodGroup.put(PayMethodChannel.WEIXIN_NATIVE_PAY, weixinNativePayService);
        payMethodGroup.put(PayMethodChannel.ALIPAY_SANDBOX, alipaySandboxService);
    }
    @Resource
    private IOrderService orderService;

    @Scheduled(cron = "0 0/10 * * * ?")
    public void exec() {
        try {
            List<String> orderIds = orderService.queryTimeoutCloseOrderList();

            if (orderIds.isEmpty()) {
                log.info("定时任务，超时30分钟订单关闭，暂无超时未支付订单 orderIds is null");
                return;
            }

            for (String orderId : orderIds) {
                // 查询订单支付方式
                PayMethodChannel payMethod = PayMethodChannel.getChannel(PayTypeVO.get("AlipaySandbox").getDesc());

                CloseOrderEntity closeOrderEntity = payMethodGroup.get(payMethod).changeOrderClose(orderId);

                if (Constants.ResponseCode.SUCCESS.equals(closeOrderEntity.getTradeStatus())) {
                    boolean status = orderService.changeOrderClose(orderId);
                    log.info("定时任务，超时30分钟订单关闭 orderId: {} status：{}", orderId, status);
                } else {
                    log.info("订单: {} 支付关单失败", orderId);
                }
            }
        } catch (Exception e) {
            log.error("定时任务，超时30分钟订单关闭失败", e);
        }
    }

}
