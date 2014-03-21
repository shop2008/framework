/*
 * @(#)MessageReceivedEvent.java	 2013-12-20
 *
 * Copyright 2004-2013 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */

package com.wxxr.mobile.stock.app.event;

import com.wxxr.mobile.core.event.api.GenericEventObject;
import com.wxxr.mobile.core.event.api.IBroadcastEvent;
import com.wxxr.mobile.stock.app.bean.RemindMessageBean;

/**
 * @class desc A MessageReceivedEvent.
 * 
 * @author wangxuyang
 * @version $Revision$
 * @created time 2013-12-20  下午3:01:30
 */
public class NewRemindingMessagesEvent extends GenericEventObject implements IBroadcastEvent {
   
   public NewRemindingMessagesEvent(RemindMessageBean[] messages) {     
      super();
      setSource(messages);
   }
   public RemindMessageBean[] getReceivedMessages(){
      return (RemindMessageBean[])getSource();
   }
}
