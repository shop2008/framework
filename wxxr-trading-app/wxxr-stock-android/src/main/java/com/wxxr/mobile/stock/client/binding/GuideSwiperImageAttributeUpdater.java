/**
 * 
 */
package com.wxxr.mobile.stock.client.binding;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.android.ui.RUtils;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.api.AttributeKey;
import com.wxxr.mobile.core.ui.api.IAttributeUpdater;
import com.wxxr.mobile.core.ui.api.IUIComponent;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.stock.client.widget.MinuteLineViewKeys;
public class GuideSwiperImageAttributeUpdater implements IAttributeUpdater<View> {
	private static final Trace log = Trace.register(GuideSwiperImageAttributeUpdater.class);
	@Override
	public <T> void updateControl(View control, AttributeKey<T> attrType,
			IUIComponent field,Object value) {
		if(!(control instanceof ImageView)){
			return;
		}
		final ImageView imgV = (ImageView)control;
		final String val = (String)value;
		if(attrType == MinuteLineViewKeys.imgUrl && val != null){
			int img = getImage(val);
			Context context = AppUtils.getFramework().getAndroidApplication().getApplicationContext();
			Bitmap bitmap = readBitMap(context, img);
			if(bitmap!=null){
				Drawable drawable =new BitmapDrawable(bitmap);
				imgV.setBackgroundDrawable(drawable);
			}
		}
	}

	private int getImage(String val){
		if(val!=null && !StringUtils.isBlank(val)){
			int img = RUtils.getInstance().getResourceIdByURI(val);
			return img;
		}
		return 0;
	}
	
	public static Bitmap readBitMap(Context context, int resId){
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is,null,opt);
		}

	@Override
	public <T> T updateModel(View control, AttributeKey<T> attrType,
			IUIComponent field) {
		return null;
	}

	@Override
	public boolean acceptable(Object control) {
		return control instanceof ImageView;
	}

}
