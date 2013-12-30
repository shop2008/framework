package com.wxxr.stock.restful.resource;

import java.security.KeyStore;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HostnameVerifier;

import junit.framework.TestCase;

import com.wxxr.mobile.core.api.IUserAuthCredential;
import com.wxxr.mobile.core.api.IUserAuthManager;
import com.wxxr.mobile.core.microkernel.api.IKernelContext;
import com.wxxr.mobile.core.rpc.http.apache.AbstractHttpRpcService;
import com.wxxr.mobile.core.rpc.http.api.HttpRequest;
import com.wxxr.mobile.core.security.api.ISiteSecurityService;
import com.wxxr.mobile.stock.app.MockApplication;
import com.wxxr.mobile.stock.app.MockRestClient;
import com.wxxr.stock.restful.json.ClientInfoVO;

public class ClientResourceTest extends TestCase {

   private ClientResource resource;

   private String appName;

   private String appVer;

   @Override
   protected void setUp() throws Exception {
      super.setUp();
      init();

   }

   @Override
   protected void tearDown() throws Exception {
      super.tearDown();
      resource = null;
   }

   protected String getAppName() {
      return ClientResourceTest.this.appName;
   }

   protected String getAppVersion() {
      return ClientResourceTest.this.appVer;
   }

   protected void init() {

      MockApplication app = new MockApplication() {
         ExecutorService executor = Executors.newFixedThreadPool(3);

         @Override
         public ExecutorService getExecutorService() {
            return executor;
         }

         @Override
         protected void initModules() {

         }

      };

      IKernelContext context = app.getContext();
      context.registerService(IUserAuthManager.class, new IUserAuthManager() {
         @Override
         public IUserAuthCredential getAuthCredential(String host, String realm) {
            return new IUserAuthCredential() {

               @Override
               public String getUserName() {
                  return "13500001009";
               }

               @Override
               public String getAuthPassword() {
                  return "404662";
               }

            };
         }
      });
    
      AbstractHttpRpcService service = new AbstractHttpRpcService() {
         public HttpRequest createRequest(String endpointUrl,
               Map<String, Object> params) {
            HttpRequest request = super.createRequest(endpointUrl, params);
            request.setHeader("appName", getAppName());
            request.setHeader("appVer", getAppVersion());
            request.setHeader("deviceid", "00-1E-90-B1-C3-57");
            request.setHeader("deviceType", "2");
            return request;
         }
      };
      service.setEnablegzip(false);
      service.startup(context);
      context.registerService(ISiteSecurityService.class,
            new ISiteSecurityService() {

               @Override
               public KeyStore getTrustKeyStore() {
                  return null;
               }

               @Override
               public KeyStore getSiteKeyStore() {
                  return null;
               }

               @Override
               public HostnameVerifier getHostnameVerifier() {
                  return null;
               }
            });

      MockRestClient builder = new MockRestClient();
      builder.init(context);
      resource = builder.getRestService(ClientResource.class,
            "http://192.168.123.44:8480/mobilestock2");
   }

  
   
   
   public void testGetClientInfo2() throws Exception {
       this.appName = "trading";
       this.appVer = "1.0.0-SNAPSHOT";
       ClientInfoVO vo = resource.getClientInfo();
       System.out.println(vo);
      
    }
 
}
