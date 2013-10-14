package com.wxxr.mobile.android.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public interface IViewCreationCallback {
	void onViewCreated(View view,Context context, AttributeSet attrset);
}
