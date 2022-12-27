package cc.kkon;

import cn.gmssl.tomcat.GMSSLImplementation;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.http11.Http11NioProtocol;
import org.junit.Test;

import java.io.File;

/**
 * @author yui
 */
public class Tests {

    public static final int PORT = 10000;

    @Test
    public void test1() throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir(null);
        String ctxPath = "";
        StandardContext ctx = new StandardContext();

        ctx.setPath(ctxPath);
        ctx.addLifecycleListener(new Tomcat.FixContextListener());
        tomcat.getHost().addChild(ctx);

        Connector connector = new Connector(Http11NioProtocol.class.getName());
        Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
        tomcat.setConnector(connector);
        protocol.setMaxThreads(2);
        connector.setPort(PORT);

        // protocol.setClientAuth("true");
        protocol.setClientAuth("false");

        protocol.setSSLEnabled(true);
        protocol.setSslImplementationName(GMSSLImplementation.class.getName());
        protocol.setKeystoreType("PKCS12");

        File cert = new File(System.getProperty("user.dir"), "/src/test/resources/sm2.auth.both.pfx");
        protocol.setKeystoreFile(cert.getAbsolutePath());
        protocol.setKeystorePass("12345678");

        tomcat.addServlet(ctxPath, "root", new EchoServlet());
        ctx.addServletMappingDecoded("/", "root");
        tomcat.start();
        // tomcat.getServer().await();
    }
}
