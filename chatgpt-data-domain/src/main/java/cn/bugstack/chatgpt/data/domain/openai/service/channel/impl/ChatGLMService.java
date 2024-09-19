package cn.bugstack.chatgpt.data.domain.openai.service.channel.impl;


import cn.stack.chatglm.model.*;
import cn.stack.chatglm.session.OpenAiSession;

import cn.bugstack.chatgpt.data.types.enums.ChatGLMModel;
import cn.bugstack.chatgpt.data.types.exception.ChatGPTException;
import cn.bugstack.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import cn.bugstack.chatgpt.data.domain.openai.service.channel.OpenAIGroupService;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author atticus
 * @Date 2024/09/12 00:17
 * @description: GLM对接
 */
@Slf4j
@Service
public class ChatGLMService implements OpenAIGroupService {
    @Autowired(required = false)
    protected OpenAiSession chatGLMOpenAiSession;
    @Override
    public void doMessageResponse(ChatProcessAggregate chatProcess, ResponseBodyEmitter emitter) throws IOException {


        if (null == chatGLMOpenAiSession) {
            emitter.send("ChatGLM 通道，模型调用未开启！");
            return;
        }
        // 1. 请求消息
        List<ChatCompletionRequest.Prompt> prompts = chatProcess.getMessages().stream()
                .map(entity -> ChatCompletionRequest.Prompt.builder()
                        .role(Role.user.getCode())
                        .content(entity.getContent())
                        .build())
                .collect(Collectors.toList());

        // 2. 封装参数
        ChatCompletionRequest request = new ChatCompletionRequest();
        request.setModel(Model.valueOf(ChatGLMModel.get(chatProcess.getModel()).name())); // chatGLM_6b_SSE、chatglm_lite、chatglm_lite_32k、chatglm_std、chatglm_pro
        request.setPrompt(prompts);


        try {
            chatGLMOpenAiSession.completions(request, new EventSourceListener() {
                StringBuilder stringBuilder = new StringBuilder();

                @Override
                public void onEvent(EventSource eventSource, @Nullable String id, @Nullable String type, String data) {
                    ChatCompletionResponse response = JSON.parseObject(data, ChatCompletionResponse.class);

                    // 发送信息
                    if (EventType.add.getCode().equals(type)){
                        try {
                            stringBuilder.append(response.getData());
                            emitter.send(response.getData());
                        } catch (Exception e) {
                            throw new ChatGPTException(e.getMessage());
                        }
                    }

                    // type 消息类型，add 增量，finish 结束，error 错误，interrupted 中断
                    if (EventType.finish.getCode().equals(type)) {
                        ChatCompletionResponse.Meta meta = JSON.parseObject(response.getMeta(), ChatCompletionResponse.Meta.class);
                        log.info("[输出结束] Tokens {}", JSON.toJSONString(meta));
                    }
                }

                @Override
                public void onClosed(EventSource eventSource) {
                    log.info("问答结果:" + stringBuilder.toString());
                    emitter.complete();
                }

            });
        } catch (Exception e) {
            log.info("gpt问答调用失败，原因:{}",e.getMessage());
            throw new RuntimeException(e);
        }


    }
}
