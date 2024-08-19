package cn.bugstack.chatgpt.data.domain.weixin.service;

/**
 * @Author Atticus
 * @date 15/8/2024 19:34
 * @description: 验签接口
 */
public interface IWeiXinValidateService {

    boolean checkSign(String signature, String timestamp, String nonce);

}
