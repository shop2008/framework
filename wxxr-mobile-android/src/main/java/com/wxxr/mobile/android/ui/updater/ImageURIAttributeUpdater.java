/**
 * 
 */
package com.wxxr.mobile.android.ui.updater;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
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
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.core.util.LRUMap;

/**
 * @author neillin
 *
 */
public class ImageURIAttributeUpdater implements IAttributeUpdater<View> {
	private static final Trace log = Trace.register(ImageURIAttributeUpdater.class);
	private static Executor executor = Executors.newFixedThreadPool(1);
	private static final LRUMap<String, Drawable> drawableMap = new LRUMap<String, Drawable>(100,10*60);
	@Override
	public <T> void updateControl(View control, AttributeKey<T> attrType,
			IUIComponent field,Object value) {
		if(!(control instanceof ImageView)){
			return;
		}
		final ImageView imgV = (ImageView)control;
		final String val = (String)value;
		if((val != null)&&(attrType == AttributeKeys.imageURI)){
			try {
				if(RUtils.isResourceIdURI(val)){
					imgV.setImageResource(RUtils.getInstance().getResourceIdByURI(val));
				}else{
					executor.execute(new Runnable() {
						public void run() {
							fetchDrawable(val);
							if(drawableMap.containsKey(val)){
								final Drawable draw = drawableMap.get(val);
								if(draw!=null){
									AppUtils.runOnUIThread(new Runnable() {
										@Override
										public void run() {
											imgV.setBackgroundDrawable(draw);
//											imgV.invalidate();
										}
									});
								}
							}
						}
					});
//					imgV.setImageDrawable(Drawable.createFromStream(url.openStream(), null));
				}
			} catch (Exception e) {
				log.error("Failed to set image for field :"+field.getName(), e);
			}
		}
	}
	
	protected Drawable fetchDrawable(String path) {
		URL url;
		try {
			url = new URL(path);
		} catch (MalformedURLException e) {
			return null;
		}
		Options op = new Options();
		op.inSampleSize = 1;
		op.inJustDecodeBounds = false;
		Bitmap bitmap;
		InputStream input=null;
		try {
			input=url.openStream();
			if(input.available()>30000){
				op.inSampleSize = 6;
			}
			bitmap = BitmapFactory.decodeStream(input, null, op);
			Drawable drawable =new BitmapDrawable(bitmap);
			drawableMap.put(path, drawable);
			return drawable;
		} catch (IOException e) {
			return null;
		}finally{
			if(input!=null){
				try {
					input.close();
				} catch (IOException e) {
				}
			}
		}

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
