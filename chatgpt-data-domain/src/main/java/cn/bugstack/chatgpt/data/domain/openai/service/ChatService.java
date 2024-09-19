package cn.bugstack.chatgpt.data.domain.openai.service;

import cn.bugstack.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import cn.bugstack.chatgpt.data.domain.openai.model.entity.RuleLogicEntity;
import cn.bugstack.chatgpt.data.domain.openai.model.entity.UserAccountEntity;
import cn.bugstack.chatgpt.data.domain.openai.model.valobj.LogicCheckTypeVO;
import cn.bugstack.chatgpt.data.domain.openai.service.channel.impl.ChatGLMService;
import cn.bugstack.chatgpt.data.domain.openai.service.channel.impl.ChatGPTService;
import cn.bugstack.chatgpt.data.domain.openai.service.rule.ILogicFilter;
import cn.bugstack.chatgpt.data.domain.openai.service.rule.factory.DefaultLogicFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Title: ChatService
 * @Author Atticus
 * @Package cn.bugstack.chatgpt.data.domain.openai.service
 * @date 15/8/2024 19:31
 * @description:
 */
@Service
public class ChatService extends AbstractChatService {

    @Resource
    private DefaultLogicFactory logicFactory;

    public ChatService(ChatGPTService chatGPTService, ChatGLMService chatGLMService) {
        super(chatGPTService, chatGLMService);
    }

    /**
     * @return cn.bugstack.chatgpt.data.domain.openai.model.entity.RuleLogicEntity<cn.bugstack.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate>
     * @Author Atticus
     * @Description
     * @Date 17:52 15/8/2024
     * @Param [chatProcess, userAccountQuotaEntity, logics]
     **/
    @Override
    protected RuleLogicEntity<ChatProcessAggregate> doCheckLogic(ChatProcessAggregate chatProcess, UserAccountEntity userAccountEntity, String... logics) throws Exception {
        //获取
        Map<String, ILogicFilter<UserAccountEntity>> logicFilterMap = logicFactory.openLogicFilter();
        RuleLogicEntity<ChatProcessAggregate> ruleLogicEntity = null;
        for (String code : logics) {
            if (DefaultLogicFactory.LogicModel.NULL.getCode().equals(code)) continue;
            ruleLogicEntity = logicFilterMap.get(code).filter(chatProcess, userAccountEntity);
            if (!LogicCheckTypeVO.SUCCESS.equals(ruleLogicEntity.getType())) return ruleLogicEntity;
        }
        return ruleLogicEntity != null ? ruleLogicEntity : RuleLogicEntity.<ChatProcessAggregate>builder()
                .type(LogicCheckTypeVO.SUCCESS)
                .data(chatProcess)
                .build();
    }



}
