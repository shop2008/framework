/*
 * @(#)Version.java	 2013-8-16
 *
 * Copyright 2010-2015 WXXR Network Technology Co. Ltd. 
 * All rights reserved.
 * 
 * WXXR PROPRIETARY/CONFIDENTIAL.
 */
package com.wxxr.mobile.stock.client;

public class Version {
    
    private static final String buildDate="201309261709";
    private static final String buildNumber="neillin201309261709";
    private static final String versionNumber="1.0.0";
    private static final String versionName="woodpecker";

    public Version() {
        super();
    }

    /**
     * @return Returns the buildDate.
     */
    public static String getBuildDate() {
        return buildDate;
    }

    /**
     * @return Returns the buildNumber.
     */
    public static String getBuildNumber() {
        return buildNumber;
    }

    /**
     * @return Returns the versionName.
     */
    public static String getVersionName() {
        return versionName;
    }

    /**
     * @return Returns the versionNumber.
     */
    public static String getVersionNumber() {
        return versionNumber;
    }

}
