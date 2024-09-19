package cn.bugstack.chatgpt.data.domain.openai.service.channel;

import cn.bugstack.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;

/**
 * @Author atticus
 * @Date 2024/09/12 00:16
 * @description: AI对话模型服务组
 */
public interface OpenAIGroupService {
    void doMessageResponse(ChatProcessAggregate chatProcess, ResponseBodyEmitter emitter) throws IOException;
}
