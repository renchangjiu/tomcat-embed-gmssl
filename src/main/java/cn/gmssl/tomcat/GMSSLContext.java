//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cn.gmssl.tomcat;

import cn.gmssl.jsse.provider.GMJSSEConf;
import java.security.KeyManagementException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashSet;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSessionContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509KeyManager;
import javax.net.ssl.X509TrustManager;
import org.apache.tomcat.util.net.SSLContext;

class GMSSLContext implements SSLContext {
    private javax.net.ssl.SSLContext context;
    private KeyManager[] kms;
    private TrustManager[] tms;

    GMSSLContext(String var1) throws Exception {
        if (GMUtil.DEBUG) {
            System.out.println("init2...");
        }

        if (GMUtil.DEBUG) {
            System.out.println("protocol=" + var1);
        }

        this.context = javax.net.ssl.SSLContext.getInstance(var1, "GMJSSE");
    }

    public void init(KeyManager[] var1, TrustManager[] var2, SecureRandom var3) throws KeyManagementException {
        if (GMUtil.DEBUG) {
            System.out.println("init... kms=" + var1 + ",tms=" + var2);
        }

        this.kms = var1;
        this.tms = var2;
        this.context.init(var1, var2, var3);
    }

    public void destroy() {
    }

    public SSLSessionContext getServerSessionContext() {
        if (GMUtil.DEBUG) {
            System.out.println("getServerSessionContext...");
        }

        return this.context.getServerSessionContext();
    }

    public SSLEngine createSSLEngine() {
        if (GMUtil.DEBUG) {
            System.out.println("createSSLEngine...");
        }

        SSLEngine var1 = this.context.createSSLEngine();
        if (GMUtil.DEBUG) {
            System.out.println("createSSLEngine adaptive=" + GMJSSEConf.adaptive);
        }

        if (!GMJSSEConf.adaptive) {
            var1.setEnabledCipherSuites("ECC_SM4_GCM_SM3,ECC_SM4_CBC_SM3,ECDHE_SM4_GCM_SM3,ECDHE_SM4_CBC_SM3".split(","));
            var1.setEnabledProtocols("GMSSLv1.1".split(","));
        }

        return var1;
    }

    public SSLServerSocketFactory getServerSocketFactory() {
        if (GMUtil.DEBUG) {
            System.out.println("getServerSocketFactory...");
        }

        return this.context.getServerSocketFactory();
    }

    public SSLParameters getSupportedSSLParameters() {
        if (GMUtil.DEBUG) {
            System.out.println("getSupportedSSLParameters...");
        }

        SSLParameters var1 = this.context.getSupportedSSLParameters();
        if (GMUtil.DEBUG) {
            System.out.println("getSupportedSSLParameters ok.");
            String[] var2 = var1.getProtocols();

            for(int var3 = 0; var3 < var2.length; ++var3) {
                System.out.println("getSupportedSSLParameters protocol=" + var2[var3]);
            }
        }

        return var1;
    }

    public X509Certificate[] getCertificateChain(String var1) {
        if (GMUtil.DEBUG) {
            System.out.println("getCertificateChain...");
        }

        X509Certificate[] var2 = null;
        if (this.kms != null) {
            for(int var3 = 0; var3 < this.kms.length && var2 == null; ++var3) {
                if (this.kms[var3] instanceof X509KeyManager) {
                    var2 = ((X509KeyManager)this.kms[var3]).getCertificateChain(var1);
                }
            }
        }

        return var2;
    }

    public X509Certificate[] getAcceptedIssuers() {
        if (GMUtil.DEBUG) {
            System.out.println("getAcceptedIssuers...");
        }

        HashSet var1 = new HashSet();
        if (this.tms != null) {
            TrustManager[] var5;
            int var4 = (var5 = this.tms).length;

            for(int var3 = 0; var3 < var4; ++var3) {
                TrustManager var2 = var5[var3];
                if (var2 instanceof X509TrustManager) {
                    X509Certificate[] var6 = ((X509TrustManager)var2).getAcceptedIssuers();
                    if (var6 != null) {
                        var1.addAll(Arrays.asList(var6));
                    }
                }
            }
        }

        return (X509Certificate[])var1.toArray(new X509Certificate[0]);
    }
}
