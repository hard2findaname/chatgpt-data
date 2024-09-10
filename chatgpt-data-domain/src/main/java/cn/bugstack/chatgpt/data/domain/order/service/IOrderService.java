package cn.bugstack.chatgpt.data.domain.order.service;

import cn.bugstack.chatgpt.data.domain.order.model.aggregates.CreateOrderAggregate;
import cn.bugstack.chatgpt.data.domain.order.model.entity.PayOrderEntity;
import cn.bugstack.chatgpt.data.domain.order.model.entity.ProductEntity;
import cn.bugstack.chatgpt.data.domain.order.model.entity.ShopCartEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author atticus
 * @Date 19/8/2024 22:12
 * @description: 订单服务
 */
public interface IOrderService {
    /**
    * @Author Atticus
    * @Description 用户下单，通过购物车信息，返回下单后的支付单
    * @Param shopCartEntity 购物车实体
    * @return 支付单实体对象
    **/

    PayOrderEntity createOrder(ShopCartEntity shopCartEntity);
    /**
     * @description:
     * @author: attiucs
     * @param: orderId 订单 id
     * @param: transactionId
     * @param: totalAmount
     * @param: payTime
     * @return: boolean
     **/
    boolean changeOrderPaySuccess(String orderId, String transactionId, BigDecimal totalAmount, Date payTime);

    /**
    * @Description 查询订单信息
    * @Param orderId 订单ID
    * @return
    **/
    CreateOrderAggregate queryOrder(String orderId);

    /**
    * @Description 订单商品发货
    * @Param orderId 订单ID
    * @return
    **/
    void deliverGoods(String orderId);

    /**
     * 查询待补货订单
     */
    List<String> queryReplenishmentOrder();

    /**
     * 查询有效期内，未接收到支付回调的订单
     */
    List<String> queryNoPayNotifyOrder();

    /**
     * 查询超时15分钟，未支付订单
     */
    List<String> queryTimeoutCloseOrderList();

    /**
     * 变更；订单支付关闭
     */
    boolean changeOrderClose(String orderId);

    /**
     * 查询商品列表
     */
    List<ProductEntity> queryProductList();

}
