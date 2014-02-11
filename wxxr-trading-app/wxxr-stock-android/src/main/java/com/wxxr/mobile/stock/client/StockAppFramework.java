/**
 * 
 */
package com.wxxr.mobile.stock.client;

import java.io.File;
import java.io.InputStream;
import java.util.Dictionary;
import java.util.Properties;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import android.app.Application;
import android.os.Environment;
import android.webkit.WebView;

import com.wxxr.mobile.android.app.AndroidFramework;
import com.wxxr.mobile.android.app.IAndroidFramework;
import com.wxxr.mobile.android.http.HttpRpcServiceModule;
import com.wxxr.mobile.android.network.NetworkManagementModule;
import com.wxxr.mobile.android.preference.DictionaryUtils;
import com.wxxr.mobile.android.preference.PreferenceManagerModule;
import com.wxxr.mobile.android.ui.module.AndroidI10NServiceModule;
import com.wxxr.mobile.android.validation.ValidationMessageInterpolator;
import com.wxxr.mobile.core.command.impl.CommandExecutorModule;
import com.wxxr.mobile.core.common.SimpleUserIdentityManagerModule;
import com.wxxr.mobile.core.common.StatefullServicePlugin;
import com.wxxr.mobile.core.common.UserBasedSessionManagerModule;
import com.wxxr.mobile.core.log.api.Trace;
import com.wxxr.mobile.core.microkernel.api.AbstractModule;
import com.wxxr.mobile.core.microkernel.api.IServiceAvailableCallback;
import com.wxxr.mobile.core.rpc.http.api.HttpHeaderNames;
import com.wxxr.mobile.core.rpc.rest.RestEasyClientModule;
import com.wxxr.mobile.preference.api.IPreferenceManager;
import com.wxxr.mobile.stock.app.IStockAppContext;
import com.wxxr.mobile.stock.app.IStockAppFramework;
import com.wxxr.mobile.stock.app.service.impl.ArticleManagementServiceImpl;
import com.wxxr.mobile.stock.app.service.impl.ContentManager;
import com.wxxr.mobile.stock.app.service.impl.DBServiceImpl;
import com.wxxr.mobile.stock.app.service.impl.EntityLoaderRegistryImpl;
import com.wxxr.mobile.stock.app.service.impl.InfoCenterManagementServiceImpl;
import com.wxxr.mobile.stock.app.service.impl.NetworkCheckServiceImpl;
import com.wxxr.mobile.stock.app.service.impl.OptionStockManagementServiceImpl;
import com.wxxr.mobile.stock.app.service.impl.StockInfoSyncServiceImpl;
import com.wxxr.mobile.stock.app.service.impl.SyncConnector;
import com.wxxr.mobile.stock.app.service.impl.TadingCalendarServiceImpl;
import com.wxxr.mobile.stock.app.service.impl.TradingManagementServiceImpl;
import com.wxxr.mobile.stock.app.service.impl.URLLocatorManagementServiceImpl;
import com.wxxr.mobile.stock.app.service.impl.UserLoginManagementServiceImpl;
import com.wxxr.mobile.stock.app.service.impl.UserManagementServiceImpl;
import com.wxxr.mobile.stock.client.module.StockSiteSecurityModule;
import com.wxxr.mobile.stock.client.module.WorkbenchManagerModule;
import com.wxxr.mobile.stock.client.service.ClientInfoService;
import com.wxxr.mobile.stock.client.service.GenericContentService;
import com.wxxr.mobile.stock.client.service.UpdateVertionService;

/**
 * @author neillin
 *
 */
public class StockAppFramework extends AndroidFramework<IStockAppContext, AbstractModule<IStockAppContext>> implements
		IStockAppFramework {

	private static final Trace log = Trace.register(StockAppFramework.class);
	
	private final StockApplication app;
	private final String userAgent;
	private class ComHelperAppContextImpl extends AbstractContext implements IStockAppContext {

		@SuppressWarnings("rawtypes")
		@Override
		public IAndroidFramework getApplication() {
			return StockAppFramework.this;
		}
		
	};
	
	private ComHelperAppContextImpl context;
	public StockAppFramework(StockApplication app){
		this.app = app;
		this.userAgent = new WebView(app).getSettings().getUserAgentString();
		context.setAttribute(HttpHeaderNames.USER_AGENT, this.userAgent);
		if(log.isDebugEnabled()){
			log.debug("UserAgent:"+this.userAgent);
		}
	}
	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.app.IAndroidApplication#getAndroidApplication()
	 */
	@Override
	public Application getAndroidApplication() {
		return app;
	}

	@Override
	protected IStockAppContext getContext() {
		if(context == null)
			context = new ComHelperAppContextImpl();
		return context;
	}

	

	@Override
	protected void initInternalServices() {
		super.initInternalServices();
		addServiceFeaturePlugin(new StatefullServicePlugin());
	}
	@Override
	protected void initModules() {
		registerKernelModule(new StockSiteSecurityModule());
		//registerKernelModule(new EventRouterImpl<IStockAppContext>());
		registerKernelModule(new NetworkManagementModule<IStockAppContext>());
		registerKernelModule(new PreferenceManagerModule<IStockAppContext>());
		registerKernelModule(new SimpleUserIdentityManagerModule<IStockAppContext>());
		registerKernelModule(new UserBasedSessionManagerModule<IStockAppContext>());
		HttpRpcServiceModule<IStockAppContext> m = new HttpRpcServiceModule<IStockAppContext>();
		m.setEnablegzip(false);
		m.setConnectionPoolSize(30);
		registerKernelModule(m);
		RestEasyClientModule<IStockAppContext> rest = new RestEasyClientModule<IStockAppContext>();
//		rest.getClient().register(GSONProvider.class);
		rest.getClient().register(JacksonJsonProvider.class);
//		rest.getClient().register(XStreamProvider.class);
		registerKernelModule(rest);
		CommandExecutorModule<IStockAppContext> cmdExecutor = new CommandExecutorModule<IStockAppContext>();
		CommandIntializer.initBizCommand(cmdExecutor);
		registerKernelModule(cmdExecutor);
		registerKernelModule(new WorkbenchManagerModule());
		registerKernelModule(new EntityLoaderRegistryImpl<IStockAppContext>());
		registerKernelModule(new NetworkManagementModule<IStockAppContext>());
		
		// register service
		registerKernelModule(new ContentManager());
		registerKernelModule(new SyncConnector());
		registerKernelModule(new StockInfoSyncServiceImpl());
		registerKernelModule(new NetworkCheckServiceImpl());
		registerKernelModule(new URLLocatorManagementServiceImpl());
		registerKernelModule(new TradingManagementServiceImpl());
		registerKernelModule(new UserManagementServiceImpl());
		registerKernelModule(new ArticleManagementServiceImpl());
		registerKernelModule(new InfoCenterManagementServiceImpl());	
		registerKernelModule(new AndroidI10NServiceModule<IStockAppContext>());
		registerKernelModule(new ValidationMessageInterpolator<IStockAppContext>());
	    registerKernelModule(new TadingCalendarServiceImpl());
	    registerKernelModule(new DBServiceImpl());
	    registerKernelModule(new GenericContentService());

	    registerKernelModule(new UserLoginManagementServiceImpl());

	    registerKernelModule(new ClientInfoService());
	    
	    registerKernelModule(new UpdateVertionService());
	    
	    registerKernelModule(new OptionStockManagementServiceImpl());

	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.app.AndroidApplication#start()
	 */
	@Override
	public void start() {
		context.checkServiceAvailable(IPreferenceManager.class, new IServiceAvailableCallback<IPreferenceManager>() {

			@Override
			public void serviceAvailable(IPreferenceManager prefMgr) {
				Dictionary<String, String> d = prefMgr.getPreference(IPreferenceManager.SYSTEM_PREFERENCE_NAME);
				if(d == null){
					try {
						InputStream is = app.getAssets().open("server.properties");
						Properties mProps = new Properties();
						mProps.load(is);
						d = DictionaryUtils.clone(mProps);
						prefMgr.newPreference(IPreferenceManager.SYSTEM_PREFERENCE_NAME,d);
					} catch (Throwable e) {
						log.error("Failed to load server.properties to system preferences", e);
					}
				}
			}
		});
		super.start();
	}
	@Override
	public String getApplicationName() {
			return "trading";
	}
	
	@Override
	public String getUserAgentString() {
		return this.userAgent;
	}

	/* (non-Javadoc)
	 * @see com.wxxr.mobile.android.app.AndroidFramework#getDataDir(java.lang.String, int)
	 */
	@Override
	public File getDataDir(String name, int mode) {
		File dataDir;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			dataDir= new File(Environment.getExternalStorageDirectory(),
					name);
		} else {
			dataDir= getAndroidApplication().getDir(name, mode);
		}
		return dataDir;
	}
	

}
