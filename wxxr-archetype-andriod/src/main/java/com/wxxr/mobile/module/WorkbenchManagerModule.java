/**
 * 
 */
package com.wxxr.mobile.module;


import com.wxxr.mobile.android.app.IAndroidAppContext;
import com.wxxr.mobile.android.ui.module.AbstractWorkbenchManagerModule;
import com.wxxr.mobile.android.ui.updater.CheckBoxAttributeUpdater;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.ui.api.IBindingDecoratorRegistry;
import com.wxxr.mobile.core.ui.api.IEventBinderManager;
import com.wxxr.mobile.core.ui.api.IFieldAttributeManager;
import com.wxxr.mobile.core.ui.api.IFieldBinderManager;
import com.wxxr.mobile.core.ui.api.IWorkbenchRTContext;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.bind.SlideEventBinder;
import com.wxxr.mobile.bind.TypeFaceAttributeUpdater;

/**
 * @author fudapeng
 *
 */
public class WorkbenchManagerModule extends AbstractWorkbenchManagerModule<IAndroidAppContext> {

	@Override
	protected void initAttributeUpdaters(IFieldAttributeManager arg0) {
		arg0.registerAttributeUpdater("typeface", new TypeFaceAttributeUpdater());
	}

	@Override
	protected void initBindingDecorators(IBindingDecoratorRegistry arg0) {
		
	}

	@Override
	protected void initEventBinders(IEventBinderManager arg0) {
		arg0.registerFieldBinder(InputEvent.EVENT_TYPE_LONGCLICK, new SlideEventBinder());
		
	}

	@Override
	protected void initFieldBinders(IFieldBinderManager arg0) {
		
	}

	@Override
	protected void initPresentationModels(IWorkbenchRTContext arg0) {
		try {
			Class.forName("com.wxxr.mobile.view.DeclarativePModelProvider").getMethod("updatePModel", new Class[]{IWorkbenchRTContext.class}).invoke(null,arg0);
		}catch(Throwable t){
			Trace.getLogger(WorkbenchManagerModule.class).fatal("Failed to load in presentation model !!!",t);
		}
	}

}
