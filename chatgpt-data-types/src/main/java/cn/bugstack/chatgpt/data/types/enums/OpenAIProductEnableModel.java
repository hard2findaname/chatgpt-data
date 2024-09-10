package cn.bugstack.chatgpt.data.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author atticus
 * @Date 19/8/2024 22:06
 * @description: openai模型是否可用
 */
@Getter
@AllArgsConstructor
public enum OpenAIProductEnableModel {
    CLOSE(0, "无效，已关闭"),
    OPEN(1,"有效，使用中"),
    ;

    private final Integer code;

    private final String info;

    public static OpenAIProductEnableModel get(Integer code){
        switch (code){
            case 1:
                return OpenAIProductEnableModel.OPEN;
            case 0:
            default:
                return OpenAIProductEnableModel.CLOSE;
        }
    }

}
