package cn.bugstack.chatgpt.data.domain.openai.model.entity;

import cn.bugstack.chatgpt.data.domain.openai.model.valobj.LogicCheckTypeVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Title: RuleLogicEntity
 * @Author  Atticus
 * @Package cn.bugstack.chatgpt.data.domain.openai.model.entity
 * @date  15/8/2024 19:23
 * @description: 过滤逻辑实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RuleLogicEntity<T> {

    private LogicCheckTypeVO type;
    private String info;
    private T data;

}
