package com.project.system.service;

import com.project.system.domain.GateInOut;
import java.util.List;

/**
 * 门禁记录 服务层
 * 
 * @author administrator
 * @date 2020-05-27
 */
public interface IGateInOutService {
	/**
     * 查询门禁记录信息
     * 
     * @param id 门禁记录ID
     * @return 门禁记录信息
     */
	public GateInOut selectGateInOutById(Long id);
	
	/**
     * 查询门禁记录列表
     * 
     * @param gateInOut 门禁记录信息
     * @return 门禁记录集合
     */
	public List<GateInOut> selectGateInOutList(GateInOut gateInOut);
	
	/**
     * 新增门禁记录
     * 
     * @param gateInOut 门禁记录信息
     * @return 结果
     */
	public int insertGateInOut(GateInOut gateInOut);
	
	/**
     * 修改门禁记录
     * 
     * @param gateInOut 门禁记录信息
     * @return 结果
     */
	public int updateGateInOut(GateInOut gateInOut);
		
	/**
     * 删除门禁记录信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteGateInOutByIds(String ids);

	/**
	 * 查询用户上次门禁记录类型
	 * @param userId
	 * @return
	 */
	public Integer selectKindLastTime(Long userId);
	
}
