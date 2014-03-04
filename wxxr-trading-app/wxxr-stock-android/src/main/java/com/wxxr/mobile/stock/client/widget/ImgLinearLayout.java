package com.wxxr.mobile.stock.client.widget;

import java.io.InputStream;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.wxxr.mobile.android.ui.RUtils;
import com.wxxr.mobile.core.util.StringUtils;
import com.wxxr.mobile.stock.client.R;

public class ImgLinearLayout extends ImageView {
	private Context context;
	public ImgLinearLayout(Context context) {
		super(context);
		this.context = context;
	}
	public ImgLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initData(attrs);
	}
	public ImgLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initData(attrs);
	}
	private void initData(AttributeSet attrs){
		TypedArray arrs = context.obtainStyledAttributes(attrs, R.styleable.ImgLinearLayout);
		if(arrs!=null){
			String background = (String) arrs.getText(R.styleable.ImgLinearLayout_backgroundImage);
			if(background!=null && !StringUtils.isBlank(background)){
				int resId = getImage(background);
				if(resId!=0){
					Bitmap bitmap = readBitMap(context, resId);
					Drawable drawable =new BitmapDrawable(bitmap);
					this.setBackgroundDrawable(drawable);
				}
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
	
	 public Bitmap drawable2Bitmap(Drawable drawable) {  
	        Bitmap bitmap = Bitmap.createBitmap(  
	                        drawable.getIntrinsicWidth(),  
	                        drawable.getIntrinsicHeight(),  
	                        Bitmap.Config.RGB_565);  
	        Canvas canvas = new Canvas(bitmap);  
	        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),  
	                drawable.getIntrinsicHeight());  
	        drawable.draw(canvas);  
	        return bitmap;  
	    }  	
}
