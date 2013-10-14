/**
 * 
 */
package com.wxxr.mobile.android.ui;

import android.content.Context;

/**
 * @author neillin
 *
 */
public interface IAndroidUIBinder {
	AndroidViewBinding doBinding(Context context,IAndroidBindingDescriptor descriptor);
}
