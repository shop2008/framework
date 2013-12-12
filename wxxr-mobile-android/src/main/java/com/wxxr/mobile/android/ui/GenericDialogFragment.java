/**
 * 
 */
package com.wxxr.mobile.android.ui;

import static com.wxxr.mobile.android.ui.BindingUtils.getNavigator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.wxxr.mobile.android.app.AppUtils;
import com.wxxr.mobile.core.ui.api.IBinding;
import com.wxxr.mobile.core.ui.api.IBindingDescriptor;
import com.wxxr.mobile.core.ui.api.IDataField;
import com.wxxr.mobile.core.ui.api.IUICommand;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.IViewBinder;
import com.wxxr.mobile.core.ui.api.IViewDescriptor;
import com.wxxr.mobile.core.ui.api.IWorkbenchManager;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.api.TargetUISystem;
import com.wxxr.mobile.core.ui.api.UIConstants;
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.core.ui.common.SimpleInputEvent;
import com.wxxr.mobile.core.ui.common.UICommand;
import com.wxxr.mobile.core.ui.common.ViewBase;
import com.wxxr.mobile.core.util.StringUtils;

/**
 * @author neillin
 *
 */
public class GenericDialogFragment extends DialogFragment {
	private final IView view;
	private final Object handback;
	private IBinding<IView> binding = null;
	private boolean onShow;

	
	public GenericDialogFragment(IView v,Object hb){
		if(v == null){
			throw new IllegalArgumentException("Pass in view cannot be NULL !");
		}
		this.view = v;
		this.handback = hb;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    IBindableActivity bActivity = (getActivity() instanceof IBindableActivity) ? (IBindableActivity)getActivity() : null;
	    getNavigator().onViewCreate(view, bActivity);
	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    View v = createUI();
	    if(v != null){
	    	builder.setView(v);
	    	if(this.handback != null){
		    	IUICommand cmd = (IUICommand)this.view.getChild(UIConstants.MESSAGEBOX_ATTRIBUTE_LEFT_BUTTON);
			    if(cmd instanceof UICommand){
			    	((UICommand)cmd).setHandback(this.handback);
			    }
			    cmd = (IUICommand)this.view.getChild(UIConstants.MESSAGEBOX_ATTRIBUTE_RIGHT_BUTTON);
			    if(cmd instanceof UICommand){
			    	((UICommand)cmd).setHandback(this.handback);
			    }
			    cmd = (IUICommand)this.view.getChild(UIConstants.MESSAGEBOX_ATTRIBUTE_MID_BUTTON);
			    if(cmd instanceof UICommand){
			    	((UICommand)cmd).setHandback(this.handback);
			    }
	    	}
	    }else{
	    	IDataField<String> msgField = (IDataField<String>)view.getChild(UIConstants.MESSAGEBOX_ATTRIBUTE_MESSAGE);
	    	IDataField<String> titleField = (IDataField<String>)view.getChild(UIConstants.MESSAGEBOX_ATTRIBUTE_TITLE);
	    	if((msgField != null)&&StringUtils.isNotBlank(msgField.getValue())){
	    		builder.setMessage(BindingUtils.getMessage(msgField.getValue()));
	    	}
	    	if((titleField != null)&&StringUtils.isNotBlank(titleField.getValue())){
	    		builder.setTitle(BindingUtils.getMessage(titleField.getValue()));
	    	}
		    // Add action buttons
		    final IUICommand leftButton = (IUICommand)this.view.getChild(UIConstants.MESSAGEBOX_ATTRIBUTE_LEFT_BUTTON);
		    if(leftButton != null){
		    	builder.setPositiveButton(leftButton.getAttribute(AttributeKeys.label), new DialogInterface.OnClickListener() {
		               @Override
		               public void onClick(DialogInterface dialog, int id) {
		            	   SimpleInputEvent evt = new SimpleInputEvent(InputEvent.EVENT_TYPE_CLICK, leftButton);
		            	   if(handback != null){
		            		   evt.addProperty(UIConstants.MESSAGEBOX_ATTRIBUTE_HANDBACK, handback);
		            	   }
		            	   leftButton.invokeCommand(null, evt);
		               }
		         });
		    }
		    final IUICommand rightButton = (IUICommand)this.view.getChild(UIConstants.MESSAGEBOX_ATTRIBUTE_RIGHT_BUTTON);
		    if(rightButton != null){
		    	builder.setPositiveButton(rightButton.getAttribute(AttributeKeys.label), new DialogInterface.OnClickListener() {
		               @Override
		               public void onClick(DialogInterface dialog, int id) {
		            	   SimpleInputEvent evt = new SimpleInputEvent(InputEvent.EVENT_TYPE_CLICK, rightButton);
		            	   if(handback != null){
		            		   evt.addProperty(UIConstants.MESSAGEBOX_ATTRIBUTE_HANDBACK, handback);
		            	   }
		            	   leftButton.invokeCommand(null, evt);
		               }
		         });
		    }
		    final IUICommand middleButton = (IUICommand)this.view.getChild(UIConstants.MESSAGEBOX_ATTRIBUTE_MID_BUTTON);
		    if(middleButton != null){
		    	builder.setPositiveButton(middleButton.getAttribute(AttributeKeys.label), new DialogInterface.OnClickListener() {
		               @Override
		               public void onClick(DialogInterface dialog, int id) {
		            	   SimpleInputEvent evt = new SimpleInputEvent(InputEvent.EVENT_TYPE_CLICK, middleButton);
		            	   if(handback != null){
		            		   evt.addProperty(UIConstants.MESSAGEBOX_ATTRIBUTE_HANDBACK, handback);
		            	   }
		            	   leftButton.invokeCommand(null, evt);
		               }
		         });
		    }
	    }
		if(view instanceof ViewBase){
			((ViewBase)view).onUICreate();
		}
	    return builder.create();
	}
	
	
	protected View createUI(){
		final IWorkbenchManager mgr = AppUtils.getService(IWorkbenchManager.class);
		IViewDescriptor vDesc = mgr.getViewDescriptor(this.view.getName());
		IBindingDescriptor bDesc = vDesc.getBindingDescriptor(TargetUISystem.ANDROID);
		if(bDesc == null){
			return null;
		}
		IViewBinder vBinder = mgr.getViewBinder();
		binding = vBinder.createBinding(new IAndroidBindingContext() {
			
			@Override
			public Context getUIContext() {
				return getActivity();
			}
			
			@Override
			public View getBindingControl() {
				return null;
			}
			@Override
			public IWorkbenchManager getWorkbenchManager() {
				return mgr;
			}

			@Override
			public boolean isOnShow() {
				return onShow;
			}

			@Override
			public void hideView() {
				getDialog().dismiss();
			}
		}, bDesc);
		View view = (View)binding.getUIControl();
		view.setTag(binding);
		return view;

	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.DialogFragment#onStart()
	 */
	@Override
	public void onStart() {
		if(this.binding != null){
			this.binding.activate(view);
		}
		super.onStart();
		this.onShow = true;
		getNavigator().onViewShow(view);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.DialogFragment#onStop()
	 */
	@Override
	public void onStop() {
		this.onShow = false;
		if(this.binding != null){
			this.binding.deactivate();
		}
		super.onStop();
		getNavigator().onViewHide(view);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.DialogFragment#onDestroyView()
	 */
	@Override
	public void onDestroyView() {
		if(this.binding != null){
			this.binding.destroy();
			this.binding = null;
		}
		if(view instanceof ViewBase){
			((ViewBase)view).onUIDestroy();
		}
		super.onDestroyView();
		getNavigator().onViewDetroy(view);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		if(this.binding != null){
			this.binding.doUpdate();
		}
		super.onResume();
	}

}
