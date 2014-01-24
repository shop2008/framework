/**
 * 
 */
package com.wxxr.mobile.model;



import android.graphics.Color;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.KUtils;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.OnCreate;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;
import com.wxxr.mobile.service.ITimeService;

/**
 * @author fudapeng
 *
 */
@View(name = "home", withToolbar = false, description = "开发示例", provideSelection = true)
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.home_page")
public abstract class DemoHomePage extends PageBase {
	private static final Trace log = Trace.register(DemoHomePage.class);
	@Bean(type = BindingType.Service)
	ITimeService timeService;
	
	@Field(valueKey="text",binding="${timeBean}")
	String time;
	
	@Bean
	String timeBean;
	
	@Bean
	boolean sbtu = true;
	
	@Bean
	boolean sabtu;
	
	boolean running = true;
	
	@Field(valueKey="enabled",binding="${sbtu}")
	boolean stopBtn;
	
	@Field(valueKey="enabled" ,binding="${sabtu}")
	boolean startBtn;
	
	@Field(valueKey="text")
	String errorMessage;
	
	@OnCreate
	void initThread(){
		sbtu = true;
		sabtu = false;
		KUtils.executeTask(new Runnable() {
			@Override
			public void run() {
				while(timeService != null){
					try {
						if(running){
							timeBean = timeService.getTime();
							registerBean("timeBean", timeBean);
							try {
								Thread.sleep(500);
							} catch (InterruptedException e1) {
								errorMessage = e1.getMessage();
							}
						}else{
							Thread.sleep(1000);
						}
					} catch (Exception e) {
						errorMessage = e.getMessage();
					}
				}
			}
		});
	}
	
	@Command
	String stopTime(InputEvent event){
		stop();
		return null;
	}
	
	@Command
	String startTime(InputEvent event){
		start();
		return null;
	}
	
	@Field(valueKey="textColor",binding="${colorBean}")
	String helloWorldColor;
	
	@Bean
	String colorBean;
	
	
	public void start(){
		sbtu = running = true;
		sabtu = false;
		registerBean("sbtu", sbtu);
		registerBean("sabtu", sabtu);
		if("#BA2514".equals(colorBean)){
			colorBean = "#00FF00";
			registerBean("colorBean", colorBean);
		}
	}
	
	public void stop(){
		sbtu = running = false;
		sabtu = true;
		registerBean("sbtu", sbtu);
		registerBean("sabtu", sabtu);
		colorBean="#BA2514";
		registerBean("colorBean", colorBean);
	}
	
	
//	private static final int STOCK_RED = Color.parseColor("#BA2514"); 
	
	
	@Command
	String swipeLeftHandler(InputEvent event){
		log.warn("swipeLeftHandler");
		if("#BA2514".equals(colorBean)){
			colorBean = "#00FF00";
			registerBean("colorBean", colorBean);
			start();
			return null;
		}
		colorBean="#BA2514";
		registerBean("colorBean", colorBean);
		stop();
		return null;
	}
	
}
