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
import com.project.system.domain.BookBorrowRecord;
import com.project.system.service.IBookBorrowRecordService;
import com.project.web.controller.common.BaseController;
import com.project.common.base.TableDataInfo;
import com.project.common.base.AjaxResult;

/**
 * 借阅记录 信息操作处理
 *
 * @author administrator
 * @date 2020-02-13
 */
@Controller
@RequestMapping("/system/bookBorrowRecord" )
public class BookBorrowRecordController extends BaseController {
    private String prefix = "system/bookBorrowRecord" ;

    @Autowired
    private IBookBorrowRecordService bookBorrowRecordService;

    @RequiresPermissions("system:bookBorrowRecord:view" )
    @GetMapping()
    public String bookBorrowRecord() {
        return prefix + "/bookBorrowRecord" ;
    }

    /**
     * 查询借阅记录列表
     */
    @RequiresPermissions("system:bookBorrowRecord:list" )
    @PostMapping("/list" )
    @ResponseBody
    public TableDataInfo list(BookBorrowRecord bookBorrowRecord) {
        if(!this.getUser().isSuperAdmin()){
            bookBorrowRecord.setUserId(this.getUserId());
        }
        startPage();
        List<BookBorrowRecord> list = bookBorrowRecordService.selectBookBorrowRecordList(bookBorrowRecord);
        return getDataTable(list);
    }

    @PostMapping("/listForSelf" )
    @ResponseBody
    public TableDataInfo listForSelf(BookBorrowRecord bookBorrowRecord) {
        bookBorrowRecord.setUserId(this.getUserId());
        startPage();
        List<BookBorrowRecord> list = bookBorrowRecordService.selectBookBorrowRecordList(bookBorrowRecord);
        return getDataTable(list);
    }

    /**
     * 新增借阅记录
     */
    @GetMapping("/add" )
    public String add() {
        return prefix + "/add" ;
    }

    /**
     * 新增保存借阅记录
     */
    @RequiresPermissions("system:bookBorrowRecord:add" )
    @PostMapping("/add" )
    @ResponseBody
    public AjaxResult addSave(BookBorrowRecord bookBorrowRecord) {
        bookBorrowRecord.setUserId(this.getUserId());
        return toAjax(bookBorrowRecordService.insertBookBorrowRecord(bookBorrowRecord));
    }

    /**
     * 查看借阅记录
     */
    @GetMapping("/detail/{id}" )
    public String detail(@PathVariable("id" ) Long id, ModelMap mmap) {
        BookBorrowRecord bookBorrowRecord =bookBorrowRecordService.selectBookBorrowRecordById(id);
        if(bookBorrowRecord==null || (!getUser().isSuperAdmin()&&!getUserId().equals(bookBorrowRecord.getUserId())))
            throw new org.apache.shiro.authz.UnauthorizedException("不可访问他人借阅记录");
        mmap.put("bookBorrowRecord" , bookBorrowRecord);
        return prefix + "/detail" ;
    }


    /**
     * 还书
     */
    @RequiresPermissions("system:bookBorrowRecord:edit" )
    @PostMapping("/edit" )
    @ResponseBody
    public AjaxResult editSave(BookBorrowRecord bookBorrowRecord) {
        BookBorrowRecord stored=bookBorrowRecordService.selectBookBorrowRecordById(bookBorrowRecord.getId());
        if(stored==null || (!getUser().isSuperAdmin()&&!getUserId().equals(stored.getUserId())))
            return error("不可归还他人借阅记录");
        bookBorrowRecord.setStatus(1);
        return toAjax(bookBorrowRecordService.updateBookBorrowRecord(bookBorrowRecord));
    }

    /**
     * 删除借阅记录
     */
    @RequiresPermissions("system:bookBorrowRecord:remove" )
    @PostMapping("/remove" )
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(bookBorrowRecordService.deleteBookBorrowRecordByIds(ids));
    }

}
