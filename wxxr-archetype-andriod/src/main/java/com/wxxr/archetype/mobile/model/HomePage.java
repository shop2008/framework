/**
 * 
 */
package com.wxxr.archetype.mobile.model;



import com.wxxr.archetype.mobile.TimestampConverter;
import com.wxxr.archetype.mobile.service.ITimeService;
import com.wxxr.archetype.mobile.service.TimeBean;
import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.Bean;
import com.wxxr.mobile.core.ui.annotation.Bean.BindingType;
import com.wxxr.mobile.core.ui.annotation.Attribute;
import com.wxxr.mobile.core.ui.annotation.Command;
import com.wxxr.mobile.core.ui.annotation.Convertor;
import com.wxxr.mobile.core.ui.annotation.Field;
import com.wxxr.mobile.core.ui.annotation.Parameter;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.common.PageBase;

/**
 * @author fudapeng
 *
 */
@View(name = "home", withToolbar = false, description = "开发示例", provideSelection = true)
@AndroidBinding(type = AndroidBindingType.FRAGMENT_ACTIVITY, layoutId = "R.layout.home_page")
public abstract class HomePage extends PageBase {
	@Bean(type = BindingType.Service)
	ITimeService timeService;
	
	@Bean(express="${timeService.getTime()}")
	TimeBean bean;
	
	@Convertor(params={
			@Parameter(name="format",value="yyyy-MM-dd HH:mm:ss")
	})
	TimestampConverter timeConverter;
	
	@Field(valueKey="text",binding="${bean.currentTime}",converter="timeConverter")
	String time;
	
	
	@Field(valueKey="enabled",binding="${bean.ticking}")
	boolean stopBtn;
	
	@Field(valueKey="enabled" ,binding="${bean.ticking == false}")
	boolean startBtn;
	
	@Field(valueKey="text")
	String errorMessage;
	
	
	@Command
	String stopTime(InputEvent event){
		this.timeService.stopTicking();
		return null;
	}
	
	@Command
	String startTime(InputEvent event){
		this.timeService.startTicking();
		return null;
	}
	
	
	@Command
	String longPressHandler(InputEvent event){
		if(bean.isTicking()){
			this.timeService.stopTicking();
		}else{
			this.timeService.startTicking();
		}
		return null;
	}
	
	//,attributes={@Attribute(name="typeface",value="${bean.ticking ? 'NORMAL' : 'ITALIC')")}
	@Field(valueKey="textColor",binding="${bean.ticking ? '#00FF00' : '#BA2514' }")
	String helloWorldColor;
	
		
}
