package com.project.system.service;

import com.project.system.domain.BookInfo;
import java.util.List;

/**
 * 图书管理 服务层
 * 
 * @author administrator
 * @date 2020-02-13
 */
public interface IBookInfoService {
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
     * 删除图书管理信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	public int deleteBookInfoByIds(String ids);
	
}
