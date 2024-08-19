package cn.bugstack.chatgpt.data.domain.openai.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Title: MessageEntity
 * @Author Atticus
 * @Package cn.bugstack.chatgpt.data.domain.openai.model.entity
 * @date 15/8/2024 19:23
 * @description:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageEntity {

    private String role;
    private String content;
    private String name;

}
