package cn.bugstack.chatgpt.data.domain.openai.service.channel.model.impl;

import cn.bugstack.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import cn.bugstack.chatgpt.data.domain.openai.service.channel.model.IGenerativeModelService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;

/**
 * @Author atticus
 * @Date 2024/09/13 15:55
 * @description:
 */
@Service
public class ImageGenerativeModelServiceImpl implements IGenerativeModelService {
    @Override
    public void doMessageResponse(ChatProcessAggregate chatProcess, ResponseBodyEmitter responseBodyEmitter) throws IOException {


    }
}
