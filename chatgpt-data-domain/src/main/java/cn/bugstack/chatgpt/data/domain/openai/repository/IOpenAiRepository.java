package cn.bugstack.chatgpt.data.domain.openai.repository;

import cn.bugstack.chatgpt.data.domain.openai.model.entity.UserAccountEntity;

/**
 * @Title: IOpenAiRepository
 * @Author Atticus
 * @Package cn.bugstack.chatgpt.data.domain.openai.repository
 * @date 15/8/2024 19:30
 * @description: OpenAi 仓储接口
 */
public interface IOpenAiRepository {

    int subAccountQuota(String openai);

    UserAccountEntity queryUserAccount(String openid);

}
