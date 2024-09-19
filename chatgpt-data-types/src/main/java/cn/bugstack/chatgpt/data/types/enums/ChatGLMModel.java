package cn.bugstack.chatgpt.data.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author atticus
 * @Date 2024/09/12 00:19
 * @description:
 */
@Getter
@AllArgsConstructor

public enum ChatGLMModel {
    CHATGLM_6B_SSE("chatGLM_6b_SSE"),
    CHATGLM_LITE("chatglm_lite"),
    CHATGLM_LITE_32K("chatglm_lite_32k"),
    CHATGLM_STD("chatglm_std"),
    CHATGLM_PRO("chatglm_pro"),

    ;
    private final String code;

    public static ChatGLMModel get(String code){
        switch (code){
            case "chatglm_lite":
                return ChatGLMModel.CHATGLM_LITE;
            case "chatglm_lite_32k":
                return ChatGLMModel.CHATGLM_LITE_32K;
            case "chatglm_std":
                return ChatGLMModel.CHATGLM_STD;
            case "chatglm_pro":
                return ChatGLMModel.CHATGLM_PRO;
            case "chatGLM_6b_SSE":
            default:
                return ChatGLMModel.CHATGLM_6B_SSE;
        }
    }


}
