package com.wxxr.mobile.stock.app.model;

import java.util.List;

import com.wxxr.mobile.core.annotation.BindableBean;
import com.wxxr.mobile.stock.app.bean.MessageInfoBean;
@BindableBean(pkg="com.wxxr.mobile.stock.app.bean",className="MessageInfoListBean")
public class MessageInfoList {

	private List<MessageInfoBean>  messageInfos;
}
