package com.project.system.service.impl;

import java.util.List;

import com.project.common.utils.TimeUuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.system.mapper.UserSecretProtectMapper;
import com.project.system.domain.UserSecretProtect;
import com.project.system.service.IUserSecretProtectService;
import com.project.common.utils.str.Convert;

/**
 * 密保问题 服务层实现
 *
 * @author administrator
 * @date 2020-03-18
 */
@Service
public class UserSecretProtectServiceImpl implements IUserSecretProtectService {
    @Autowired
    private UserSecretProtectMapper userSecretProtectMapper;

    /**
     * 查询密保问题信息
     *
     * @param id 密保问题ID
     * @return 密保问题信息
     */
    @Override
    public UserSecretProtect selectUserSecretProtectById(Long id) {
        return userSecretProtectMapper.selectUserSecretProtectById(id);
    }

    /**
     * 查询密保问题列表
     *
     * @param userSecretProtect 密保问题信息
     * @return 密保问题集合
     */
    @Override
    public List<UserSecretProtect> selectUserSecretProtectList(UserSecretProtect userSecretProtect) {
        return userSecretProtectMapper.selectUserSecretProtectList(userSecretProtect);
    }

    /**
     * 新增密保问题
     *
     * @param
     * @return 结果
     */
    @Override
    public int insertUserSecretProtect(String[] question,String[] answer,Long userId) {
        UserSecretProtect secretProtect = new UserSecretProtect();
        secretProtect.setUserId(userId);
        for(int i=0;i<question.length;i++){
            secretProtect.setId(TimeUuidUtil.get16UUID());
            secretProtect.setQuestion(question[i]);
            secretProtect.setAnswer(answer[i]);
            userSecretProtectMapper.insertUserSecretProtect(secretProtect);
        }
        return 1;
    }

    /**
     * 修改密保问题
     *
     * @param userSecretProtect 密保问题信息
     * @return 结果
     */
    @Override
    public int updateUserSecretProtect(UserSecretProtect userSecretProtect) {
        return userSecretProtectMapper.updateUserSecretProtect(userSecretProtect);
    }

    @Override
    public int deleteUserSecretProtectByUserId(Long userId) {
        return userSecretProtectMapper.deleteUserSecretProtectByUserId(userId);
    }

    /**
     * 删除密保问题对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteUserSecretProtectByIds(String ids) {
        return userSecretProtectMapper.deleteUserSecretProtectByIds(Convert.toStrArray(ids));
    }

}
