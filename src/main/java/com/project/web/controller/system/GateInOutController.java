package com.project.web.controller.system;

import java.util.List;

import com.project.common.utils.ExcelUtil;
import com.project.system.domain.SysUser;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.project.system.domain.GateInOut;
import com.project.system.service.IGateInOutService;
import com.project.web.controller.common.BaseController;
import com.project.common.base.TableDataInfo;
import com.project.common.base.AjaxResult;

/**
 * 门禁记录 信息操作处理
 *
 * @author administrator
 * @date 2020-05-27
 */
@Controller
@RequestMapping("/system/gateInOut" )
public class GateInOutController extends BaseController {
    private String prefix = "system/gateInOut" ;

    @Autowired
    private IGateInOutService gateInOutService;


    @RequiresPermissions("system:gateInOut:viewMy" )
    @GetMapping("/viewMy")
    public String gateInOutMy() {
        return prefix + "/gateInOutMy" ;
    }
    @PostMapping("/listMy" )
    @ResponseBody
    public TableDataInfo listMy(GateInOut gateInOut) {
        gateInOut.setUserId(getUserId());
        startPage();
        List<GateInOut> list = gateInOutService.selectGateInOutList(gateInOut);
        return getDataTable(list);
    }

    @RequiresPermissions("system:gateInOut:view" )
    @GetMapping()
    public String gateInOut() {
        return prefix + "/gateInOut" ;
    }


    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(GateInOut gateInOut) {
        List<GateInOut> list = gateInOutService.selectGateInOutList(gateInOut);
        ExcelUtil<GateInOut> util = new ExcelUtil<GateInOut>(GateInOut.class);
        return util.exportExcel(list, "门禁记录");
    }
    /**
     * 查询门禁记录列表
     */
    @PostMapping("/list" )
    @ResponseBody
    public TableDataInfo list(GateInOut gateInOut) {
        startPage();
        List<GateInOut> list = gateInOutService.selectGateInOutList(gateInOut);
        return getDataTable(list);
    }



    /**
     * 新增保存门禁记录
     */
    @PostMapping("/add" )
    @ResponseBody
    public AjaxResult addSave(GateInOut gateInOut) {
        gateInOut.setUserId(getUserId());
        return toAjax(gateInOutService.insertGateInOut(gateInOut));
    }

    /**
     * 查看门禁记录
     */
    @GetMapping("/detail/{id}" )
    public String detail(@PathVariable("id" ) Long id, ModelMap mmap) {
        GateInOut gateInOut =gateInOutService.selectGateInOutById(id);
        mmap.put("gateInOut" , gateInOut);
        return prefix + "/detail" ;
    }

    /**
     * 修改门禁记录
     */
    @GetMapping("/edit/{id}" )
    public String edit(@PathVariable("id" ) Long id, ModelMap mmap) {
        GateInOut gateInOut =gateInOutService.selectGateInOutById(id);
        mmap.put("gateInOut" , gateInOut);
        return prefix + "/edit" ;
    }

    /**
     * 修改保存门禁记录
     */
    @RequiresPermissions("system:gateInOut:edit" )
    @PostMapping("/edit" )
    @ResponseBody
    public AjaxResult editSave(GateInOut gateInOut) {
        return toAjax(gateInOutService.updateGateInOut(gateInOut));
    }

    /**
     * 删除门禁记录
     */
    @RequiresPermissions("system:gateInOut:remove" )
    @PostMapping("/remove" )
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(gateInOutService.deleteGateInOutByIds(ids));
    }

}
