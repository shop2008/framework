package com.wxxr.mobile.stock.app.command;

import java.util.List;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.stock.article.ejb.api.ArticleVO;

@NetworkConstraint
public class GetMyArticlesCommand implements ICommand<List<ArticleVO>> {
	
	public static final String COMMAND_NAME = "GetMyArticles";
	

	private int start, limit, type;
	@Override
	public String getCommandName() {
		return COMMAND_NAME;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Class<List<ArticleVO>> getResultType() {
		Class clazz = List.class;
		return clazz;
	}

	@Override
	public void validate() {
		
	}
	
	
	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}


	/**
	 * @param start the start to set
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * @param limit the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

}