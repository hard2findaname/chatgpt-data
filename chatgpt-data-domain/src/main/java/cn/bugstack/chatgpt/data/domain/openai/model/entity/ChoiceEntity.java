package cn.bugstack.chatgpt.data.domain.openai.model.entity;

import lombok.Data;

/**
 * @Title: ChoiceEntity
 * @Author  Atticus
 * @Package cn.bugstack.chatgpt.data.domain.openai.model.entity
 * @date  15/8/2024 19:24
 * @description:
 */
@Data
public class ChoiceEntity {

    /** stream = true 请求参数里返回的属性是 delta */
    private MessageEntity delta;
    /** stream = false 请求参数里返回的属性是 delta */
    private MessageEntity message;

}
