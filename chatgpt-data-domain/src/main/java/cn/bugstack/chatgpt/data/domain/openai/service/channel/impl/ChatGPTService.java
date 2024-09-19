package cn.bugstack.chatgpt.data.domain.openai.service.channel.impl;

import cn.bugstack.chatgpt.common.Constants;
import cn.bugstack.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import cn.bugstack.chatgpt.data.domain.openai.model.valobj.GenerativeModelVO;
import cn.bugstack.chatgpt.data.domain.openai.service.channel.OpenAIGroupService;
import cn.bugstack.chatgpt.data.domain.openai.service.channel.model.IGenerativeModelService;
import cn.bugstack.chatgpt.data.domain.openai.service.channel.model.impl.ImageGenerativeModelServiceImpl;
import cn.bugstack.chatgpt.data.domain.openai.service.channel.model.impl.TextGenerativeModelServiceImpl;
import cn.bugstack.chatgpt.data.types.exception.ChatGPTException;
import cn.bugstack.chatgpt.domain.chat.ChatChoice;
import cn.bugstack.chatgpt.domain.chat.ChatCompletionRequest;
import cn.bugstack.chatgpt.domain.chat.ChatCompletionResponse;
import cn.bugstack.chatgpt.domain.chat.Message;
import cn.bugstack.chatgpt.session.OpenAiSession;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author atticus
 * @Date 2024/09/12 00:17
 * @description: ChatGPT对接服务
 */
@Service
public class ChatGPTService implements OpenAIGroupService {
    private final Map<GenerativeModelVO, IGenerativeModelService> generativeModelGroup = new HashMap<>();

    public ChatGPTService(ImageGenerativeModelServiceImpl imageGenerativeModelService, TextGenerativeModelServiceImpl textGenerativeModelService) {
        generativeModelGroup.put(GenerativeModelVO.IMAGES, imageGenerativeModelService);
        generativeModelGroup.put(GenerativeModelVO.TEXT, textGenerativeModelService);
    }
    /**
     * @description:GPT的应答函数
     * @author: attiucs
     * @param: chatProcess
     * @param: emitter
     * @return: void
     **/
    @Override
    public void doMessageResponse(ChatProcessAggregate chatProcess, ResponseBodyEmitter emitter) throws IOException {
        GenerativeModelVO generativeModelVO = chatProcess.getGenerativeModelVO();
        generativeModelGroup.get(generativeModelVO).doMessageResponse(chatProcess, emitter);

    }
}
