package com.project.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.project.common.base.BaseEntity;
import java.util.Date;

/**
 * 考勤项目表 project_info
 *
 * @author administrator
 * @date 2020-05-27
 */
public class ProjectInfo extends BaseEntity{
	private static final long serialVersionUID = 1L;

	/** 考勤码 */
	private Long projectId;
	/** 考勤项目 */
	private String projectName;
	/** 考勤开始时间 */
	private String startTime;
	/** 考勤结束时间 */
	private String endTime;
	/** 项目介绍 */
	private String remark;
	/** 创建人 */
	private Long userId;
	private String userName;
	/*加入人数量*/
	private Integer userNum;

	public void setProjectId(Long projectId){
		this.projectId = projectId;
	}

	public Long getProjectId(){
		return projectId;
	}
	public void setProjectName(String projectName){
		this.projectName = projectName;
	}

	public String getProjectName(){
		return projectName;
	}
	public void setStartTime(String startTime){
		this.startTime = startTime;
	}

	public String getStartTime(){
		return startTime;
	}
	public void setEndTime(String endTime){
		this.endTime = endTime;
	}

	public String getEndTime(){
		return endTime;
	}
	public void setRemark(String remark){
		this.remark = remark;
	}

	public String getRemark(){
		return remark;
	}
	public void setUserId(Long userId){
		this.userId = userId;
	}

	public Long getUserId(){
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getUserNum() {
		return userNum;
	}

	public void setUserNum(Integer userNum) {
		this.userNum = userNum;
	}

	public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("projectId", getProjectId())
            .append("projectName", getProjectName())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("remark", getRemark())
            .append("userId", getUserId())
            .append("createTime", getCreateTime())
            .toString();
    }
}
