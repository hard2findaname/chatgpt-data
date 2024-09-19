package cn.bugstack.chatgpt.data.domain.openai.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author atticus
 * @Date 2024/09/13 15:55
 * @description:
 */
@Getter
@AllArgsConstructor
public enum GenerativeModelVO {
    TEXT("TEXT","文本"),
    IMAGES("IMAGES","图片"),
            ;

    private final String code;
    private final String info;
}
