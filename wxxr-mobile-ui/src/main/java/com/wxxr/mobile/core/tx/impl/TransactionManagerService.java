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
package com.wxxr.mobile.core.tx.impl;

import java.util.HashMap;
import java.util.Map;

import com.wxxr.javax.transaction.TransactionManager;

/**
 *  This is a JMX service which manages the TransactionManager.
 *  The service creates it and binds a Reference to it into JNDI.
 *
 *  @see TxManager
 *  @author <a href="mailto:rickard.oberg@telkel.com">Rickard �berg</a>
 *  @author <a href="mailto:osh@sparre.dk">Ole Husgaard</a>
 *  @author <a href="mailto:toby.allsopp@peace.com">Toby Allsopp</a>
 *  @author <a href="reverbel@ime.usp.br">Francisco Reverbel</a>
 *  @version $Revision$
 *
 * @jmx.mbean extends="org.jboss.system.ServiceMBean"
 */
public class TransactionManagerService
{

   // Attributes ----------------------------------------------------

   private boolean globalIdsEnabled = false; // flag duplicated in TM

   /** Whether to interrupt threads at transaction timeout */
   private boolean interruptThreads = false;

   private int timeout = 300; // default tx timeout, dupl. in TM when it exists.

   /** The integrity factory */
   private TransactionIntegrityFactory integrityFactory;

   private final Map xaExceptionFormatters = new HashMap();

   // Static --------------------------------------------------------

   static TxManager tm;

   // ServiceMBeanSupport overrides ---------------------------------


   protected void startService()
         throws Exception
   {
      XidFactory xidFactoryObj = new XidFactory();
      TransactionImpl.xidFactory = xidFactoryObj;
      TransactionImpl.txManagerService = this;

      // Get a reference to the TxManager singleton.
      tm = TxManager.getInstance();
      // Set its default timeout.
      tm.setDefaultTransactionTimeout(timeout);
      // Initialize its globalIdsEnabled flag.
      tm.setGlobalIdsEnabled(globalIdsEnabled);
      tm.setInterruptThreads(interruptThreads);
      if (integrityFactory != null)
         tm.setTransactionIntegrity(integrityFactory.createTransactionIntegrity());
      else
         tm.setTransactionIntegrity(null);

   }

   protected void stopService()
   {
   }

   /**
    * Describe <code>getGlobalIdsEnabled</code> method here.
    *
    * @return an <code>boolean</code> value
    * @jmx:managed-attribute
    */
   public boolean getGlobalIdsEnabled()
   {
      return globalIdsEnabled;
   }

   /**
    * Describe <code>setGlobalIdsEnabled</code> method here.
    *
    * @param newValue an <code>boolean</code> value
    * @jmx:managed-attribute
    */
   public void setGlobalIdsEnabled(boolean newValue)
   {
      globalIdsEnabled = newValue;
      if (tm != null) // Update TM setting
         tm.setGlobalIdsEnabled(newValue);
   }

   /**
    * Is thread interruption enabled at transaction timeout
    * 
    * @jmx:managed-attribute
    * @return true for interrupt threads, false otherwise
    */
   public boolean isInterruptThreads() 
   {
      return interruptThreads;
   }

   /**
    * Enable/disable thread interruption at transaction timeout.
    * 
    * @jmx:managed-attribute
    * @param interruptThreads pass true to interrupt threads, false otherwise
    */
   public void setInterruptThreads(boolean interruptThreads) 
   {
      this.interruptThreads = interruptThreads;
      if (tm != null)
         tm.setInterruptThreads(interruptThreads);
   }

   /**
    * Describe <code>getTransactionTimeout</code> method here.
    *
    * @return an <code>int</code> value
    * @jmx:managed-attribute
    */
   public int getTransactionTimeout()
   {
      if (tm != null) // Get timeout value from TM (in case it was changed).
         timeout = tm.getDefaultTransactionTimeout();
      return timeout;
   }

   /**
    * Describe <code>setTransactionTimeout</code> method here.
    *
    * @param timeout an <code>int</code> value
    * @jmx:managed-attribute
    */
   public void setTransactionTimeout(int timeout)
   {
      this.timeout = timeout;
      if (tm != null) // Update TM default timeout
         tm.setDefaultTransactionTimeout(timeout);
   }

   /**
    * Set the Transaction integrity factory
    *
    * @param factory the factory 
    */
   public void setTransactionIntegrityFactory(TransactionIntegrityFactory factory)
   {
      this.integrityFactory = factory;
      if (tm != null)
      {
         if (factory != null)
            tm.setTransactionIntegrity(factory.createTransactionIntegrity());
         else
            tm.setTransactionIntegrity(null);
      }
   }

   /**
    * mbean get-set pair for field transactionManager
    * Get the value of transactionManager
    * @return value of transactionManager
    *
    * @jmx.managed-attribute description="Returns the TransactionManager managed by this service"
    *      access="read-only"
    */
   public TransactionManager getTransactionManager()
   {
      return tm;
   }

   /**
    * Counts the number of transactions
    *
    * @return the number of active transactions
    *
    * @jmx:managed-attribute
    */
   public long getTransactionCount()
   {
      return tm.getTransactionCount();
   }
   /** The number of commits.
    *
    * @return the number of transactions that have been committed
    *
    * @jmx:managed-attribute
    */
   public long getCommitCount()
   {
      return tm.getCommitCount();
   }
   /** The number of rollbacks.
    *
    * @return the number of transactions that have been rolled back
    *
    * @jmx:managed-attribute
    */
   public long getRollbackCount()
   {
      return tm.getRollbackCount();
   }


   /**
    * The <code>unregisterXAExceptionFormatter</code> method
    *
    * @param clazz a <code>Class</code> value
    *
    * @jmx.managed-operation
    */
   public void unregisterXAExceptionFormatter(Class clazz)
   {
      xaExceptionFormatters.remove(clazz);
   }

}
