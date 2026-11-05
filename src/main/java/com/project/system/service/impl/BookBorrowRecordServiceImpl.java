package com.project.system.service.impl;

import java.util.List;
import java.util.Map;

import com.project.common.utils.DateUtils;
import com.project.common.utils.TimeUuidUtil;
import com.project.system.mapper.BookInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.system.mapper.BookBorrowRecordMapper;
import com.project.system.domain.BookBorrowRecord;
import com.project.system.service.IBookBorrowRecordService;
import com.project.common.utils.str.Convert;
import org.springframework.transaction.annotation.Transactional;

/**
 * 借阅记录 服务层实现
 *
 * @author administrator
 * @date 2020-02-13
 */
@Service
public class BookBorrowRecordServiceImpl implements IBookBorrowRecordService {
    @Autowired private org.springframework.jdbc.core.JdbcTemplate db;
    @Autowired
    private BookBorrowRecordMapper bookBorrowRecordMapper;

    @Autowired
    private BookInfoMapper bookInfoMapper;

    /**
     * 查询借阅记录信息
     *
     * @param id 借阅记录ID
     * @return 借阅记录信息
     */
    @Override
    public BookBorrowRecord selectBookBorrowRecordById(Long id) {
        return bookBorrowRecordMapper.selectBookBorrowRecordById(id);
    }

    /**
     * 查询借阅记录列表
     *
     * @param bookBorrowRecord 借阅记录信息
     * @return 借阅记录集合
     */
    @Override
    public List<BookBorrowRecord> selectBookBorrowRecordList(BookBorrowRecord bookBorrowRecord) {
        return bookBorrowRecordMapper.selectBookBorrowRecordList(bookBorrowRecord);
    }

    /**
     * 新增借阅记录
     *
     * @param bookBorrowRecord 借阅记录信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertBookBorrowRecord(BookBorrowRecord bookBorrowRecord) {
        bookBorrowRecord.setId(TimeUuidUtil.get16UUID());
        if (bookBorrowRecord.getUserId()==null || db.queryForList("select user_id from sys_user where user_id=? and status='0' and del_flag='0'",bookBorrowRecord.getUserId()).isEmpty())
            throw new IllegalArgumentException("借阅用户不存在或已停用");
        if (db.update("update book_info set book_num=book_num-1 where book_id=? and book_num>0",bookBorrowRecord.getBookId())!=1)
            throw new IllegalArgumentException("图书不存在或可借库存不足");
        bookBorrowRecord.setStatus(0);
        bookBorrowRecord.setBorrowTime(DateUtils.getTime());
        bookBorrowRecord.setReturnTime(null);
        return bookBorrowRecordMapper.insertBookBorrowRecord(bookBorrowRecord);
    }

    /**
     * 还书
     *
     * @param bookBorrowRecord 借阅记录信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateBookBorrowRecord(BookBorrowRecord bookBorrowRecord) {
        java.util.List<java.util.Map<String,Object>> rows=db.queryForList("select book_id from book_borrow_record where id=? for update",bookBorrowRecord.getId());
        if(rows.isEmpty()) throw new IllegalArgumentException("借阅记录不存在");
        int updated=db.update("update book_borrow_record set status=1,return_time=NOW() where id=? and status=0",bookBorrowRecord.getId());
        if(updated==0) return 1; // Repeated returns must not add inventory again.
        if(db.update("update book_info set book_num=book_num+1 where book_id=?",rows.get(0).get("book_id"))!=1)
            throw new IllegalArgumentException("图书信息缺失，归还已回滚");
        return updated;
    }

    /**
     * 删除借阅记录对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBookBorrowRecordByIds(String ids) {
        return bookBorrowRecordMapper.deleteBookBorrowRecordByIds(Convert.toStrArray(ids));
    }
    /**
     * 统计各个书的借阅次数
     * @return
     */
    public List<Map<String,Object>> countTimesGroupByBookId(BookBorrowRecord bookBorrowRecord){
        return bookBorrowRecordMapper.countTimesGroupByBookId(bookBorrowRecord);
    }

    @Override
    public List<Map<String, Object>> selectGroupNumTimesGroup(BookBorrowRecord bookBorrowRecord) {
        return bookBorrowRecordMapper.selectGroupNumTimesGroup(bookBorrowRecord);
    }

    /**
     * 查询平均借阅时长
     * @return
     */
    public List<Map<String,Object>> countDurationGroupByBookId(){
        return bookBorrowRecordMapper.countDurationGroupByBookId();
    }
}
