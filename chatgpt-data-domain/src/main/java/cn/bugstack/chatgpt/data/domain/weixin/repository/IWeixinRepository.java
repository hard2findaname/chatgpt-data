package cn.bugstack.chatgpt.data.domain.weixin.repository;

/**
 * @Author atticus
 * @Date 2024/09/19 11:22
 * @description: 微信服务仓储
 */
public interface IWeixinRepository {
    String genCode(String openId);
}
