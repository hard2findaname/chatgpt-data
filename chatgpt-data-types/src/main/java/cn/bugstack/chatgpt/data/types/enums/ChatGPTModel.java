package cn.bugstack.chatgpt.data.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author Atticus
 * @date 15/8/2024 19:35
 * @description:
 */
@Getter
@AllArgsConstructor
public enum ChatGPTModel {

    /** gpt-3.5-turbo */
    GPT_3_5_TURBO("gpt-3.5-turbo"),
    /**
     * 文生图
     */
    DALL_E_2("dall-e-2"), DALL_E_3("dall-e-3"),
    ;
    private final String code;
    private static ChatGPTModel getByCode(String code) {
        switch (code) {
            case "dall-e-2":
                return ChatGPTModel.DALL_E_2;
            case "dall-e-3":
                return ChatGPTModel.DALL_E_3;
            case "gpt-3.5-turbo":
            default:
                return ChatGPTModel.GPT_3_5_TURBO;
        }
    }

}
