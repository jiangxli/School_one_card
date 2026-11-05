package com.project.system.mapper;

import com.project.system.domain.ProjectInfo;
import java.util.List;	

/**
 * 考勤项目 数据层
 * 
 * @author administrator
 * @date 2020-05-27
 */
public interface ProjectInfoMapper {
	/**
     * 查询考勤项目信息
     * 
     * @param projectId 考勤项目ID
     * @return 考勤项目信息
     */
	public ProjectInfo selectProjectInfoById(Long projectId);
	
	/**
     * 查询考勤项目列表
     * 
     * @param projectInfo 考勤项目信息
     * @return 考勤项目集合
     */
	public List<ProjectInfo> selectProjectInfoList(ProjectInfo projectInfo);
	
	/**
     * 新增考勤项目
     * 
     * @param projectInfo 考勤项目信息
     * @return 结果
     */
	public int insertProjectInfo(ProjectInfo projectInfo);
	
	/**
     * 修改考勤项目
     * 
     * @param projectInfo 考勤项目信息
     * @return 结果
     */
	public int updateProjectInfo(ProjectInfo projectInfo);
	
	/**
     * 删除考勤项目
     * 
     * @param projectId 考勤项目ID
     * @return 结果
     */
	public int deleteProjectInfoById(Long projectId);
	
	/**
     * 批量删除考勤项目
     * 
     * @param projectIds 需要删除的数据ID
     * @return 结果
     */
	public int deleteProjectInfoByIds(String[] projectIds);
	
}