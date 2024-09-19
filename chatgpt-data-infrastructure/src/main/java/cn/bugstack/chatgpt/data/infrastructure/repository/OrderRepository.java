package cn.bugstack.chatgpt.data.infrastructure.repository;

import cn.bugstack.chatgpt.data.domain.openai.model.valobj.UserAccountStatusVO;
import cn.bugstack.chatgpt.data.domain.order.model.aggregates.CreateOrderAggregate;
import cn.bugstack.chatgpt.data.domain.order.model.entity.*;
import cn.bugstack.chatgpt.data.domain.order.model.valobj.OrderStatusVO;
import cn.bugstack.chatgpt.data.domain.order.model.valobj.PayStatusVO;
import cn.bugstack.chatgpt.data.domain.order.repository.IOrderRepository;
import cn.bugstack.chatgpt.data.infrastructure.dao.IOpenAIOrderDao;
import cn.bugstack.chatgpt.data.infrastructure.dao.IOpenAIProductDao;
import cn.bugstack.chatgpt.data.infrastructure.dao.IUserAccountDao;
import cn.bugstack.chatgpt.data.infrastructure.po.OpenAIOrderPO;
import cn.bugstack.chatgpt.data.infrastructure.po.OpenAIProductPO;
import cn.bugstack.chatgpt.data.infrastructure.po.UserAccountPO;
import cn.bugstack.chatgpt.data.types.enums.OpenAIProductEnableModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author atticus
 * @Date 24/8/2024 14:26
 * @description: 订单仓储-数据库操作
 */
@Repository
@Slf4j
public class OrderRepository implements IOrderRepository {
    @Resource
    private IOpenAIOrderDao openAIOrderDao;
    @Resource
    private IOpenAIProductDao openAIProductDao;
    @Resource
    private IUserAccountDao userAccountDao;

    @Override
    public UnpaidOrderEntity queryUnpaidOrder(ShopCartEntity shopCartEntity) {
        // 根据 openid+productid 查询未支付订单
        OpenAIOrderPO openAIOrderPOReq  = OpenAIOrderPO.builder()
                .openid(shopCartEntity.getOpenid())
                .productId(shopCartEntity.getProductId())
                .build();

        OpenAIOrderPO openAIOrderPORes = openAIOrderDao.queryUnpaidOrder(openAIOrderPOReq);
        if(null == openAIOrderPORes) return null;

        return UnpaidOrderEntity.builder()
                .openid(shopCartEntity.getOpenid())
                .orderId(openAIOrderPORes.getOrderId())
                .productName(openAIOrderPORes.getProductName())
                .totalAmount(openAIOrderPORes.getTotalAmount())
                .payUrl(openAIOrderPORes.getPayUrl())
                .payStatus(PayStatusVO.get(openAIOrderPORes.getPayStatus()))
                .build();

    }

    @Override
    public ProductEntity queryProduct(Integer productId) {
        OpenAIProductPO openAiProductPO = openAIProductDao.queryProductByProductId(productId);
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductId(openAiProductPO.getProductId());
        productEntity.setProductName(openAiProductPO.getProductName());
        productEntity.setProductDesc(openAiProductPO.getProductDesc());
        productEntity.setQuota(openAiProductPO.getQuota());
        productEntity.setPrice(openAiProductPO.getPrice());
        productEntity.setEnable(OpenAIProductEnableModel.get(openAiProductPO.getIsEnabled()));
        return productEntity;
    }

    @Override
    public void saveOrder(CreateOrderAggregate aggregate) {
        String openid = aggregate.getOpenid();
        ProductEntity product = aggregate.getProduct();
        OrderEntity order = aggregate.getOrder();

        OpenAIOrderPO openAIOrderPO = new OpenAIOrderPO();
        openAIOrderPO.setOpenid(openid);
        openAIOrderPO.setProductId(product.getProductId());
        openAIOrderPO.setProductName(product.getProductName());
        openAIOrderPO.setProductQuota(product.getQuota());
        openAIOrderPO.setOrderId(order.getOrderId());
        openAIOrderPO.setOrderTime(order.getOrderTime());
        openAIOrderPO.setOrderStatus(order.getOrderStatus().getCode());
        openAIOrderPO.setTotalAmount(order.getTotalAmount());
        openAIOrderPO.setPayType(order.getPayTypeVO().getCode());
        openAIOrderPO.setPayStatus(PayStatusVO.WAIT.getCode());
        openAIOrderDao.insert(openAIOrderPO);

    }

    @Override
    public void updateOrderPayInfo(PayOrderEntity payOrderEntity) {
        try {
            OpenAIOrderPO openAiOrderPO = new OpenAIOrderPO();
            openAiOrderPO.setOpenid(payOrderEntity.getOpenid());
            openAiOrderPO.setOrderId(payOrderEntity.getOrderId());
            openAiOrderPO.setPayUrl(payOrderEntity.getPayUrl());
            openAiOrderPO.setPayStatus(payOrderEntity.getPayStatus().getCode());
            openAIOrderDao.updateOrderPayInfo(openAiOrderPO);
        }catch (Exception e){
            log.info("执行数据库查询出错,错误原因： {}",e.getMessage());

        }
    }

    @Override
    public boolean changeOrderPaySuccess(String orderId, String transactionId, BigDecimal totalAmount, Date payTime) {
        OpenAIOrderPO openAIOrderPO = new OpenAIOrderPO();
        openAIOrderPO.setOrderId(orderId);
        openAIOrderPO.setPayAmount(totalAmount);
        openAIOrderPO.setPayTime(payTime);
        openAIOrderPO.setTransactionId(transactionId);
        int count = openAIOrderDao.changeOrderPaySuccess(openAIOrderPO);
        return count == 1;

    }

    @Override
    public CreateOrderAggregate queryOrder(String orderId) {

        OpenAIOrderPO openAIOrderPO = openAIOrderDao.queryOrder(orderId);

        ProductEntity product = new ProductEntity();
        product.setProductId(openAIOrderPO.getProductId());
        product.setProductName(openAIOrderPO.getProductName());

        OrderEntity order = new OrderEntity();
        order.setOrderId(openAIOrderPO.getOrderId());
        order.setOrderTime(openAIOrderPO.getOrderTime());
        order.setOrderStatus(OrderStatusVO.get(openAIOrderPO.getOrderStatus()));
        order.setTotalAmount(openAIOrderPO.getTotalAmount());

        CreateOrderAggregate createOrderAggregate = new CreateOrderAggregate();
        createOrderAggregate.setOpenid(openAIOrderPO.getOpenid());
        createOrderAggregate.setOrder(order);
        createOrderAggregate.setProduct(product);

        return createOrderAggregate;

    }

    @Override
    @Transactional(rollbackFor = Exception.class, timeout = 350, propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void deliverGoods(String orderId) {
        OpenAIOrderPO openAIOrderPO = openAIOrderDao.queryOrder(orderId);

        // 1. 变更发货状态
        int updateOrderStatusDeliverGoodsCount = openAIOrderDao.updateOrderStatusDeliverGoods(orderId);
        if (1 != updateOrderStatusDeliverGoodsCount) throw new RuntimeException("updateOrderStatusDeliverGoodsCount update count is not equal 1");

        // 2. 账户额度变更
        UserAccountPO userAccountPO = userAccountDao.queryUserAccount(openAIOrderPO.getOpenid());
        UserAccountPO userAccountPOReq = new UserAccountPO();
        userAccountPOReq.setOpenid(openAIOrderPO.getOpenid());
        userAccountPOReq.setTotalQuota(openAIOrderPO.getProductQuota());
        userAccountPOReq.setSurplusQuota(openAIOrderPO.getProductQuota());

        if (null != userAccountPO){
            int addAccountQuotaCount = userAccountDao.addAccountQuota(userAccountPOReq);
            if (1 != addAccountQuotaCount) throw new RuntimeException("addAccountQuotaCount update count is not equal 1");
        } else {
            userAccountPOReq.setStatus(UserAccountStatusVO.AVAILABLE.getCode());
            userAccountPOReq.setModelTypes("gpt-3.5-turbo,gpt-3.5-turbo-16k,gpt-4,chatglm_lite,chatglm_std,chatglm_pro,chatglm_turbo");
            userAccountDao.insert(userAccountPOReq);
        }

    }

    @Override
    public List<String> queryReplenishmentOrder() {
        return openAIOrderDao.queryReplenishmentOrder();
    }

    @Override
    public List<String> queryNoPayNotifyOrder() {
        return openAIOrderDao.queryNoPayNotifyOrder();
    }

    @Override
    public List<String> queryTimeoutCloseOrderList() {
        return openAIOrderDao.queryTimeoutCloseOrderList();
    }

    @Override
    public boolean changeOrderClose(String orderId) {
        return openAIOrderDao.changeOrderClose(orderId);
    }

    @Override
    public List<ProductEntity> queryProductList() {
        List<OpenAIProductPO> openAIProductPOList =  openAIProductDao.queryProductList();
        List<ProductEntity> productEntityList = new ArrayList<>(openAIProductPOList.size());
        for (OpenAIProductPO openAIProductPO : openAIProductPOList) {
            ProductEntity productEntity = new ProductEntity();
            productEntity.setProductId(openAIProductPO.getProductId());
            productEntity.setProductName(openAIProductPO.getProductName());
            productEntity.setProductDesc(openAIProductPO.getProductDesc());
            productEntity.setQuota(openAIProductPO.getQuota());
            productEntity.setPrice(openAIProductPO.getPrice());
            productEntityList.add(productEntity);
        }
        return productEntityList;

    }
}
