package cn.bugstack.chatgpt.data.infrastructure.repository;

import cn.bugstack.chatgpt.data.domain.auth.repository.IAuthRepository;
import cn.bugstack.chatgpt.data.infrastructure.redis.IRedisService;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;


/**
 * @Author atticus
 * @Date 2024/09/19 11:34
 * @description:
 */
@Repository
public class AuthRepository implements IAuthRepository {
    private static final String Key = "weixin_code";

    @Resource
    private IRedisService redisService;

    @Override
    public String getCodeUserOpenId(String code) {
        return redisService.getValue(Key + "_" + code);
    }

    @Override
    public void removeCodeByOpenId(String code, String openId) {
        redisService.remove(Key + "_" + code);
        redisService.remove(Key + "_" + openId);

    }
}
