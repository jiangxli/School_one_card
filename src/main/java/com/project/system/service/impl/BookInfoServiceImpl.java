package com.project.system.service.impl;

import java.util.List;

import com.project.common.utils.TimeUuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.system.mapper.BookInfoMapper;
import com.project.system.domain.BookInfo;
import com.project.system.service.IBookInfoService;
import com.project.common.utils.str.Convert;

/**
 * 图书管理 服务层实现
 *
 * @author administrator
 * @date 2020-02-13
 */
@Service
public class BookInfoServiceImpl implements IBookInfoService {
    @Autowired
    private BookInfoMapper bookInfoMapper;

    /**
     * 查询图书管理信息
     *
     * @param bookId 图书管理ID
     * @return 图书管理信息
     */
    @Override
    public BookInfo selectBookInfoById(Long bookId) {
        return bookInfoMapper.selectBookInfoById(bookId);
    }

    /**
     * 查询图书管理列表
     *
     * @param bookInfo 图书管理信息
     * @return 图书管理集合
     */
    @Override
    public List<BookInfo> selectBookInfoList(BookInfo bookInfo) {
        return bookInfoMapper.selectBookInfoList(bookInfo);
    }

    /**
     * 新增图书管理
     *
     * @param bookInfo 图书管理信息
     * @return 结果
     */
    @Override
    public int insertBookInfo(BookInfo bookInfo) {
        bookInfo.setBookId(TimeUuidUtil.get16UUID());
        return bookInfoMapper.insertBookInfo(bookInfo);
    }

    /**
     * 修改图书管理
     *
     * @param bookInfo 图书管理信息
     * @return 结果
     */
    @Override
    public int updateBookInfo(BookInfo bookInfo) {
        return bookInfoMapper.updateBookInfo(bookInfo);
    }

    /**
     * 删除图书管理对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteBookInfoByIds(String ids) {
        return bookInfoMapper.deleteBookInfoByIds(Convert.toStrArray(ids));
    }

}
