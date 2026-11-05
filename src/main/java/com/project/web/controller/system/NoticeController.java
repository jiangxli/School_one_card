package com.project.web.controller.system;

import java.util.List;

import com.project.common.config.ServerConfig;
import com.project.common.utils.file.FileUploadUtils;
import com.project.web.controller.common.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.project.common.base.AjaxResult;
import com.project.common.utils.ShiroUtils;
import com.project.common.base.TableDataInfo;
import com.project.system.domain.SysNotice;
import com.project.system.service.ISysNoticeService;
import org.springframework.web.multipart.MultipartFile;

/**
 * 公告 信息操作处理
 *
 * @author
 */
@Controller
@RequestMapping("/system/notice")
public class NoticeController extends BaseController {
    private String prefix = "system/notice";
    @Autowired
    private ServerConfig serverConfig;

    @Autowired
    private ISysNoticeService noticeService;

    @RequiresPermissions("system:notice:view")
    @GetMapping()
    public String notice() {
        return prefix + "/notice";
    }

    /**
     * 查询公告列表
     */
    @RequiresPermissions("system:notice:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysNotice notice) {
        startPage();
        List<SysNotice> list = noticeService.selectNoticeList(notice);
        return getDataTable(list);
    }

    /**
     * 新增公告
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存公告
     */
    @RequiresPermissions("system:notice:add")
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysNotice notice, MultipartFile file) {
        try{
            if(file != null){
                notice.setFileUrl(FileUploadUtils.upload(file,null));
            }
            notice.setCreateBy(ShiroUtils.getLoginName());
            return toAjax(noticeService.insertNotice(notice));
        }catch (Exception e){
            e.printStackTrace();
            return error();
        }

    }
    /**
     * 修改公告
     */
    @GetMapping("/edit/{noticeId}")
    public String edit(@PathVariable("noticeId") Long noticeId, ModelMap mmap) {
        mmap.put("notice", noticeService.selectNoticeById(noticeId));
        return prefix + "/edit";
    }

    /**
     * 修改保存公告
     */
    @RequiresPermissions("system:notice:edit")
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysNotice notice, MultipartFile file) {
        try{
            if(file != null){
                notice.setFileUrl(FileUploadUtils.upload(file,null));
            }
            notice.setUpdateBy(ShiroUtils.getLoginName());
            return toAjax(noticeService.updateNotice(notice));
        }catch (Exception e){
            e.printStackTrace();
            return error();
        }
    }

    /**
     * 删除公告
     */
    @RequiresPermissions("system:notice:remove")
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(noticeService.deleteNoticeByIds(ids));
    }

    /**
     * 查看公告
     */
    @GetMapping("/detail/{noticeId}")
    public String detail(@PathVariable("noticeId") Long noticeId, ModelMap mmap) {
        mmap.put("notice", noticeService.selectNoticeById(noticeId));
        return prefix + "/detail";
    }
    /**
     * 首页查询公告列表
     */
    @PostMapping("/viewList")
    @ResponseBody
    public TableDataInfo viewList(SysNotice notice) {
        notice.setStatus("0");
        notice.getParams().put("userId",this.getUserId());
        startPage();
        List<SysNotice> list = noticeService.selectNoticeList(notice);
        return getDataTable(list);
    }

    @GetMapping("/setUser/{noticeId}")
    public String setUser(@PathVariable("noticeId") Long noticeId, ModelMap mmap) {
        mmap.put("noticeId", noticeId);
        return prefix + "/setUser";
    }
    @PostMapping("/setUser")
    @ResponseBody
    public AjaxResult setUser(Long[] userId,Long noticeId) {
        return toAjax(noticeService.setUser(noticeId,userId));
    }

}
