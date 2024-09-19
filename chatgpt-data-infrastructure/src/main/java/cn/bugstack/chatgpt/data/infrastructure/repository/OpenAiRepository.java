package cn.bugstack.chatgpt.data.infrastructure.repository;

import cn.bugstack.chatgpt.data.domain.openai.model.entity.UserAccountEntity;
import cn.bugstack.chatgpt.data.domain.openai.model.valobj.UserAccountStatusVO;
import cn.bugstack.chatgpt.data.domain.openai.repository.IOpenAiRepository;
import cn.bugstack.chatgpt.data.infrastructure.dao.IUserAccountDao;
import cn.bugstack.chatgpt.data.infrastructure.po.UserAccountPO;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @Author Atticus
 * @date 2024-07-14 15:13 15:13
 * @description: OpenAi 仓储服务
 */
@Repository
public class OpenAiRepository implements IOpenAiRepository {

    @Resource
    private IUserAccountDao userAccountDao;

    @Override
    public int subAccountQuota(String openid) {
        return userAccountDao.subAccountQuota(openid);
    }

    @Override
    public UserAccountEntity queryUserAccount(String openid) {
        UserAccountPO userAccountPO = userAccountDao.queryUserAccount(openid);
        if (null == userAccountPO) return null;

        UserAccountEntity userAccountEntity = new UserAccountEntity();
        userAccountEntity.setOpenid(userAccountPO.getOpenid());
        userAccountEntity.setTotalQuota(userAccountPO.getTotalQuota());
        userAccountEntity.setSurplusQuota(userAccountPO.getSurplusQuota());
        userAccountEntity.setUserAccountStatusVO(UserAccountStatusVO.get(userAccountPO.getStatus()));
        userAccountEntity.genModelTypes(userAccountPO.getModelTypes());
        return userAccountEntity;
    }


}
