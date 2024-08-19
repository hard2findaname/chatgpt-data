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

    ;
    private final String code;

}
