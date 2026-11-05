package com.project.system.mapper;

import com.project.system.domain.BookInfo;
import java.util.List;	

/**
 * 图书管理 数据层
 * 
 * @author administrator
 * @date 2020-02-13
 */
public interface BookInfoMapper {
	/**
     * 查询图书管理信息
     * 
     * @param bookId 图书管理ID
     * @return 图书管理信息
     */
	public BookInfo selectBookInfoById(Long bookId);
	
	/**
     * 查询图书管理列表
     * 
     * @param bookInfo 图书管理信息
     * @return 图书管理集合
     */
	public List<BookInfo> selectBookInfoList(BookInfo bookInfo);
	
	/**
     * 新增图书管理
     * 
     * @param bookInfo 图书管理信息
     * @return 结果
     */
	public int insertBookInfo(BookInfo bookInfo);
	
	/**
     * 修改图书管理
     * 
     * @param bookInfo 图书管理信息
     * @return 结果
     */
	public int updateBookInfo(BookInfo bookInfo);
	
	/**
     * 删除图书管理
     * 
     * @param bookId 图书管理ID
     * @return 结果
     */
	public int deleteBookInfoById(Long bookId);
	
	/**
     * 批量删除图书管理
     * 
     * @param bookIds 需要删除的数据ID
     * @return 结果
     */
	public int deleteBookInfoByIds(String[] bookIds);

	/**
	 * 还书数量加1
	 * @param bookId
	 * @return
	 */
	public int updateBookNumAdd(Long bookId);

	/**
	 * 借书数量减1
	 * @param bookId
	 * @return
	 */
	public int updateBookNumMinus(Long bookId);
}