/*
 * @(#)ClientInfoBean.java 2013-12-23 Copyright 2004-2013 WXXR Network
 * Technology Co. Ltd. All rights reserved. WXXR PROPRIETARY/CONFIDENTIAL.
 */

package com.wxxr.mobile.stock.app.bean;

import com.wxxr.mobile.core.bean.api.IBindableBean;
import com.wxxr.mobile.core.bean.api.IPropertyChangeListener;
import com.wxxr.mobile.core.ui.common.BindableBeanSupport;

/**
 * @class desc A ClientInfoBean.
 * @author wangxuyang
 * @version $Revision$
 * @created time 2013-12-23 下午2:29:30
 */
public class ClientInfoBean implements IBindableBean {
   private final BindableBeanSupport emitter = new BindableBeanSupport(this);

   private String version;// 版本号

   private Integer status;// 状态（不需要升级：0,必须升级：1）

   private String description;// 描述（说明）

   private String url;// 下载地址

   public void addPropertyChangeListener(IPropertyChangeListener listener) {
      emitter.addPropertyChangeListener(listener);
   }

   /**
    * @param listener
    */
   public void removePropertyChangeListener(IPropertyChangeListener listener) {
      emitter.removePropertyChangeListener(listener);
   }

   public String getVersion() {
      return version;
   }

   public void setVersion(String version) {
      String old = this.version;
      this.version = version;
      this.emitter.firePropertyChange("version", old, this.version);
   }

   public Integer getStatus() {
      return status;
   }

   public void setStatus(Integer status) {
      Integer old =  this.status;
      this.status = status;
      this.emitter.firePropertyChange("status", old, this.status);
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      String old =  this.description;
      this.description = description;
      this.emitter.firePropertyChange("description", old, this.description);
   }

   public String getUrl() {
      return url;
   }
   public void setUrl(String url) {
      String old =  this.url;
      this.url = url;
      this.emitter.firePropertyChange("url", old, this.url);
   }
}
