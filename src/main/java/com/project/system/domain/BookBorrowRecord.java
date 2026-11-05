package com.project.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.project.common.base.BaseEntity;
import java.util.Date;

/**
 * 借阅记录表 book_borrow_record
 *
 * @author administrator
 * @date 2020-02-13
 */
public class BookBorrowRecord extends BaseEntity{
	private static final long serialVersionUID = 1L;

	/** 记录编号 */
	private Long id;
	/** 书籍 */
	private Long bookId;
	private String bookName;
	/** 用户 */
	private Long userId;
	private String userName;
	/** 借书时间 */
	private String borrowTime;
	/** 还书时间 */
	private String returnTime;
	/** 状态0未还1已还 */
	private Integer status;
	/*作者*/
	private String author;

	public void setId(Long id){
		this.id = id;
	}

	public Long getId(){
		return id;
	}
	public void setBookId(Long bookId){
		this.bookId = bookId;
	}

	public Long getBookId(){
		return bookId;
	}
	public void setUserId(Long userId){
		this.userId = userId;
	}

	public Long getUserId(){
		return userId;
	}
	public void setBorrowTime(String borrowTime){
		this.borrowTime = borrowTime;
	}

	public String getBorrowTime(){
		return borrowTime;
	}
	public void setReturnTime(String returnTime){
		this.returnTime = returnTime;
	}

	public String getReturnTime(){
		return returnTime;
	}
	public void setStatus(Integer status){
		this.status = status;
	}

	public Integer getStatus(){
		return status;
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("bookId", getBookId())
            .append("userId", getUserId())
            .append("borrowTime", getBorrowTime())
            .append("returnTime", getReturnTime())
            .append("status", getStatus())
            .toString();
    }
}
