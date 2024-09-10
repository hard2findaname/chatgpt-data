package cn.bugstack.chatgpt.data.domain.order.service.channel.imp;

import cn.bugstack.chatgpt.data.domain.order.model.entity.CloseOrderEntity;
import cn.bugstack.chatgpt.data.domain.order.model.entity.NotifyOrderEntity;
import cn.bugstack.chatgpt.data.domain.order.model.entity.PayOrderEntity;
import cn.bugstack.chatgpt.data.domain.order.model.valobj.PayStatusVO;
import cn.bugstack.chatgpt.data.domain.order.repository.IOrderRepository;
import cn.bugstack.chatgpt.data.domain.order.service.channel.PayMethodGroupService;
import cn.bugstack.chatgpt.data.types.common.Constants;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @Author atticus
 * @Date 2024/08/31 15:54
 * @description: 支付宝沙箱支付
 */
@Slf4j
@Service
public class AlipaySandboxService implements PayMethodGroupService {
    @Value("${alipay.notify_url}")
    private String notifyUrl;
    @Value("${alipay.return_url}")
    private String returnUrl;

    @Autowired(required = false)
    private AlipayClient alipayClient;
    @Resource
    IOrderRepository orderRepository;

    /**
     * @description:
     * @author: attiucs
     * @param: openid 支付主体的识别id
     * @param: orderId 项目后端生成的订单id
     * @param: productName 购买的商品名称
     * @param: amountTotal 购买量
     * @return: PayOrderEntity 支付订单实体
     **/
    @Override
    public PayOrderEntity doPrepayOrder(String openid, String orderId, String productName, BigDecimal amountTotal) throws Exception {
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
//        AlipayTradePagePayRequest request  = new AlipayTradePagePayRequest();
        request.setNotifyUrl(notifyUrl);
//        request.setReturnUrl(returnUrl);

        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", orderId);
        bizContent.put("total_amount", amountTotal.toString());
        bizContent.put("subject", productName);
//        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");
        request.setBizContent(bizContent.toString());


        String payUrl = "";
        if(null != alipayClient) {
            AlipayTradePrecreateResponse response = alipayClient.execute(request);
//            AlipayTradePagePayResponse response = alipayClient.pageExecute(request);
            log.info("完成支付宝订单下单" + response.getBody());
            payUrl = response.getQrCode();
//            log.info("完成支付宝订单下单" +payUrl);
        }else {
            payUrl = "因未配置支付宝沙箱支付渠道，所以暂时不能生成支付URL";
        }
        PayOrderEntity payOrderEntity = PayOrderEntity.builder()
                .openid(openid)
                .orderId(orderId)
                .payStatus(PayStatusVO.WAIT)
                .payUrl(payUrl).build();

//        PayOrderEntity payOrderEntity = new PayOrderEntity();
//        payOrderEntity.setOrderId(orderId);
//        payOrderEntity.setPayUrl(form);
//        payOrderEntity.setPayStatus(OrderStatusVO.WAIT);

        // 更新订单支付信息
        orderRepository.updateOrderPayInfo(payOrderEntity);
        return payOrderEntity;
    }

    @Override
    public NotifyOrderEntity checkNoPayNotifyOrder(String orderId) throws Exception {
        if (null == alipayClient) {
            log.info("定时任务，订单支付状态更新。应用未配置 AlipaySandbox 支付渠道，任务不执行。");
            return NotifyOrderEntity.builder()
                    .tradeStatus(Constants.ResponseCode.UNABLE_CONFIG)
                    .orderId(orderId)
                    .build();
        }

        log.info("正在尝试查询订单: {} 支付状态", orderId);

        // 订单查询 请求参数
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();

        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", orderId);

        request.setBizContent(bizContent.toString());

        // 查询
        AlipayTradeQueryResponse response = alipayClient.execute(request);

        if ("TRADE_SUCCESS".equals(response.getTradeStatus())) {
            return NotifyOrderEntity.builder()
                    .tradeStatus(Constants.ResponseCode.SUCCESS)
                    .orderId(orderId)
                    .transactionId(response.getTradeNo())
                    .totalAmount(new BigDecimal(response.getBuyerPayAmount()))
                    .successTime(response.getSendPayDate())
                    .build();
        } else {
            return NotifyOrderEntity.builder()
                    .tradeStatus(Constants.ResponseCode.UN_ERROR)
                    .orderId(orderId)
                    .build();
        }
    }

    @Override
    public CloseOrderEntity changeOrderClose(String orderId) throws Exception {
        if (null == alipayClient) {
            log.info("定时任务，超时30分钟订单关闭。应用未配置 AlipaySandbox 支付渠道，任务不执行。");
            return CloseOrderEntity.builder()
                    .tradeStatus(Constants.ResponseCode.UNABLE_CONFIG)
                    .orderId(orderId)
                    .build();
        }

        log.info("正在尝试关闭订单: {}", orderId);

        // 订单查询 请求参数
        AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();

        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", orderId);

        request.setBizContent(bizContent.toString());

        // 查询
        AlipayTradeCloseResponse response = alipayClient.execute(request);

        // 关单成功 或者 用户始终没有扫描开启支付
        if ("Success".equals(response.getMsg()) || "ACQ.TRADE_NOT_EXIST".equals(response.getSubCode())) {
            return CloseOrderEntity.builder()
                    .tradeStatus(Constants.ResponseCode.SUCCESS)
                    .orderId(orderId)
                    .transactionId(response.getTradeNo())
                    .build();
        } else {
            return CloseOrderEntity.builder()
                    .tradeStatus(Constants.ResponseCode.UNABLE_CONFIG)
                    .orderId(orderId)
                    .build();
        }
    }
}
