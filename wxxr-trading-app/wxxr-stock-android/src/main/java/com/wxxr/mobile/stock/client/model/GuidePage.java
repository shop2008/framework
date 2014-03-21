package com.wxxr.mobile.stock.client.model;

import java.util.ArrayList;
import java.util.List;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Menu;
import com.wxxr.mobile.core.ui.annotation.OnCreate;
import com.wxxr.mobile.core.ui.annotation.OnHide;
import com.wxxr.mobile.core.ui.annotation.OnUICreate;
import com.wxxr.mobile.core.ui.annotation.UIItem;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.annotation.ViewGroup;
import com.wxxr.mobile.core.ui.api.IMenu;
import com.wxxr.mobile.core.ui.api.IModelUpdater;
import com.wxxr.mobile.core.ui.api.IViewGroup;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;

@View(name = "guidePage", withToolbar = true, description = "新手指引")
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.guide_page_layout")
public abstract class GuidePage extends PageBase implements IModelUpdater{

	@ViewGroup(viewIds={"Guide1", "Guide2", "Guide3", "Guide4", "Guide5", "Guide6", "Guide7", "Guide8", "Guide9", "GuideVoucherItemView"},attributes={
			@Attribute(name = "viewPosition", value = "${nextPosition}")
	})
	private IViewGroup contents;
	
	@Field(valueKey = "options", binding = "${imageUris}", attributes = { @Attribute(name = "position", value = "${nextPosition}") })
	List<String> guideImages;

	int selectPos;

	@Field(valueKey = "text", binding = "${10}")
	String totalPage;

	@Field(valueKey = "text", binding = "${(currentPosition+1)>10?'10':(currentPosition+1)}")
	String currentPage;

	@Menu(items = { "left" })
	private IMenu toolbar;

	@Command(description = "Invoke when a toolbar item was clicked", uiItems = { @UIItem(id = "left", label = "返回", icon = "resourceId:drawable/back_button_style", visibleWhen = "${true}") })
	String toolbarClickedLeft(InputEvent event) {
		hide();
		return null;
	}

	@OnUICreate
	void onUiCreateDemo() {
		selectPos = 0;
		registerBean("nextPosition", 0);
		registerBean("currentPosition", 0);
	}

	@OnHide
	void onHideDemo() {
		registerBean("nextPosition", selectPos + 1);
	}

	@Bean
	int nextPosition = 0;

	@Bean
	int currentPosition = 0;

	@Field(valueKey = "text", visibleWhen = "${currentPosition<9}")
	String bottomRightBtn;

	@Field(valueKey = "text", visibleWhen = "${currentPosition>0}")
	String bottomLeftBtn;

	@Field(valueKey = "text", enableWhen = "${currentPosition==9}", visibleWhen = "${currentPosition==9}")
	String centerBtn;

	@Bean
	List<String> imageUris;

	@OnCreate
	protected void initGuideImages() {
		List<String> guideImages = new ArrayList<String>();
		for (int i = 0; i < 10; i++) {
			guideImages.add("resourceId:drawable/guide_" + (i + 1));
		}
		this.imageUris = guideImages;
		registerBean("imageUris", this.imageUris);
	}

	@Command
	String handlerPageChanged(InputEvent event) {

		Integer position = (Integer) event.getProperty("position");

		if (position != null) {
			selectPos = position;
			registerBean("currentPosition", selectPos);
		}
		return null;
	}

	@Command
	String bottomLeftClick(InputEvent event) {

		selectPos = selectPos - 1;
		if (selectPos >= 0) {
			registerBean("currentPosition", selectPos);
			registerBean("nextPosition", selectPos);
		}
		return null;
	}

	@Command
	String bottomRightClick(InputEvent event) {
		selectPos = selectPos + 1;
		
		if (selectPos < 10) {
			registerBean("currentPosition", selectPos);
			registerBean("nextPosition", selectPos);
		}
		return null;
	}
	
	@Override
	public void updateModel(Object arg0) {
		
	}
}
