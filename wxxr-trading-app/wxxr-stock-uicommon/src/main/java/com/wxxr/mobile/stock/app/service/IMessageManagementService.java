/**
 * 
 */
package com.wxxr.mobile.stock.app.service;

import java.util.List;

import com.wxxr.mobile.stock.app.bean.PullMessageBean;
import com.wxxr.mobile.stock.app.bean.RemindMessageBean;

/**
 * @author wangxuyang
 *
 */
public interface IMessageManagementService {
	 List<PullMessageBean> getUnReadPullMessage();
	 List<RemindMessageBean> getUnReadRemindMessage();
	 PullMessageBean getFirstPullMessage();
	 RemindMessageBean getFirstRemindMessage();
	 void saveRemindMsg(RemindMessageBean msg);
	 void savePullMsg(PullMessageBean msg);
}
