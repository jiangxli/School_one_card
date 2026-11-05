package com.project.system.domain;

import com.project.common.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.project.common.base.BaseEntity;
import java.util.Date;

/**
 * 考勤记录表 project_attend
 *
 * @author administrator
 * @date 2020-05-27
 */
public class ProjectAttend extends BaseEntity{
	private static final long serialVersionUID = 1L;

	/** 主键 */
	private Long id;
	/** 考勤人 */
	private Long userId;
	@Excel(name = "考勤人学工号")
	private String loginName;
	@Excel(name = "考勤人")
	private String userName;
	/** 0正常1迟到 */
	@Excel(name = "考勤状态",readConverterExp = "0=正常,1=迟到")
	private Integer status;
	/** 考勤项目 */
	private Long projectId;
	@Excel(name = "考勤项目")
	private String projectName;
	@Excel(name = "考勤时间")
	private String createTime;

	public void setId(Long id){
		this.id = id;
	}

	public Long getId(){
		return id;
	}
	public void setUserId(Long userId){
		this.userId = userId;
	}

	public Long getUserId(){
		return userId;
	}
	public void setStatus(Integer status){
		this.status = status;
	}

	public Integer getStatus(){
		return status;
	}
	public void setProjectId(Long projectId){
		this.projectId = projectId;
	}

	public Long getProjectId(){
		return projectId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@Override
	public String getCreateTime() {
		return createTime;
	}

	@Override
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("projectId", getProjectId())
            .toString();
    }
}
