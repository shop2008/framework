/**
 * 
 */
package com.wxxr.mobile.stock.client.model;

import com.wxxr.mobile.android.ui.AndroidBindingType;
import com.wxxr.mobile.android.ui.annotation.AndroidBinding;
import com.wxxr.mobile.core.ui.annotation.View;
import com.wxxr.mobile.core.ui.common.ViewBase;

/**
 * @author neillin
 *
 */
@View(name="tradingWinner")
@AndroidBinding(type=AndroidBindingType.FRAGMENT,layoutId="R.layout.layout_earn_money_rank")
public abstract class TradingWinnerView extends ViewBase {

}
