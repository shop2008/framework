package com.wxxr.mobile.stock.app.command;

import com.wxxr.mobile.core.command.annotation.NetworkConstraint;

@NetworkConstraint
public class GetOtherUserGainBeanCommand extends GetGainBeanCommand {
    String userId;
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}