package cn.bugstack.chatgpt.data.domain.weixin.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author Atticus
 * @date 15/8/2024 19:33
 * @description: 用户行为信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBehaviorMessageEntity {

    private String openId;
    private String fromUserName;
    private String msgType;
    private String content;
    private String event;
    private Date createTime;

}
