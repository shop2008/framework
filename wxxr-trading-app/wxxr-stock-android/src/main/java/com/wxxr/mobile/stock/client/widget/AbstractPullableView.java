/**
 * 
 */
package com.wxxr.mobile.stock.client.widget;

import android.view.View;

/**
 * @author neillin
 *
 */
public abstract class AbstractPullableView implements IPullableView{
	
	protected boolean isPointInsideView(float x, float y, View view){
	    int location[] = new int[2];
	    view.getLocationOnScreen(location);
	    int viewX = location[0];
	    int viewY = location[1];

	    //point is inside view bounds
	    if(( x > viewX && x < (viewX + view.getWidth())) &&
	            ( y > viewY && y < (viewY + view.getHeight()))){
	        return true;
	    } else {
	        return false;
	    }
	}
}
