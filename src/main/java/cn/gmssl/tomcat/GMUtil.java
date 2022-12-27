//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cn.gmssl.tomcat;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Vector;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.net.SSLContext;
import org.apache.tomcat.util.net.SSLHostConfigCertificate;
import org.apache.tomcat.util.net.SSLUtilBase;
import org.apache.tomcat.util.net.openssl.ciphers.Cipher;
import org.apache.tomcat.util.res.StringManager;

public class GMUtil extends SSLUtilBase {
    public static boolean DEBUG = false;
    private static final Log log = LogFactory.getLog(GMUtil.class);
    private static final StringManager sm = StringManager.getManager(GMUtil.class);
    private static final Set<String> implementedProtocols;
    private static final Set<String> implementedCiphers;
    private SSLHostConfigCertificate conf;

    static {
        GMSSLContext var0 = null;

        try {
            var0 = new GMSSLContext("GMSSLv1.1");
            var0.init((KeyManager[])null, (TrustManager[])null, (SecureRandom)null);
        } catch (Exception var7) {
            var7.printStackTrace();
            throw new IllegalArgumentException(var7);
        }

        String[] var1 = var0.getSupportedSSLParameters().getProtocols();
        implementedProtocols = new HashSet(var1.length);
        String[] var5 = var1;
        int var4 = var1.length;

        for(int var3 = 0; var3 < var4; ++var3) {
            String var2 = var5[var3];
            String var6 = var2.toUpperCase(Locale.ENGLISH);
            if (!"SSLV2HELLO".equals(var6) && !"SSLV3".equals(var6) && var6.contains("SSL")) {
                log.debug(sm.getString("jsse.excludeProtocol", new Object[]{var2}));
            } else {
                implementedProtocols.add(var2);
            }
        }

        if (implementedProtocols.size() == 0) {
            log.warn(sm.getString("jsse.noDefaultProtocols"));
        }

        String[] var8 = var0.getSupportedSSLParameters().getCipherSuites();
        implementedCiphers = new HashSet(var8.length);
        implementedCiphers.addAll(Arrays.asList(var8));
    }

    public GMUtil(SSLHostConfigCertificate var1) {
        this(var1, true);
    }

    public GMUtil(SSLHostConfigCertificate var1, boolean var2) {
        super(var1, var2);
        this.conf = null;
        this.conf = var1;
    }

    protected Log getLog() {
        return log;
    }

    public KeyManager[] getKeyManagers() throws Exception {
        String var1 = null;
        String var2 = null;
        String var3 = null;
        var1 = this.conf.getCertificateKeystoreFile();
        var2 = this.conf.getCertificateKeystorePassword();
        var3 = this.conf.getCertificateKeystoreType();
        String var4 = this.conf.getSSLHostConfig().getCiphers();
        if (DEBUG) {
            System.out.println("getKeyManagers...");
            System.out.println("keystoreFile=" + var1);
            System.out.println("keystorePass=" + var2);
            System.out.println("keystoreType=" + var3);
            System.out.println("ciphers=" + var4);
            LinkedHashSet var5 = this.conf.getSSLHostConfig().getCipherList();
            Iterator var6 = var5.iterator();

            while(var6.hasNext()) {
                System.out.println("ciphersx=" + var6.next());
            }
        }

        KeyManager[] var9 = null;

        try {
            if (DEBUG) {
                System.out.println("xxx pfx keystoreFile=" + var1);
            }

            if (var1.startsWith("file:")) {
                var1 = var1.substring(5);
            }

            KeyStore var10 = KeyStore.getInstance(var3, "GMJCE");
            var10.load(new FileInputStream(var1), var2.toCharArray());
            if (DEBUG) {
                System.out.println("xxx pfx size=" + var10.size());
            }

            if (var10 != null) {
                KeyManagerFactory var7 = KeyManagerFactory.getInstance("SunX509");
                var7.init(var10, var2.toCharArray());
                var9 = var7.getKeyManagers();
            }
        } catch (Exception var8) {
            var8.printStackTrace();
        }

        if (DEBUG) {
            System.out.println("getKeyManagers kms=" + var9);
        }

        return var9;
    }

    public TrustManager[] getTrustManagers() throws Exception {
        if (DEBUG) {
            System.out.println("getTrustManagers...");
        }

        TrustManager[] var1 = null;
        var1 = new TrustManager[]{new GMTrustManager()};
        if (DEBUG) {
            System.out.println("getTrustManagers tms=" + var1);
        }

        return var1;
    }

    protected Set<String> getImplementedProtocols() {
        if (DEBUG) {
            Iterator var1 = implementedProtocols.iterator();

            while(var1.hasNext()) {
                System.out.println("implementedProtocol=" + var1.next());
            }
        }

        return implementedProtocols;
    }

    protected Set<String> getImplementedCiphers() {
        if (DEBUG) {
            Iterator var1 = implementedCiphers.iterator();

            while(var1.hasNext()) {
                System.out.println("implementedCipher=" + var1.next());
            }
        }

        return implementedCiphers;
    }

    public String[] getEnabledProtocols() {
        String[] var1 = super.getEnabledProtocols();
        if (DEBUG) {
            for(int var2 = 0; var2 < var1.length; ++var2) {
                System.out.println("getEnabledProtocolsx1 [" + var2 + "]=" + var1[var2]);
            }
        }

        String[] var4 = new String[var1.length + 2];

        int var3;
        for(var3 = 0; var3 < var1.length; ++var3) {
            var4[var3] = var1[var3];
        }

        var4[var1.length] = "GMSSLv1.1";
        var4[var1.length + 1] = "TLSv1.2";
        if (DEBUG) {
            for(var3 = 0; var3 < var4.length; ++var3) {
                System.out.println("getEnabledProtocolsx2 [" + var3 + "]=" + var4[var3]);
            }
        }

        return var4;
    }

    public String[] getEnabledCiphers() {
        String[] var1 = super.getEnabledCiphers();
        if (DEBUG) {
            for(int var2 = 0; var2 < var1.length; ++var2) {
                System.out.println("getEnabledCiphersx1 [" + var2 + "]=" + var1[var2]);
            }
        }

        Vector var6 = new Vector();

        for(int var3 = 0; var3 < var1.length; ++var3) {
            if (var1[var3].indexOf("ECDSA") == -1 && var1[var3].indexOf("_DSS_") == -1) {
                var6.addElement(var1[var3]);
            }
        }

        LinkedHashSet var7 = this.conf.getSSLHostConfig().getCipherList();
        if (DEBUG) {
            if (var7 == null) {
                System.out.println("getCipherList is null");
            } else {
                Iterator var4 = var7.iterator();

                while(var4.hasNext()) {
                    String var5 = ((Cipher)var4.next()).toString();
                    System.out.println("getCipherList=" + var5);
                }
            }
        }

        String var8 = this.conf.getSSLHostConfig().getCiphers();
        if (DEBUG) {
            System.out.println("getCiphers=" + var8);
        }

        var6.addElement("SSL_RSA_WITH_3DES_EDE_CBC_SHA");
        var6.addElement("TLS_RSA_WITH_AES_128_CBC_SHA256");
        var6.addElement("ECC_SM4_GCM_SM3");
        var6.addElement("ECC_SM4_CBC_SM3");
        var6.addElement("ECDHE_SM4_GCM_SM3");
        var6.addElement("ECDHE_SM4_CBC_SM3");
        var1 = new String[var6.size()];

        int var9;
        for(var9 = 0; var9 < var6.size(); ++var9) {
            var1[var9] = (String)var6.elementAt(var9);
        }

        if (DEBUG) {
            System.out.println("getCiphers=" + var8);

            for(var9 = 0; var9 < var1.length; ++var9) {
                System.out.println("getEnabledCiphersx2 [" + var9 + "]=" + var1[var9]);
            }
        }

        return var1;
    }

    protected boolean isTls13RenegAuthAvailable() {
        return false;
    }

    public SSLContext createSSLContextInternal(List<String> var1) throws Exception {
        return new GMSSLContext(this.sslHostConfig.getSslProtocol());
    }
}
