package com.project.system.service.impl;

import com.project.common.config.Global;
import com.project.common.constant.UserConstants;
import com.project.common.shiro.service.PasswordService;
import com.project.common.utils.TimeUuidUtil;
import com.project.common.utils.str.StringUtils;
import com.project.common.utils.ShiroUtils;
import com.project.system.domain.ImportExcelResult;
import com.project.system.domain.SysUser;
import com.project.system.service.IExcelImportService;
import com.project.system.service.ISysUserService;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExcelImportServiceImpl implements IExcelImportService {

    @Autowired
    private ISysUserService userService;
    @Autowired
    private PasswordService passwordService;

    @Override
    @Transactional
    public ImportExcelResult saveData(List<Object> list, String importType) {
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        ImportExcelResult importExcelResult = new ImportExcelResult(0,result);
        for(int i=0;i<list.size();i++){
            switch (importType){
                case "1":
                    result.add(saveData((SysUser)list.get(i),i+1,importExcelResult));
                    break;

                default:
                    throw new RuntimeException();
            }
        }
        return importExcelResult;
    }

    /**
     * 保存用户
     *
     * @param user
     * @param rowNum
     * @return
     */
    public Map<String, String> saveData(SysUser user, int rowNum, ImportExcelResult importExcelResult) {
        StringBuilder msg = new StringBuilder("");
        //1.校验数据的合法性
        if(StringUtils.isEmpty(user.getLoginName())){
            msg.append("学工号不能为空！");
        }
        if(StringUtils.isEmpty(user.getUserName())){
            msg.append("用户名称不能为空！");
        }
        if(UserConstants.USER_NAME_NOT_UNIQUE.equals(userService.checkLoginNameUnique(user.getLoginName()))){
            msg.append("学工号系统内已经存在！");
        }
        if(!StringUtils.isEmpty(msg.toString())){
            return importExcelResultMap(user.getLoginName(),1,msg.toString(),rowNum);
        }
        //2.初始化默认值
        if(StringUtils.isEmpty(user.getSex())){
            user.setSex("2");
        }else if("男".equals(user.getSex())){
            user.setSex("0");
        }else if("女".equals(user.getSex())){
            user.setSex("1");
        }
        user.setSalt(ShiroUtils.randomSalt());
        user.setPassword("123456");
        user.setPassword(passwordService.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
        user.setCreateBy(ShiroUtils.getLoginName());
        user.setRoleId(Global.ROLE_ID_DEFAULT);
        user.setUserId(TimeUuidUtil.get16UUID());
        //3.保存
        userService.insertUser(user);
        importExcelResult.setSuccessNum(importExcelResult.getSuccessNum()+1);
        return importExcelResultMap(user.getLoginName(),0,"添加成功！",rowNum);
    }


    /**
     * 每行导入结果
     * @param name
     * @param code
     * @param msg
     * @return
     */
    private Map<String,String> importExcelResultMap(String name,Integer code,String msg,Integer rowNum){
        Map<String,String> map = new HashMap<String, String>();
        map.put("name",name);
        map.put("code",code.toString());
        map.put("msg",msg);
        map.put("rowNum",rowNum.toString());
        return map;
    }

}
