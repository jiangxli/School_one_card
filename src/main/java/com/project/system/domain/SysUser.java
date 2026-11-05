package com.project.system.domain;

import com.project.common.annotation.ExcelImport;
import com.project.common.config.Global;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;
import java.util.List;

import com.project.common.annotation.Excel;
import com.project.common.base.BaseEntity;

/**
 * 用户对象 sys_user
 *
 * @author
 */
public class SysUser extends BaseEntity {
    private String assignedRoleNames;
    public String getAssignedRoleNames(){return assignedRoleNames;}
    public void setAssignedRoleNames(String value){assignedRoleNames=value;}

    private static final long serialVersionUID = 1L;
    private String cardNo;
    private Integer cardWriteCount;
    public String getCardNo(){return cardNo;}
    public void setCardNo(String value){cardNo=value;}
    public Integer getCardWriteCount(){return cardWriteCount;}
    public void setCardWriteCount(Integer value){cardWriteCount=value;}


    /**
     * 用户ID
     */
    @Excel(name = "用户序号")
    private Long userId;

    /**
     * 部门父ID
     */
    private Long parentId;

    /**
     * 学工号
     */
    @Excel(name = "学工号")
    @ExcelImport(colNum = 0,colName = "学工号")
    private String loginName;

    /**
     * 用户名称
     */
    @Excel(name = "用户名称")
    @ExcelImport(colNum = 1,colName = "用户名称")
    private String userName;



    /**
     * 用户性别
     */
    @Excel(name = "用户性别",readConverterExp = "0=男,1=女,2=未知")
    @ExcelImport(colNum = 2,colName = "性别（男/女/未知）")
    private String sex;
    /**
     * 用户邮箱
     */
    @Excel(name = "用户邮箱")
    @ExcelImport(colNum = 3,colName = "用户邮箱")
    private String email;

    /**
     * 手机号码
     */
    @Excel(name = "手机号码")
    @ExcelImport(colNum = 4,colName = "手机号码")
    private String phonenumber;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 密码
     */
    private String password;

    /**
     * 盐加密
     */
    private String salt;

    /**
     * 帐号状态（0正常 1停用）
     */
    @Excel(name = "帐号状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    /**
     * 最后登陆IP
     */
    @Excel(name = "最后登陆IP")
    private String loginIp;

    /**
     * 最后登陆时间
     */
    @Excel(name = "最后登陆时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss", type = Excel.Type.EXPORT)
    private Date loginDate;


    /**
     * 角色组
     */
    private Long roleId;
    private SysRole role;

    private String userType;

    /** 出生日期 */
    @ExcelImport(colNum = 5,colName = "出生日期(格式yyyy-MM-dd)")
    private String birthday;
    /** 民族 */
    @ExcelImport(colNum = 6,colName = "民族")
    private String mz;
    /** 院系 */
    @ExcelImport(colNum = 7,colName = "院系")
    private String department;
    /** 住址 */
    @ExcelImport(colNum = 8,colName = "住址")
    private String address;
    /** 身份 */
    @ExcelImport(colNum = 9,colName = "身份(专科生、本科生、研究生、教师/职工)")
    private String identity;
    /** 学籍状态 */
    @ExcelImport(colNum = 10,colName = "学籍状态(学生填)")
    private String xjzt;
    /** 专业 */
    @ExcelImport(colNum = 11,colName = "专业(学生填)")
    private String profession;
    /** 班级 */
    @ExcelImport(colNum = 12,colName = "班级(学生填)")
    private String classname;
    /** 学制 */
    @ExcelImport(colNum = 13,colName = "学制(学生填)")
    private String studyyear;
    /** 考勤码 */
    private Long projectId;
    /** 加入考勤日期 */
    private String projectJoinTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isSuperAdmin() {
        return isSuperAdmin(this.userId);
    }

    public static boolean isSuperAdmin(Long userId) {
        return userId != null && userId==Global.TOP_ADMIN_ID;
    }



    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public SysRole getRole() {
        return role;
    }

    public void setRole(SysRole role) {
        this.role = role;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getMz() {
        return mz;
    }

    public void setMz(String mz) {
        this.mz = mz;
    }

    public String getXjzt() {
        return xjzt;
    }

    public void setXjzt(String xjzt) {
        this.xjzt = xjzt;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getStudyyear() {
        return studyyear;
    }

    public void setStudyyear(String studyyear) {
        this.studyyear = studyyear;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectJoinTime() {
        return projectJoinTime;
    }

    public void setProjectJoinTime(String projectJoinTime) {
        this.projectJoinTime = projectJoinTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("userId", getUserId())
                .append("loginName", getLoginName())
                .append("userName", getUserName())
                .append("email", getEmail())
                .append("phonenumber", getPhonenumber())
                .append("sex", getSex())
                .append("avatar", getAvatar())
                .append("password", getPassword())
                .append("salt", getSalt())
                .append("status", getStatus())
                .append("delFlag", getDelFlag())
                .append("loginIp", getLoginIp())
                .append("loginDate", getLoginDate())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
