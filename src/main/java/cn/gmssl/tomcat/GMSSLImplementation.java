//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cn.gmssl.tomcat;

import cn.gmssl.jce.provider.GMJCE;
import cn.gmssl.jsse.provider.GMJSSE;
import java.security.Security;
import javax.net.ssl.SSLSession;
import org.apache.tomcat.util.net.SSLHostConfigCertificate;
import org.apache.tomcat.util.net.SSLImplementation;
import org.apache.tomcat.util.net.SSLSupport;
import org.apache.tomcat.util.net.SSLUtil;

public class GMSSLImplementation extends SSLImplementation {
    private static void init() {
        Security.insertProviderAt(new GMJCE(), 1);
        Security.insertProviderAt(new GMJSSE(), 2);
    }

    public GMSSLImplementation() {
        init();
    }

    public SSLSupport getSSLSupport(SSLSession var1) {
        return new GMSupport(var1);
    }

    public SSLUtil getSSLUtil(SSLHostConfigCertificate var1) {
        return new GMUtil(var1);
    }

    public boolean isAlpnSupported() {
        return false;
    }
}
