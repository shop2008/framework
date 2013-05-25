package com.wxxr.mobile.core.log.spi;

import java.util.logging.Level;

final class JDKLevel extends Level {

    private static final long serialVersionUID = 1L;

    protected JDKLevel(final String name, final int value) {
        super(name, value);
    }

    protected JDKLevel(final String name, final int value, final String resourceBundleName) {
        super(name, value, resourceBundleName);
    }

    public static final JDKLevel FATAL = new JDKLevel("FATAL", 1100);
    public static final JDKLevel ERROR = new JDKLevel("ERROR", 1000);
    public static final JDKLevel WARN = new JDKLevel("WARN", 900);
    public static final JDKLevel INFO = new JDKLevel("INFO", 800);
    public static final JDKLevel DEBUG = new JDKLevel("DEBUG", 500);
    public static final JDKLevel TRACE = new JDKLevel("TRACE", 400);
}
