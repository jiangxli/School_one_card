package com.project.system.domain;

import com.project.common.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.project.common.base.BaseEntity;
import java.util.Date;

/**
 * 门禁记录表 gate_in_out
 *
 * @author administrator
 * @date 2020-05-27
 */
public class GateInOut extends BaseEntity{
	private static final long serialVersionUID = 1L;

	/** 主键 */
	private Long id;
	/** 用户 */
	private Long userId;
	@Excel(name = "学工号")
	private String loginName;
	@Excel(name = "用户名称")
	private String userName;
	/** 操作时间 */
	@Excel(name = "时间")
	private String createTime;
	/** 1进入2外出 */
	@Excel(name = "类型",readConverterExp = "1=外出,2=进入")
	private Integer kind;

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
	public void setCreateTime(String createTime){
		this.createTime = createTime;
	}

	public String getCreateTime(){
		return createTime;
	}
	public void setKind(Integer kind){
		this.kind = kind;
	}

	public Integer getKind(){
		return kind;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("createTime", getCreateTime())
            .append("kind", getKind())
            .toString();
    }
}
