/**
 * 
 */
package com.wxxr.mobile.android.ui;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.core.microkernel.api.KUtils;
import com.wxxr.mobile.core.util.LRUMap;

/**
 * @author neillin
 *
 */
public abstract class ImageUtils {

	private static final LRUMap<String, Drawable> drawableMap = new LRUMap<String, Drawable>(100,10*60);

	public static void updateViewBackgroupImage(final String val, final View imgV) {
		if(RUtils.isResourceIdURI(val)){
			imgV.setBackgroundResource(RUtils.getInstance().getResourceIdByURI(val));
		}else{
			Drawable img = drawableMap.get(val);
			if(img != null){
				imgV.setBackgroundDrawable(img);
			}else{
				KUtils.executeTask(new Runnable() {
					public void run() {
						final Drawable img = fetchDrawable(val);
						if(img != null){
							AppUtils.runOnUIThread(new Runnable() {
								@Override
								public void run() {
									imgV.setBackgroundDrawable(img);
								}
							});
						}
					}
				});
			}
		}
	}
	
	public static void updateImage(final String val, final ImageView imgV) {
		if(RUtils.isResourceIdURI(val)){
			imgV.setImageResource(RUtils.getInstance().getResourceIdByURI(val));
		}else{
			Drawable img = drawableMap.get(val);
			if(img != null){
				imgV.setImageDrawable(img);
			}else{
				KUtils.executeTask(new Runnable() {
					public void run() {
						final Drawable img = fetchDrawable(val);
						if(img != null){
							AppUtils.runOnUIThread(new Runnable() {
								@Override
								public void run() {
									imgV.setImageDrawable(img);
								}
							});
						}
					}
				});
			}
		}
	}


	protected static Drawable fetchDrawable(String path) {
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

}
