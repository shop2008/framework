package com.wxxr.mobile.android.ui;

import com.wxxr.mobile.android.app.AppUtils;

import android.view.View;
import android.view.ViewParent;

public abstract class UIUtils extends AppUtils{
	
	public static boolean isSubsidiary(View parent, View child){
		ViewParent p = child.getParent();
		while(p != null){
			if(p == parent){
				return true;
			}
			p = p.getParent();
		}
		return false;
	}

}
