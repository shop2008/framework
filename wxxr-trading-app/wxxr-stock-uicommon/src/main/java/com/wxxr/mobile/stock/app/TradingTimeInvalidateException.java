package com.wxxr.mobile.stock.app;

import com.wxxr.mobile.core.command.annotation.ConstraintLiteral;
import com.wxxr.mobile.core.command.api.CommandConstraintViolatedException;

public class TradingTimeInvalidateException extends CommandConstraintViolatedException {

    public TradingTimeInvalidateException(ConstraintLiteral ann) {
        super(ann);
    }

}
