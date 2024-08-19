package cn.bugstack.chatgpt.data.domain.openai.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Title: UserAccountStatusVO
 * @Author Atticus
 * @Package cn.bugstack.chatgpt.data.domain.openai.model.valobj
 * @date 15/8/2024 19:27
 * @description: 账户状态
 */
@Getter
@AllArgsConstructor
public enum UserAccountStatusVO {

    AVAILABLE(0, "可用"),
    FREEZE(1,"冻结"),
    ;

    private final Integer code;
    private final String info;

    public static UserAccountStatusVO get(Integer code){
        switch (code){
            case 0:
                return UserAccountStatusVO.AVAILABLE;
            case 1:
                return UserAccountStatusVO.FREEZE;
            default:
                return UserAccountStatusVO.AVAILABLE;
        }
    }

}
