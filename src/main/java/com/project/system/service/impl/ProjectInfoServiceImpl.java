package com.project.system.service.impl;

import java.util.List;

import com.project.common.utils.TimeUuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.system.mapper.ProjectInfoMapper;
import com.project.system.domain.ProjectInfo;
import com.project.system.service.IProjectInfoService;
import com.project.common.utils.str.Convert;

/**
 * 考勤项目 服务层实现
 *
 * @author administrator
 * @date 2020-05-27
 */
@Service
public class ProjectInfoServiceImpl implements IProjectInfoService {
    @Autowired
    private ProjectInfoMapper projectInfoMapper;

    /**
     * 查询考勤项目信息
     *
     * @param projectId 考勤项目ID
     * @return 考勤项目信息
     */
    @Override
    public ProjectInfo selectProjectInfoById(Long projectId) {
        return projectInfoMapper.selectProjectInfoById(projectId);
    }

    /**
     * 查询考勤项目列表
     *
     * @param projectInfo 考勤项目信息
     * @return 考勤项目集合
     */
    @Override
    public List<ProjectInfo> selectProjectInfoList(ProjectInfo projectInfo) {
        return projectInfoMapper.selectProjectInfoList(projectInfo);
    }

    /**
     * 新增考勤项目
     *
     * @param projectInfo 考勤项目信息
     * @return 结果
     */
    @Override
    public int insertProjectInfo(ProjectInfo projectInfo) {
        projectInfo.setProjectId(TimeUuidUtil.get16UUID());
        return projectInfoMapper.insertProjectInfo(projectInfo);
    }

    /**
     * 修改考勤项目
     *
     * @param projectInfo 考勤项目信息
     * @return 结果
     */
    @Override
    public int updateProjectInfo(ProjectInfo projectInfo) {
        return projectInfoMapper.updateProjectInfo(projectInfo);
    }

    /**
     * 删除考勤项目对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteProjectInfoByIds(String ids) {
        return projectInfoMapper.deleteProjectInfoByIds(Convert.toStrArray(ids));
    }

}
