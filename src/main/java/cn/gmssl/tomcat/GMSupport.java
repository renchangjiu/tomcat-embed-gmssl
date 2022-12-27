//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cn.gmssl.tomcat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.net.ssl.SSLSession;
import org.apache.tomcat.util.net.SSLSessionManager;
import org.apache.tomcat.util.net.SSLSupport;
import org.apache.tomcat.util.net.openssl.ciphers.Cipher;

public class GMSupport implements SSLSupport, SSLSessionManager {
    private static final Map<String, Integer> keySizeCache = new HashMap();
    private SSLSession session;

    static {
        Cipher[] var3;
        int var2 = (var3 = Cipher.values()).length;

        for(int var1 = 0; var1 < var2; ++var1) {
            Cipher var0 = var3[var1];
            Iterator var5 = var0.getJsseNames().iterator();

            while(var5.hasNext()) {
                String var4 = (String)var5.next();
                keySizeCache.put(var4, var0.getStrength_bits());
            }
        }

    }

    static void init() {
    }

    public GMSupport(SSLSession var1) {
        this.session = var1;
    }

    public String getCipherSuite() throws IOException {
        if (this.session == null) {
            return null;
        } else {
            String var1 = this.session.getCipherSuite();
            return var1;
        }
    }

    public X509Certificate[] getPeerCertificateChain() throws IOException {
        if (this.session == null) {
            return null;
        } else {
            Certificate[] var1 = null;

            try {
                var1 = this.session.getPeerCertificates();
            } catch (Throwable var8) {
                var8.printStackTrace();
                return null;
            }

            if (var1 == null) {
                return null;
            } else {
                X509Certificate[] var2 = new X509Certificate[var1.length];

                for(int var3 = 0; var3 < var1.length; ++var3) {
                    if (var1[var3] instanceof X509Certificate) {
                        var2[var3] = (X509Certificate)var1[var3];
                    } else {
                        try {
                            byte[] var4 = var1[var3].getEncoded();
                            CertificateFactory var5 = CertificateFactory.getInstance("X.509");
                            ByteArrayInputStream var6 = new ByteArrayInputStream(var4);
                            var2[var3] = (X509Certificate)var5.generateCertificate(var6);
                        } catch (Exception var7) {
                            var7.printStackTrace();
                            return null;
                        }
                    }
                }

                if (var2.length < 1) {
                    return null;
                } else {
                    return var2;
                }
            }
        }
    }

    public Integer getKeySize() throws IOException {
        return this.session == null ? null : (Integer)keySizeCache.get(this.session.getCipherSuite());
    }

    public String getSessionId() throws IOException {
        if (this.session == null) {
            return null;
        } else {
            byte[] var1 = this.session.getId();
            if (var1 == null) {
                return null;
            } else {
                StringBuilder var2 = new StringBuilder();
                byte[] var6 = var1;
                int var5 = var1.length;

                for(int var4 = 0; var4 < var5; ++var4) {
                    byte var3 = var6[var4];
                    String var7 = Integer.toHexString(var3);
                    if (var7.length() < 2) {
                        var2.append('0');
                    }

                    if (var7.length() > 2) {
                        var7 = var7.substring(var7.length() - 2);
                    }

                    var2.append(var7);
                }

                return var2.toString();
            }
        }
    }

    public void setSession(SSLSession var1) {
        this.session = var1;
    }

    public void invalidateSession() {
        this.session.invalidate();
    }

    public String getProtocol() throws IOException {
        if (this.session == null) {
            if (GMUtil.DEBUG) {
                System.out.println("getProtocol=null");
            }

            return null;
        } else {
            String var1 = this.session.getProtocol();
            if (GMUtil.DEBUG) {
                System.out.println("getProtocol=" + var1);
            }

            return var1;
        }
    }

    @Override
    public String getRequestedProtocols() throws IOException {
        return null;
    }

    @Override
    public String getRequestedCiphers() throws IOException {
        return null;
    }
}
