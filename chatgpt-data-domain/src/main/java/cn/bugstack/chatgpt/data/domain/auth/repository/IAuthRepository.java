package cn.bugstack.chatgpt.data.domain.auth.repository;

/**
 * @Author atticus
 * @Date 2024/09/19 11:13
 * @description: 认证仓储服务
 */
public interface IAuthRepository {
    String getCodeUserOpenId(String code);

    void removeCodeByOpenId(String code, String openId);

}
