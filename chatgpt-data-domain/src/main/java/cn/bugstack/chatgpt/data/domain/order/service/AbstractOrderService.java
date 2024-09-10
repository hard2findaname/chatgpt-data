package cn.bugstack.chatgpt.data.domain.order.service;

import cn.bugstack.chatgpt.data.domain.order.model.entity.*;
import cn.bugstack.chatgpt.data.domain.order.model.valobj.PayStatusVO;
import cn.bugstack.chatgpt.data.domain.order.repository.IOrderRepository;
import cn.bugstack.chatgpt.data.domain.order.service.channel.PayMethodGroupService;
import cn.bugstack.chatgpt.data.domain.order.service.channel.imp.AlipaySandboxService;
import cn.bugstack.chatgpt.data.domain.order.service.channel.imp.WeixinNativePayService;
import cn.bugstack.chatgpt.data.types.common.Constants;
import cn.bugstack.chatgpt.data.types.enums.channel.PayMethodChannel;
import cn.bugstack.chatgpt.data.types.exception.ChatGPTException;
import com.alipay.api.AlipayApiException;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author atticus
 * @Date 19/8/2024 22:20
 * @description: 抽象订单服务
 */
@Slf4j
public abstract class AbstractOrderService implements IOrderService{
    private final Map<PayMethodChannel, PayMethodGroupService> payMethodGroup = new HashMap<>();

    public AbstractOrderService(WeixinNativePayService weixinNativePayService, AlipaySandboxService alipaySandboxService) {
        payMethodGroup.put(PayMethodChannel.WEIXIN_NATIVE_PAY, weixinNativePayService);
        payMethodGroup.put(PayMethodChannel.ALIPAY_SANDBOX, alipaySandboxService);
    }
    @Resource
    protected IOrderRepository orderRepository;
    /**
     * @description: 用户下单，通过购物车信息，返回下单后的支付单
     * @author: attiucs
     * @param: shopCartEntity 购物车实体
     * @return: PayOrderEntity 支付单实体
     **/
    @Override
    public PayOrderEntity createOrder(ShopCartEntity shopCartEntity) {
        try {
            // 0. 基础信息
            String openid = shopCartEntity.getOpenid();
            Integer productId = shopCartEntity.getProductId();

            // 1. 查询有效的未支付订单，如果存在直接返回支付 CodeUrl
            UnpaidOrderEntity unpaidOrderEntity = orderRepository.queryUnpaidOrder(shopCartEntity);
            if (null != unpaidOrderEntity && PayStatusVO.WAIT.equals(unpaidOrderEntity.getPayStatus()) && null != unpaidOrderEntity.getPayUrl()) {
                log.info("创建订单-存在，已生成支付链接，返回 openid: {} orderId: {} payUrl: {}", openid, unpaidOrderEntity.getOrderId(), unpaidOrderEntity.getPayUrl());
                return PayOrderEntity.builder()
                        .openid(openid)
                        .orderId(unpaidOrderEntity.getOrderId())
                        .payUrl(unpaidOrderEntity.getPayUrl())
                        .payStatus(unpaidOrderEntity.getPayStatus())
                        .build();
            }
            // 存在订单 + 状态:等待支付 + 未生成支付 URL
            else if (null != unpaidOrderEntity && null == unpaidOrderEntity.getPayUrl()) {
                log.info("创建订单-存在，未生成支付链接，返回 openid: {} orderId: {}", openid, unpaidOrderEntity.getOrderId());
                PayOrderEntity payOrderEntity = payMethodGroup.get(shopCartEntity.getPayMethodChannel()).doPrepayOrder(openid, unpaidOrderEntity.getOrderId(), unpaidOrderEntity.getProductName(), unpaidOrderEntity.getTotalAmount());
                log.info("创建订单-完成，生成支付单。openid: {} orderId: {} payUrl: {}", openid, payOrderEntity.getOrderId(), payOrderEntity.getPayUrl());
                return payOrderEntity;
            }

            // 2. 商品查询
            ProductEntity productEntity = orderRepository.queryProduct(productId);
            if (!productEntity.isAvailable()) {
                throw new ChatGPTException(Constants.ResponseCode.ORDER_PRODUCT_ERR.getCode(), Constants.ResponseCode.ORDER_PRODUCT_ERR.getInfo());
            }

            // 3. 保存订单
            OrderEntity orderEntity = this.doSaveOrder(openid, productEntity, shopCartEntity.getPayMethodChannel());

            // 4. 创建支付
            PayOrderEntity payOrderEntity = payMethodGroup.get(shopCartEntity.getPayMethodChannel()).doPrepayOrder(openid, orderEntity.getOrderId(), productEntity.getProductName(), orderEntity.getTotalAmount());
            log.info("创建订单-完成，生成支付单。openid: {} orderId: {} payUrl: {}", openid, orderEntity.getOrderId(), payOrderEntity.getPayUrl());

            return payOrderEntity;
        } catch (Exception e) {
            log.error("创建订单，未能生成支付，返回 openid: {} productId: {}", shopCartEntity.getOpenid(), shopCartEntity.getProductId());
            throw new ChatGPTException(Constants.ResponseCode.UN_ERROR.getCode(), Constants.ResponseCode.UN_ERROR.getInfo());
        }


    }
    protected abstract OrderEntity doSaveOrder(String openid, ProductEntity productEntity, PayMethodChannel payMethod);




}
