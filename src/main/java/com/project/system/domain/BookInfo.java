package com.project.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.project.common.base.BaseEntity;
import java.util.Date;

/**
 * 图书管理表 book_info
 *
 * @author administrator
 * @date 2020-02-13
 */
public class BookInfo extends BaseEntity{
	private static final long serialVersionUID = 1L;

	/** 图书编号 */
	private Long bookId;
	/** 图书名称 */
	private String bookName;
	/** 作者 */
	private String author;
	/** 出版社 */
	private String publisher;
	/** 出版时间 */
	private String publishDate;
	/** 图书类型 */
	private String kind;
	/** 图书数量 */
	private Integer bookNum;
	/*借阅次数*/
	private Integer borrowNum;
	/*平均借阅时长*/
	private Long millSeconds;


	public void setBookId(Long bookId){
		this.bookId = bookId;
	}

	public Long getBookId(){
		return bookId;
	}
	public void setBookName(String bookName){
		this.bookName = bookName;
	}

	public String getBookName(){
		return bookName;
	}
	public void setAuthor(String author){
		this.author = author;
	}

	public String getAuthor(){
		return author;
	}
	public void setPublisher(String publisher){
		this.publisher = publisher;
	}

	public String getPublisher(){
		return publisher;
	}
	public void setPublishDate(String publishDate){
		this.publishDate = publishDate;
	}

	public String getPublishDate(){
		return publishDate;
	}
	public void setKind(String kind){
		this.kind = kind;
	}

	public String getKind(){
		return kind;
	}
	public void setBookNum(Integer bookNum){
		this.bookNum = bookNum;
	}

	public Integer getBookNum(){
		return bookNum;
	}

	public Integer getBorrowNum() {
		return borrowNum;
	}

	public void setBorrowNum(Integer borrowNum) {
		this.borrowNum = borrowNum;
	}

	public Long getMillSeconds() {
		return millSeconds;
	}

	public void setMillSeconds(Long millSeconds) {
		this.millSeconds = millSeconds;
	}

	public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("bookId", getBookId())
            .append("bookName", getBookName())
            .append("author", getAuthor())
            .append("publisher", getPublisher())
            .append("publishDate", getPublishDate())
            .append("kind", getKind())
            .append("bookNum", getBookNum())
            .append("createTime", getCreateTime())
            .toString();
    }
}
