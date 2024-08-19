package cn.bugstack.chatgpt.data.domain.openai.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Title: LogicCheckTypeVO
 * @Author Atticus
 * @Package cn.bugstack.chatgpt.data.domain.openai.model.valobj
 * @date 15/8/2024 19:27
 * @description: 逻辑校验类型，值对象
 */
@Getter
@AllArgsConstructor
public enum LogicCheckTypeVO {

    SUCCESS("0000", "校验通过"),
    REFUSE("0001","校验拒绝"),
            ;

    private final String code;
    private final String info;

}
