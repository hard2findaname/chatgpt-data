package cn.bugstack.chatgpt.data.domain.weixin.service;

import cn.bugstack.chatgpt.data.domain.weixin.model.entity.UserBehaviorMessageEntity;

/**
 * @Author Atticus
 * @date 15/8/2024 19:34
 * @description: 受理用户行为接口
 */
public interface IWeiXinBehaviorService {

    String acceptUserBehavior(UserBehaviorMessageEntity userBehaviorMessageEntity);

}
