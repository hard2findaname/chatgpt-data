package cn.bugstack.chatgpt.data.trigger.http.dto;

import lombok.Data;

/**
 * @Author Atticus
 * @date 15/8/2024 19:34
 * @description:
 */
@Data
public class ChoiceEntity {

    /** stream = true 请求参数里返回的属性是 delta */
    private MessageEntity delta;
    /** stream = false 请求参数里返回的属性是 delta */
    private MessageEntity message;

}
