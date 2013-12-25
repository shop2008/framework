package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.ItemViewSelector;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.ArticleBean;

@View(name = "helpCenterItemView",provideSelection=true)
public abstract class HelpCenterItemView extends ViewBase implements ItemViewSelector {
	static Trace log = Trace.getLogger(HelpCenterItemView.class);
	boolean currentIndex = true;
	@Override
	public String getItemViewId(Object itemData) {
		if(itemData instanceof ArticleBean){
			ArticleBean tempBean = (ArticleBean) itemData;
			String imgUrl = tempBean.getImageUrl();
			log.info("helpCenterItemView imgUrl:" +imgUrl);
			if(imgUrl.endsWith(".jpg") || imgUrl.endsWith(".png") || imgUrl.endsWith(".gif")){
				return "helpCenterItemImageView";
			}else{
				return "helpCenterItemTextView";
			}
		}
		return null;
	}
	@Override
	public String[] getAllViewIds() {
		return new String[] {"helpCenterItemImageView","helpCenterItemTextView"};
	}
}
