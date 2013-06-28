/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.wxxr.javax.transaction;

/**
 *  The NotSupportedException exception indicates that an operation is not
 *  supported.
 *
 *  For example, the {@link TransactionManager#begin()} and 
 *  {@link UserTransaction#begin()} methods throw this exception if the
 *  calling thread is already associated with a transaction, and nested
 *  transactions are not supported.
 *
 *  @version $Revision$
 */
public class NotSupportedException extends Exception
{

    /**
     *  Creates a new <code>NotSupportedException</code> without a
     *  detail message.
     */
    public NotSupportedException()
    {
    }

    /**
     *  Constructs an <code>NotSupportedException</code> with the specified
     *  detail message.
     *
     *  @param msg the detail message.
     */
    public NotSupportedException(String msg)
    {
        super(msg);
    }
}
