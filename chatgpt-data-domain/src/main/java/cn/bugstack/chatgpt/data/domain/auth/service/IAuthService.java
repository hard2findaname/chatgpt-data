package cn.bugstack.chatgpt.data.domain.auth.service;

import cn.bugstack.chatgpt.data.domain.auth.model.entity.AuthStateEntity;

/**
 * @Title: IAuthService
 * @Author Atticus
 * @Package cn.bugstack.chatgpt.data.domain.auth.service
 * @date 15/8/2024 19:25
 * @description:
 */
public interface IAuthService {

    /**
     * 登录验证
     * @param code 验证码
     * @return Token
     */
    AuthStateEntity doLogin(String code);

    boolean checkToken(String token);

    String openid(String token);

}
