package com.project.system.service.impl;

import java.util.List;

import com.project.common.utils.TimeUuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.system.mapper.ProjectAttendMapper;
import com.project.system.domain.ProjectAttend;
import com.project.system.service.IProjectAttendService;
import com.project.common.utils.str.Convert;

/**
 * 考勤记录 服务层实现
 *
 * @author administrator
 * @date 2020-05-27
 */
@Service
public class ProjectAttendServiceImpl implements IProjectAttendService {
    @Autowired
    private ProjectAttendMapper projectAttendMapper;

    /**
     * 查询考勤记录信息
     *
     * @param id 考勤记录ID
     * @return 考勤记录信息
     */
    @Override
    public ProjectAttend selectProjectAttendById(Long id) {
        return projectAttendMapper.selectProjectAttendById(id);
    }

    /**
     * 查询考勤记录列表
     *
     * @param projectAttend 考勤记录信息
     * @return 考勤记录集合
     */
    @Override
    public List<ProjectAttend> selectProjectAttendList(ProjectAttend projectAttend) {
        return projectAttendMapper.selectProjectAttendList(projectAttend);
    }

    /**
     * 新增考勤记录
     *
     * @param projectAttend 考勤记录信息
     * @return 结果
     */
    @Override
    public int insertProjectAttend(ProjectAttend projectAttend) {
        projectAttend.setId(TimeUuidUtil.get16UUID());
        return projectAttendMapper.insertProjectAttend(projectAttend);
    }

    /**
     * 修改考勤记录
     *
     * @param projectAttend 考勤记录信息
     * @return 结果
     */
    @Override
    public int updateProjectAttend(ProjectAttend projectAttend) {
        return projectAttendMapper.updateProjectAttend(projectAttend);
    }

    /**
     * 删除考勤记录对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteProjectAttendByIds(String ids) {
        return projectAttendMapper.deleteProjectAttendByIds(Convert.toStrArray(ids));
    }

}
