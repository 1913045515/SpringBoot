package com.example.demo.entity;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author linzhiqiang
 * @create 2018/08/01
 */
public class Page<T> implements Serializable {

	private static final long serialVersionUID = 7688886652754641380L;

	/**
	 * 默认每页大小
	 */
	public static final int DEFAULT_PAGE_SIZE = 10;

	/**
	 * 当前页, 默认为第1页
	 */
	protected int pageNo = 1;

	/**
	 * 每页记录数
	 */
	protected int pageSize = DEFAULT_PAGE_SIZE;

	/**
	 * 总记录数, 默认为-1, 表示需要查询
	 */
	protected long totalRecord = -1;

	/**
	 * 总页数, 默认为-1, 表示需要计算
	 */
	protected int totalPage = -1;

	/**
	 * 当前页记录List形式
	 */
	protected List<T> results;

	/**
	 * 该页首条记录的编号数
	 */
	private int startNum;

	/**
	 * 该页的结束数
	 */
	private int endNum;

	/**
	 * 排序字段
	 */
	private String field;

	/**
	 * 排序方式
	 */
	private String order;

	public Page() {

	}

	/**
	 * 构造分页对象
	 * 
	 * @param results
	 *            当前页对象
	 * @param pageSize
	 *            每页记录数
	 * @param totalRecord
	 *            总记录数
	 * @param pageNo
	 *            当前页
	 */
	public Page(List<T> results, int pageSize, int totalRecord, int pageNo) {
		if (results.equals(Collections.EMPTY_LIST)) {
			this.results = results;
			this.pageSize = pageSize;
		} else {
			this.results = results;
			this.totalRecord = totalRecord;
			this.pageNo = pageNo;
			this.pageSize = pageSize;
			this.totalPage = (totalRecord + pageSize - 1) / pageSize;
			this.startNum = (pageNo - 1) * pageSize + 1;
			this.endNum = startNum + results.size() - 1;
		}
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getOrder() {
		return (order == null || "".equals(order)) ? "ASC" : order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
		computeTotalPage();
	}

	public long getTotalRecord() {
		return totalRecord;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalRecord(long totalRecord) {
		this.totalRecord = totalRecord;
		computeTotalPage();
	}

	protected void computeTotalPage() {
		if (getPageSize() > 0 && getTotalRecord() > -1) {
			this.totalPage = (int) (getTotalRecord() % getPageSize() == 0 ? getTotalRecord() / getPageSize() : getTotalRecord() / getPageSize() + 1);
		}
	}

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}

	public int getStartNum() {
		return startNum;
	}

	public void setStartNum(int startNum) {
		this.startNum = startNum;
	}

	public int getEndNum() {
		return endNum;
	}

	public void setEndNum(int endNum) {
		this.endNum = endNum;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder().append("Page [pageNo=").append(pageNo).append(", pageSize=").append(pageSize).append(", totalRecord=").append(totalRecord < 0 ? "null" : totalRecord).append(", totalPage=").append(totalPage < 0 ? "null" : totalPage).append(", results=").append(results == null ? "null" : results).append("]");
		return builder.toString();
	}
}