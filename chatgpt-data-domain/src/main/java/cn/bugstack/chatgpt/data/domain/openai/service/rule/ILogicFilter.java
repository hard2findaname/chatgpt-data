package cn.bugstack.chatgpt.data.domain.openai.service.rule;

import cn.bugstack.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import cn.bugstack.chatgpt.data.domain.openai.model.entity.RuleLogicEntity;

/**
 * @Title: ILogicFilter
 * @Author Atticus
 * @Package cn.bugstack.chatgpt.data.domain.openai.service.rule
 * @date 15/8/2024 19:30
 * @description: 规则过滤接口
 */
public interface ILogicFilter<T> {

    RuleLogicEntity<ChatProcessAggregate> filter(ChatProcessAggregate chatProcess, T data) throws Exception;

}
