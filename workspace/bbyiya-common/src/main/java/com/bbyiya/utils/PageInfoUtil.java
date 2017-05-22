package com.bbyiya.utils;

import java.io.Serializable;
import java.util.List;

import com.github.pagehelper.PageInfo;

public class PageInfoUtil<T> extends PageInfo<T>  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	/**
	 * 分页对象转换
	 * @param totalCount
	 * @param limit
	 * @param list
	 */
	 public PageInfoUtil(PageInfo page,List<T> list)
	 {
		 	this.setList(list);
		 	this.setTotal(page.getTotal());
		 	this.setSize(page.getSize());
			this.setEndRow(page.getEndRow());
			this.setFirstPage(page.getFirstPage());
			this.setHasNextPage(page.isHasNextPage());
			this.setHasPreviousPage(page.isHasPreviousPage());
			this.setIsFirstPage(page.isIsFirstPage());
			this.setIsLastPage(page.isIsLastPage());
			this.setLastPage(page.getLastPage());
			this.setNavigatepageNums(page.getNavigatepageNums());
			this.setNavigatePages(page.getNavigatePages());
			this.setPageNum(page.getPageNum());
			this.setPages(page.getPages());
			this.setPageSize(page.getPrePage());
			this.setStartRow(page.getStartRow());
	       
	 }

	
}
