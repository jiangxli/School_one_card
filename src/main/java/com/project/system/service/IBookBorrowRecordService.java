package com.project.system.service;

import com.project.system.domain.BookBorrowRecord;
import java.util.List;
import java.util.Map;

/**
 * 借阅记录 服务层
 * 
 * @author administrator
 * @date 2020-02-13
 */
public interface IBookBorrowRecordService {
	/**
     * 查询借阅记录信息
     * 
     * @param id 借阅记录ID
     * @return 借阅记录信息
     */
	public BookBorrowRecord selectBookBorrowRecordById(Long id);
	
	/**
     * 查询借阅记录列表
     * 
     * @param bookBorrowRecord 借阅记录信息
     * @return 借阅记录集合
     */
	public List<BookBorrowRecord> selectBookBorrowRecordList(BookBorrowRecord bookBorrowRecord);
	
	/**
     * 新增借阅记录
     * 
     * @param bookBorrowRecord 借阅记录信息
     * @return 结果
     */
	public int insertBookBorrowRecord(BookBorrowRecord bookBorrowRecord);
	
	/**
     * 修改借阅记录
     * 
     * @param bookBorrowRecord 借阅记录信息
     * @return 结果
     */
	public int updateBookBorrowRecord(BookBorrowRecord bookBorrowRecord);
		
	/**
     * 删除借阅记录信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteBookBorrowRecordByIds(String ids);

	/**
	 * 统计各个书的借阅次数
	 * @return
	 */
	public List<Map<String,Object>> countTimesGroupByBookId(BookBorrowRecord bookBorrowRecord);

	/**
	 * 查询分别有几个次数
	 * @return
	 */
	public List<Map<String,Object>> selectGroupNumTimesGroup(BookBorrowRecord bookBorrowRecord);
	/**
	 * 查询平均借阅时长
	 * @return
	 */
	public List<Map<String,Object>> countDurationGroupByBookId();
}
