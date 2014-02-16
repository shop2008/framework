package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.common.ViewBase;


@View(name="actualShareCountView")
@AndroidBinding(type=AndroidBindingType.VIEW, layoutId="R.layout.actual_share_count_layout")
public abstract class ActualShareCountView extends ViewBase {

	@Field(valueKey="text")
	String challengeSharedNum;
}
