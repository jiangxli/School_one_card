package com.project.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.project.common.base.BaseEntity;

/**
 * 密保问题表 user_secret_protect
 *
 * @author administrator
 * @date 2020-03-18
 */
public class UserSecretProtect extends BaseEntity{
	private static final long serialVersionUID = 1L;

	/** 编号 */
	private Long id;
	/** 问题 */
	private String question;
	/** 回答 */
	private String answer;
	/** 用户 */
	private Long userId;

	public void setId(Long id){
		this.id = id;
	}

	public Long getId(){
		return id;
	}
	public void setQuestion(String question){
		this.question = question;
	}

	public String getQuestion(){
		return question;
	}
	public void setAnswer(String answer){
		this.answer = answer;
	}

	public String getAnswer(){
		return answer;
	}
	public void setUserId(Long userId){
		this.userId = userId;
	}

	public Long getUserId(){
		return userId;
	}

    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("question", getQuestion())
            .append("answer", getAnswer())
            .append("userId", getUserId())
            .toString();
    }
}
