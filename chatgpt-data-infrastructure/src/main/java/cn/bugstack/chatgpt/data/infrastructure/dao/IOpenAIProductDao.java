package cn.bugstack.chatgpt.data.infrastructure.dao;

import cn.bugstack.chatgpt.data.infrastructure.po.OpenAIProductPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author atticus
 * @Date 24/8/2024 14:33
 * @description:
 */
@Mapper
public interface IOpenAIProductDao {
    OpenAIProductPO queryProductByProductId(Integer productId);

    List<OpenAIProductPO> queryProductList();

}
