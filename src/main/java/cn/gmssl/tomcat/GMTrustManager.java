//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cn.gmssl.tomcat;

import java.net.Socket;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.X509ExtendedTrustManager;
import javax.net.ssl.X509TrustManager;

class GMTrustManager extends X509ExtendedTrustManager {
    protected X509TrustManager tm = null;

    public GMTrustManager() {
    }

    public GMTrustManager(List<X509Certificate> var1) {
    }

    public void checkClientTrusted(X509Certificate[] var1, String var2) throws CertificateException {
    }

    public void checkServerTrusted(X509Certificate[] var1, String var2) throws CertificateException {
    }

    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

    public void checkClientTrusted(X509Certificate[] var1, String var2, Socket var3) throws CertificateException {
        this.checkClientTrusted(var1, var2);
    }

    public void checkClientTrusted(X509Certificate[] var1, String var2, SSLEngine var3) throws CertificateException {
    }

    public void checkServerTrusted(X509Certificate[] var1, String var2, Socket var3) throws CertificateException {
    }

    public void checkServerTrusted(X509Certificate[] var1, String var2, SSLEngine var3) throws CertificateException {
    }
}
