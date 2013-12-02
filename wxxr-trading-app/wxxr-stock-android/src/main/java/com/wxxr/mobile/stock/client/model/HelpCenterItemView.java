package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.ItemViewSelector;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.stock.app.bean.ArticleBean;

@View(name = "helpCenterItemView",provideSelection=true)
public abstract class HelpCenterItemView extends ViewBase implements ItemViewSelector {
	static Trace log = Trace.getLogger(HelpCenterItemView.class);
	int currentIndex = 0;
	@Override
	public String getItemViewId(Object itemData) {
		if(itemData instanceof ArticleBean){
			ArticleBean tempBean = (ArticleBean) itemData;
			String imgUrl = tempBean.getImageUrl();
			String imgType = imgUrl.substring(imgUrl.length() - 4, imgUrl.length());
			imgType = imgType.toLowerCase();
			log.info("getItemViewId="+itemData.toString());
			if(currentIndex==0 && (imgType.equals(".png") || imgType.equals(".jpg") || imgType.equals(".gif"))){
				currentIndex++;
				return "helpCenterItemImageView";
			}else{
				return "helpCenterItemTextView";
			}
		}
		return null;
	}
}
