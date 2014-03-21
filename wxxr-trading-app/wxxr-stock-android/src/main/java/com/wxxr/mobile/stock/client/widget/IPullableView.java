/**
 * 
 */
package com.wxxr.mobile.stock.client.widget;

import android.view.View;

/**
 * @author neillin
 *
 */
public interface IPullableView {	
	View getView();
	boolean isPullable(View view,int deltaY);
	View getTouchView(float x, float y);
}
