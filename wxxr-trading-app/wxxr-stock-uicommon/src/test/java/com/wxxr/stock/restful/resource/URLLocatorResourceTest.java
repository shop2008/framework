package com.wxxr.stock.restful.resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.KeyStore;
import java.util.Map;

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

public class URLLocatorResourceTest extends TestCase {

   private IURLLocatorResource resource;

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
      return URLLocatorResourceTest.this.appName;
   }

   protected String getAppVersion() {
      return URLLocatorResourceTest.this.appVer;
   }

   protected void init() {

      MockApplication app = new MockApplication() {

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
      resource = builder.getRestService(IURLLocatorResource.class,null,
            "http://192.168.123.44:8480/mobilestock2");
   }

   public void testGetURLSettings() throws Exception {
      //test snapshot
      System.out.println("===========test snapshot ============");
      this.appName = "trading";
      this.appVer = "1.0.0.SNAPSHOT";
      byte[] a = resource.getURLSettings("");
      assertNotNull(a);
      Map<String, String> urls = fromBytes(a);
      System.out.println(urls);
      // test M
      System.out.println("===========test Milestone ============");
      this.appName = "trading";
      this.appVer = "1.0.0.M1";
      a = resource.getURLSettings("");
      assertNotNull(a);
      urls = fromBytes(a);
      System.out.println(urls);
      
      //test rc
      System.out.println("===========test Release credit ============");
      this.appName = "trading";
      this.appVer = "1.0.0.RC1";
      a = resource.getURLSettings("");
      assertNotNull(a);
      urls = fromBytes(a);
      System.out.println(urls);
      //test error format
      this.appName = "trading";
      this.appVer = "1.0.0.GA";
      a = resource.getURLSettings("");
      assertNull(a);
   }

   private <T> T fromBytes(byte[] data) throws Exception {
      if (data == null) {
         return null;
      }
      ByteArrayInputStream bis = new ByteArrayInputStream(data);
      ObjectInputStream ois = null;
      try {
         ois = new ObjectInputStream(bis);
         return (T) ois.readObject();
      }
      catch (Exception e) {
         throw e;
      }
      finally {
         try {
            if (ois != null) {
               ois.close();
            }
            bis.close();
         }
         catch (IOException e) {
         }
      }

   }
}
