package com.project.web.controller.system;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.project.system.domain.BookInfo;
import com.project.system.service.IBookInfoService;
import com.project.web.controller.common.BaseController;
import com.project.common.base.TableDataInfo;
import com.project.common.base.AjaxResult;

/**
 * 图书管理 信息操作处理
 *
 * @author administrator
 * @date 2020-02-13
 */
@Controller
@RequestMapping("/system/bookInfo" )
public class BookInfoController extends BaseController {
    private String prefix = "system/bookInfo" ;

    @Autowired
    private IBookInfoService bookInfoService;

    @RequiresPermissions("system:bookInfo:view" )
    @GetMapping()
    public String bookInfo() {
        return prefix + "/bookInfo" ;
    }

    /**
     * 查询图书管理列表
     */
    @RequiresPermissions("system:bookInfo:list" )
    @PostMapping("/list" )
    @ResponseBody
    public TableDataInfo list(BookInfo bookInfo) {
        startPage();
        List<BookInfo> list = bookInfoService.selectBookInfoList(bookInfo);
        return getDataTable(list);
    }

    /**
     * 新增图书管理
     */
    @GetMapping("/add" )
    public String add() {
        return prefix + "/add" ;
    }

    /**
     * 新增保存图书管理
     */
    @RequiresPermissions("system:bookInfo:add" )
    @PostMapping("/add" )
    @ResponseBody
    public AjaxResult addSave(BookInfo bookInfo) {
        return toAjax(bookInfoService.insertBookInfo(bookInfo));
    }

    /**
     * 查看图书管理
     */
    @GetMapping("/detail/{bookId}" )
    public String detail(@PathVariable("bookId" ) Long bookId, ModelMap mmap) {
        BookInfo bookInfo =bookInfoService.selectBookInfoById(bookId);
        mmap.put("bookInfo" , bookInfo);
        return prefix + "/detail" ;
    }

    /**
     * 修改图书管理
     */
    @GetMapping("/edit/{bookId}" )
    public String edit(@PathVariable("bookId" ) Long bookId, ModelMap mmap) {
        BookInfo bookInfo =bookInfoService.selectBookInfoById(bookId);
        mmap.put("bookInfo" , bookInfo);
        return prefix + "/edit" ;
    }

    /**
     * 修改保存图书管理
     */
    @RequiresPermissions("system:bookInfo:edit" )
    @PostMapping("/edit" )
    @ResponseBody
    public AjaxResult editSave(BookInfo bookInfo) {
        return toAjax(bookInfoService.updateBookInfo(bookInfo));
    }

    /**
     * 删除图书管理
     */
    @RequiresPermissions("system:bookInfo:remove" )
    @PostMapping("/remove" )
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(bookInfoService.deleteBookInfoByIds(ids));
    }

}
