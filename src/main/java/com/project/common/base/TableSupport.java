package com.project.common.base;

import com.project.common.constant.Constants;
import com.project.common.utils.str.Convert;
import com.project.common.utils.web.ServletUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 表格数据处理
 *
 * @author
 */
public class TableSupport {
    /**
     * 封装分页对象
     */
    public static PageDomain getPageDomain() {
        PageDomain pageDomain = new PageDomain();
        HttpServletRequest request = ServletUtils.getRequest();
        pageDomain.setPageNum(Convert.toInt(request.getParameter(Constants.PAGE_NUM)));
        pageDomain.setPageSize(Convert.toInt(request.getParameter(Constants.PAGE_SIZE)));
        pageDomain.setOrderByColumn(request.getParameter(Constants.ORDER_BY_COLUMN));
        pageDomain.setIsAsc(request.getParameter(Constants.IS_ASC));
        return pageDomain;
    }

    public static PageDomain buildPageRequest() {
        return getPageDomain();
    }
}
