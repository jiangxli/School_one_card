package com.project.system.service.impl;

import java.util.*;

import com.project.common.shiro.service.PasswordService;
import com.project.common.utils.TimeUuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.common.constant.UserConstants;
import com.project.common.utils.str.Convert;
import com.project.common.utils.str.StringUtils;
import com.project.system.domain.SysRole;
import com.project.system.domain.SysUser;
import com.project.system.mapper.SysRoleMapper;
import com.project.system.mapper.SysUserMapper;
import com.project.system.service.ISysUserService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户 业务层处理
 *
 * @author
 */
@Service
public class SysUserServiceImpl implements ISysUserService {
    @Autowired
    private SysUserMapper userMapper;
    @Autowired private com.project.system.service.WalletService walletService;
    @Autowired private org.springframework.jdbc.core.JdbcTemplate db;

    @Autowired
    private SysRoleMapper roleMapper;
    @Autowired
    private PasswordService passwordService;



    /**
     * 根据条件分页查询用户对象
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    public List<SysUser> selectUserList(SysUser user) {
        return userMapper.selectUserList(user);
    }

    public static void main(String[] args) {
        Properties properties = System.getProperties();
        Set<Map.Entry<Object,Object>> set  = properties.entrySet();
        System.out.println(set.size());
        for (Map.Entry<Object,Object> entry : set){
            System.out.println(entry.getKey()+"===="+entry.getValue());
        }
    }
    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByLoginName(String userName) {
        return userMapper.selectUserByLoginName(userName);
    }

    /**
     * 通过手机号码查询用户
     *
     * @param phoneNumber 用户名
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByPhoneNumber(String phoneNumber) {
        return userMapper.selectUserByPhoneNumber(phoneNumber);
    }

    /**
     * 通过邮箱查询用户
     *
     * @param email 邮箱
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByEmail(String email) {
        return userMapper.selectUserByEmail(email);
    }

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserById(Long userId) {
        return userMapper.selectUserById(userId);
    }

    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public int deleteUserById(Long userId) {
        return userMapper.deleteUserById(userId);
    }

    /**
     * 批量删除用户信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteUserByIds(String ids) throws Exception {
        Long[] userIds = Convert.toLongArray(ids);
        for (Long userId : userIds) {
            if (SysUser.isSuperAdmin(userId)) {
                throw new Exception("不允许删除超级管理员用户");
            }
        }
        return userMapper.deleteUserByIds(userIds);
    }

    /**
     * 新增保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertUser(SysUser user) {
        user.setRoleId(1812120000000002L);
        int rows = userMapper.insertUser(user);
        if(rows > 0){walletService.issue(user.getUserId());db.update("insert into sys_user_role(user_id,role_id) values(?,?)",user.getUserId(),1812120000000002L);}
        return rows;
    }

    /**
     * 修改保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUser(SysUser user) {
        SysUser stored=userMapper.selectUserById(user.getUserId());
        if(stored!=null && user.getLoginName()!=null && !stored.getLoginName().equals(user.getLoginName()))
            throw new IllegalArgumentException("学工号为钱包唯一标识，不允许直接修改");
        if(stored!=null)user.setRoleId(stored.getRoleId());
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户个人详细信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserInfo(SysUser user) {
        user.setRoleId(null);
        return userMapper.updateUser(user);
    }

    /**
     * 修改用户密码
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int resetUserPwd(SysUser user) {
        if("1".equals(user.getParams().get("resetPwdByEmail"))){
            SysUser info = userMapper.checkEmailUnique(user.getEmail());
            user.setUserId(info.getUserId());
            user.setPassword(passwordService.encryptPassword(info.getLoginName(), user.getPassword(), user.getSalt()));
        }
        if("1".equals(user.getParams().get("resetPwdByLoginName"))){
            SysUser info = userMapper.checkLoginNameUnique(user.getLoginName());
            user.setUserId(info.getUserId());
            user.setPassword(passwordService.encryptPassword(info.getLoginName(), user.getPassword(), user.getSalt()));
        }
        return updateUserInfo(user);
    }



    /**
     * 校验用户名称是否唯一
     *
     * @param loginName 用户名
     * @return
     */
    @Override
    public String checkLoginNameUnique(String loginName) {
        SysUser user = userMapper.checkLoginNameUnique(loginName);
        if (user != null) {
            return UserConstants.USER_NAME_NOT_UNIQUE;
        }
        return UserConstants.USER_NAME_UNIQUE;
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param user 用户名
     * @return
     */
    @Override
    public String checkPhoneUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkPhoneUnique(user.getPhonenumber());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.USER_PHONE_NOT_UNIQUE;
        }
        return UserConstants.USER_PHONE_UNIQUE;
    }

    /**
     * 校验email是否唯一
     *
     * @param user 用户名
     * @return
     */
    @Override
    public String checkEmailUnique(SysUser user) {
        Long userId = StringUtils.isNull(user.getUserId()) ? -1L : user.getUserId();
        SysUser info = userMapper.checkEmailUnique(user.getEmail());
        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
            return UserConstants.USER_EMAIL_NOT_UNIQUE;
        }
        return UserConstants.USER_EMAIL_UNIQUE;
    }

    /**
     * 查询用户所属角色组
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public String selectUserRoleGroup(Long userId) {
        List<SysRole> list = roleMapper.selectRolesByUserId(userId);
        StringBuffer idsStr = new StringBuffer();
        for (SysRole role : list) {
            idsStr.append(role.getRoleName()).append(",");
        }
        if (StringUtils.isNotEmpty(idsStr.toString())) {
            return idsStr.substring(0, idsStr.length() - 1);
        }
        return idsStr.toString();
    }

    /**
     * 查询用户所属岗位组
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public String selectUserPostGroup(Long userId) {
        return "";
    }
}
