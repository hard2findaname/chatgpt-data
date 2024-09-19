package cn.bugstack.chatgpt.data.types.enums.channel;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author atticus
 * @Date 2024/09/12 00:18
 * @description:
 */
@Getter
@AllArgsConstructor
public enum OpenAiChannel {
    ChatGLM("ChatGLM"),
    ChatGPT("ChatGPT"),

            ;
    private final String code;

    public static OpenAiChannel getChannel(String model) {
        if (model.toLowerCase().contains("gpt")) return OpenAiChannel.ChatGPT;
        if (model.toLowerCase().contains("glm")) return OpenAiChannel.ChatGLM;
        return null;
    }

}
