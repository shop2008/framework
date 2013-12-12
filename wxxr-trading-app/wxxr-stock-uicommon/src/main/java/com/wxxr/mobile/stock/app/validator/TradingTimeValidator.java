package com.wxxr.mobile.stock.app.validator;

import com.wxxr.mobile.core.command.annotation.ConstraintLiteral;
import com.wxxr.mobile.core.command.api.CommandConstraintViolatedException;
import com.wxxr.mobile.core.command.api.ICommand;
import com.wxxr.mobile.core.command.api.ICommandExecutionContext;
import com.wxxr.mobile.core.command.api.ICommandValidator;
import com.wxxr.mobile.stock.app.TradingTimeInvalidateException;
import com.wxxr.mobile.stock.app.annotation.TadingTimeConstraint;
import com.wxxr.mobile.stock.app.service.ITadingCalendarService;

public class TradingTimeValidator implements ICommandValidator {
    private ICommandExecutionContext context;

    @Override
    public void checkCommandConstraints(ICommand<?> command) throws CommandConstraintViolatedException {
        TadingTimeConstraint constraint = command.getClass().getAnnotation(TadingTimeConstraint.class);
        if(constraint == null){
            return;
        }
        doValidation(TadingTimeLiteral.fromAnnotation(constraint));
    }

    private void doValidation(TadingTimeLiteral constraint) {
        String m= constraint.getMarket();
        ITadingCalendarService mgr = context.getKernelContext().getService(ITadingCalendarService.class);
        if(mgr == null){
            return;
        }
        if(!mgr.isTradingTime(m)){
//            throw new TradingTimeInvalidateException(constraint);
        }    
        
    }

    @Override
    public void destroy() {
        this.context = null;

    }

    @Override
    public void init(ICommandExecutionContext ctx) {
        this.context = ctx;

    }

    @Override
    public void validationConstraints(ConstraintLiteral... constraints) {
        TadingTimeLiteral constraint = getTadingTimeConstraint(constraints);
        if(constraint != null){
            doValidation(constraint);
        }
    }

    private TadingTimeLiteral getTadingTimeConstraint(ConstraintLiteral[] constraints) {
        if((constraints == null)||(constraints.length == 0)){
            return null;
        }
        for (ConstraintLiteral con : constraints) {
            if(con instanceof TadingTimeLiteral){
                return (TadingTimeLiteral)con;
            }
        }
        return null;
    }
    
}
