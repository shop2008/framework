package com.wxxr.mobile.stock.client.binding;

import com.wxxr.mobile.core.ui.api.IDataField;
import com.wxxr.mobile.core.ui.api.IView;
import com.wxxr.mobile.core.ui.api.InputEvent;
import com.wxxr.mobile.core.ui.api.InputEventDecorator;
import com.wxxr.mobile.core.ui.api.InputEventHandlingContext;
import com.wxxr.mobile.stock.client.widget.StockSearchViewKeys;

public class StockSearchViewEventDecorator implements InputEventDecorator {

	private final InputEventDecorator next;

	public StockSearchViewEventDecorator(InputEventDecorator decor) {
		this.next = decor;
	}

	@Override
	public void handleEvent(InputEventHandlingContext context, InputEvent event) {
		final IView v = context.getViewModel();

		IDataField<String> stockSearchViewBody = v.getChild(
				"stockSearchViewBody", IDataField.class);
		
		Boolean keyBoardViewShown = stockSearchViewBody.getAttribute(StockSearchViewKeys.keyBoardShow);
		boolean keyBoardIsShow = false;
		if(keyBoardViewShown!= null) {
			keyBoardIsShow = keyBoardViewShown.booleanValue();
		}
		stockSearchViewBody
				.setAttribute(StockSearchViewKeys.keyBoardShow, keyBoardIsShow);
		this.next.handleEvent(context, event);
	}

}
