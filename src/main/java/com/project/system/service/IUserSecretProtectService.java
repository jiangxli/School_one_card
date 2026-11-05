package com.project.system.service;

import com.project.system.domain.UserSecretProtect;
import java.util.List;

/**
 * 密保问题 服务层
 * 
 * @author administrator
 * @date 2020-03-18
 */
public interface IUserSecretProtectService {
	/**
     * 查询密保问题信息
     * 
     * @param id 密保问题ID
     * @return 密保问题信息
     */
	public UserSecretProtect selectUserSecretProtectById(Long id);
	
	/**
     * 查询密保问题列表
     * 
     * @param userSecretProtect 密保问题信息
     * @return 密保问题集合
     */
	public List<UserSecretProtect> selectUserSecretProtectList(UserSecretProtect userSecretProtect);
	
	/**
     * 新增密保问题
     * 
     * @param
     * @return 结果
     */
	public int insertUserSecretProtect(String[] question, String[] answer, Long userId);
	
	/**
     * 修改密保问题
     * 
     * @param userSecretProtect 密保问题信息
     * @return 结果
     */
	public int updateUserSecretProtect(UserSecretProtect userSecretProtect);

	/**
	 * 删除密保问题
	 *
	 * @param userId 用户ID
	 * @return 结果
	 */
	public int deleteUserSecretProtectByUserId(Long userId);
	/**
     * 删除密保问题信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteUserSecretProtectByIds(String ids);
	
}
