package com.tossclone.securities.dto;

public class Comment {
	private int commentId;
	private Long userId;
	private String userName, stockCode, content;

	public Comment() {
		super();
	}

	public Comment(int commentId, Long userId, String userName, String stockCode, String content) {
		super();
		this.commentId = commentId;
		this.userId = userId;
		this.userName = userName;
		this.stockCode = stockCode;
		this.content = content;
	}

	public int getCommentId() {
		return commentId;
	}

	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getStockCode() {
		return stockCode;
	}

	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Comment [commentId=" + commentId + ", userId=" + userId + ", userName=" + userName + ", stockCode="
				+ stockCode + ", content=" + content + "]";
	}

}
