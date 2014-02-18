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

/**
 * @class desc A MessageReceivedEvent.
 * 
 * @author wangxuyang
 * @version $Revision$
 * @created time 2013-12-20  下午3:01:30
 */
public class HomePageRefreshRequestEvent extends GenericEventObject implements IBroadcastEvent {
   
   public HomePageRefreshRequestEvent() {     
      super();
   }
  
}
