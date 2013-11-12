/**
 * 
 */
package com.wxxr.mobile.android.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
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
import com.wxxr.mobile.core.ui.common.AttributeKeys;
import com.wxxr.mobile.core.ui.common.SimpleInputEvent;
import com.wxxr.mobile.core.util.StringUtils;

/**
 * @author neillin
 *
 */
public class GenericDialogFragment extends DialogFragment {
	private final IView view;
	private IBinding<IView> binding = null;

	
	public GenericDialogFragment(IView v){
		if(v == null){
			throw new IllegalArgumentException("Pass in view cannot be NULL !");
		}
		this.view = v;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    View v = createUI();
	    if(v != null){
	    	builder.setView(v);
	    }else{
	    	IDataField<String> msgField = (IDataField<String>)view.getChild("message");
	    	IDataField<String> titleField = (IDataField<String>)view.getChild("title");
	    	if((msgField != null)&&StringUtils.isNotBlank(msgField.getValue())){
	    		builder.setMessage(BindingUtils.getMessage(msgField.getValue()));
	    	}
	    	if((titleField != null)&&StringUtils.isNotBlank(titleField.getValue())){
	    		builder.setTitle(BindingUtils.getMessage(titleField.getValue()));
	    	}
	    }
	    // Add action buttons
	    final IUICommand leftButton = (IUICommand)this.view.getChild("left_button");
	    if(leftButton != null){
	    	builder.setPositiveButton(leftButton.getAttribute(AttributeKeys.label), new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   SimpleInputEvent evt = new SimpleInputEvent(InputEvent.EVENT_TYPE_CLICK, leftButton);
	            	   leftButton.invokeCommand(null, evt);
	               }
	         });
	    }
	    final IUICommand rightButton = (IUICommand)this.view.getChild("right_button");
	    if(rightButton != null){
	    	builder.setPositiveButton(rightButton.getAttribute(AttributeKeys.label), new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   SimpleInputEvent evt = new SimpleInputEvent(InputEvent.EVENT_TYPE_CLICK, rightButton);
	            	   leftButton.invokeCommand(null, evt);
	               }
	         });
	    }
	    final IUICommand middleButton = (IUICommand)this.view.getChild("mid_button");
	    if(middleButton != null){
	    	builder.setPositiveButton(middleButton.getAttribute(AttributeKeys.label), new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   SimpleInputEvent evt = new SimpleInputEvent(InputEvent.EVENT_TYPE_CLICK, middleButton);
	            	   leftButton.invokeCommand(null, evt);
	               }
	         });
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
		}, bDesc);
		View view = (View)binding.getUIControl();
		view.setTag(binding);
		return view;

	}

}
