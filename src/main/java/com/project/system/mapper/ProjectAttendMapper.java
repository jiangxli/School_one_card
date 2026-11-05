package com.project.system.mapper;

import com.project.system.domain.ProjectAttend;
import java.util.List;	

/**
 * 考勤记录 数据层
 * 
 * @author administrator
 * @date 2020-05-27
 */
public interface ProjectAttendMapper {
	/**
     * 查询考勤记录信息
     * 
     * @param id 考勤记录ID
     * @return 考勤记录信息
     */
	public ProjectAttend selectProjectAttendById(Long id);
	
	/**
     * 查询考勤记录列表
     * 
     * @param projectAttend 考勤记录信息
     * @return 考勤记录集合
     */
	public List<ProjectAttend> selectProjectAttendList(ProjectAttend projectAttend);
	
	/**
     * 新增考勤记录
     * 
     * @param projectAttend 考勤记录信息
     * @return 结果
     */
	public int insertProjectAttend(ProjectAttend projectAttend);
	
	/**
     * 修改考勤记录
     * 
     * @param projectAttend 考勤记录信息
     * @return 结果
     */
	public int updateProjectAttend(ProjectAttend projectAttend);
	
	/**
     * 删除考勤记录
     * 
     * @param id 考勤记录ID
     * @return 结果
     */
	public int deleteProjectAttendById(Long id);
	
	/**
     * 批量删除考勤记录
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteProjectAttendByIds(String[] ids);
	
}