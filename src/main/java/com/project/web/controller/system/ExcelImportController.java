package com.project.web.controller.system;

import com.project.common.base.AjaxResult;
import com.project.common.utils.ExcelUtil;
import com.project.web.controller.common.BaseController;
import com.project.system.domain.SysUser;
import com.project.system.service.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户信息
 *
 * @author
 */
@Controller
@RequestMapping("/system/excelImport")
public class ExcelImportController extends BaseController {
    private String prefix = "system/excelImport";

    @Autowired
    private IExcelImportService excelImportService;

    @RequiresPermissions("system:excelImport:view")
    @GetMapping()
    public String user() {
        return prefix + "/excelImport";
    }

    private static Map<String,String> excelNameMap = new HashMap<String,String>();
    static {
        excelNameMap.put("1","用户导入");
        excelNameMap.put("2","部门导入");
    }
    @PostMapping("/downloadExcelModel")
    @ResponseBody
    public  AjaxResult downloadExcelModel(String importType){
        ExcelUtil excelUtil = excelUtilObj(importType);
        return excelUtil.exportExcelModel(excelNameMap.get(importType));
    }
    @PostMapping("/save")
    @ResponseBody
    public AjaxResult save(MultipartFile excelFile, String importType) {
        try {
            ExcelUtil excelUtil = excelUtilObj(importType);
            List<Object> data = excelUtil.importExcel(excelFile.getInputStream());
            return success(excelImportService.saveData(data,importType));
        } catch (Exception e) {
            e.printStackTrace();
            return error("请选择正确的excel文件！");
        }
    }

    /**
     * 创建excel工具对象
     * @param type
     * @return
     */
    private static ExcelUtil excelUtilObj(String type){
        ExcelUtil excelUtil = null;
        switch (type){
            case "1":
                excelUtil= new ExcelUtil(SysUser.class);
                break;
            default:
                throw new RuntimeException("不支持的导入数据类型！");
        }
        return excelUtil;
    }
}