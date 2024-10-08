package cn.bugstack.chatgpt.data.infrastructure.dao;

import cn.bugstack.chatgpt.data.infrastructure.po.UserAccountPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author
 * @description 用户账户DAO
 * @create 2024-8-23 16:39
 */
@Mapper
public interface IUserAccountDao {

    int subAccountQuota(String openid);

    UserAccountPO queryUserAccount(String openid);

    void insert(UserAccountPO userAccountPOReq);

    int addAccountQuota(UserAccountPO userAccountPOReq);


}
