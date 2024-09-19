package cn.bugstack.chatgpt.data.domain.openai.model.aggregates;

import cn.bugstack.chatgpt.data.domain.openai.model.entity.MessageEntity;
import cn.bugstack.chatgpt.data.domain.openai.model.valobj.GenerativeModelVO;
import cn.bugstack.chatgpt.data.types.common.Constants;
import cn.bugstack.chatgpt.data.types.enums.ChatGPTModel;
import cn.bugstack.chatgpt.data.types.enums.channel.OpenAiChannel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Title: ChatProcessAggregate
 * @Author  Atticus
 * @Package cn.bugstack.chatgpt.data.domain.openai.model.aggregates
 * @date  15/8/2024 19:24
 * @description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatProcessAggregate {

    /** 用户ID */
    private String openid;
    /** 默认模型 */
    private String model = ChatGPTModel.GPT_3_5_TURBO.getCode();
    /** 问题描述 */
    private List<MessageEntity> messages;

    public boolean isWhiteList(String whiteListStr) {
        String[] whiteList = whiteListStr.split(Constants.SPLIT);
        for (String whiteOpenid : whiteList) {
            if (whiteOpenid.equals(openid)) return true;
        }
        return false;
    }
    public OpenAiChannel getChannel(){
        return OpenAiChannel.getChannel(this.model);
    }

    public GenerativeModelVO getGenerativeModelVO() {
        switch (this.model) {
            case "dall-e-2":
            case "dall-e-3":
                return GenerativeModelVO.IMAGES;
            default:
                return GenerativeModelVO.TEXT;
        }
    }
}
