/**
 * 
 */
package com.wxxr.mobile.android.ui;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.core.async.api.IAsyncCallable;
import com.wxxr.mobile.core.async.api.IAsyncCallback;
import com.wxxr.mobile.core.async.api.ICancellable;
import com.wxxr.mobile.core.command.api.ICommandExecutor;
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
				KUtils.getService(ICommandExecutor.class).submit(new IAsyncCallable<Drawable>() {

					/* (non-Javadoc)
					 * @see java.util.concurrent.Callable#call()
					 */
					@Override
					public void call(IAsyncCallback<Drawable> cb) {
						try {
							cb.success(fetchDrawable(val));
						}catch(Throwable t){
							cb.failed(t);
						}
					}
					
				}, new IAsyncCallback<Drawable>() {
					
					@Override
					public void success(final Drawable img) {
						if(img != null){
							AppUtils.runOnUIThread(new Runnable() {
								@Override
								public void run() {
									imgV.setBackgroundDrawable(img);
								}
							});
						}
					}
					
					@Override
					public void setCancellable(ICancellable cancellable) {
					}
					
					@Override
					public void failed(Throwable cause) {
					}
					
					@Override
					public void cancelled() {
					}
				});
			}
		}
	}
	
	public static void updateImage(final String val, final ImageView imgV) {
		if(RUtils.isResourceIdURI(val)){
			imgV.setImageResource(RUtils.getInstance().getResourceIdByURI(val));
		}else {
			Drawable img = drawableMap.get(val);
			if(img != null){
				imgV.setImageDrawable(img);
			}else{
				KUtils.getService(ICommandExecutor.class).submit(new IAsyncCallable<Drawable>() {
	
					/* (non-Javadoc)
					 * @see java.util.concurrent.Callable#call()
					 */
					@Override
					public void call(IAsyncCallback<Drawable> cb) {
						try {
							cb.success(fetchDrawable(val));
						}catch(Throwable t){
							cb.failed(t);
						}
					}
					
				}, new IAsyncCallback<Drawable>() {
					
					@Override
					public void success(final Drawable img) {
						if(img != null){
							AppUtils.runOnUIThread(new Runnable() {
								@Override
								public void run() {
									imgV.setImageDrawable(img);
								}
							});
						}
					}
					
					@Override
					public void setCancellable(ICancellable cancellable) {
					}
					
					@Override
					public void failed(Throwable cause) {
					}
					
					@Override
					public void cancelled() {
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
		op.inPurgeable = true;
		op.inInputShareable = true;
		op.inPreferredConfig = Bitmap.Config.RGB_565;
		Bitmap bitmap;
		InputStream input=null;
		OutputStream output = null;
		try {
			URLConnection con = url.openConnection();
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setRequestProperty("deviceid", AppUtils.getFramework().getDeviceId());
			con.setRequestProperty("deviceType", AppUtils.getFramework().getDeviceType());
			con.setRequestProperty("appName", AppUtils.getFramework().getApplicationName());
			con.setRequestProperty("appVer", AppUtils.getFramework().getApplicationVersion());
			output = con.getOutputStream();
			input = con.getInputStream();
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
			if(output!=null){
				try {
					output.close();
				} catch (IOException e) {
				}
			}
			if(input!=null){
				try {
					input.close();
				} catch (IOException e) {
				}
			}
		}

	}
}
