package com.wxxr.mobile.stock.app.validator;

import com.wxxr.mobile.core.command.annotation.ConstraintLiteral;
import com.wxxr.mobile.stock.app.annotation.TadingTimeConstraint;

public class TadingTimeLiteral extends ConstraintLiteral{
    private String market;
    
    public TadingTimeLiteral(){
        
    } 
    public TadingTimeLiteral(String market){
        if(market == null){
            throw new IllegalArgumentException("Invalid  type : NULL");
        }
        this.market = market;
    }
    public String getMarket() {
        return market;
    }

    public static TadingTimeLiteral fromAnnotation(TadingTimeConstraint ann){
        return new TadingTimeLiteral(ann.market());
    }
    
    
    
}
