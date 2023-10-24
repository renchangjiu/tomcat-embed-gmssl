package cc.kkon.utils;

import cn.gmssl.tomcat.GMSSLImplementation;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.http11.Http11NioProtocol;

import javax.servlet.Servlet;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * tomcat utils
 */
public class Tomcats {

    /**
     * @param port       监听端口
     * @param certPath   证书路径, 例: "classpath:sm2.pfx" 或 "/home/sm2.pfx"
     * @param certPasswd 证书密码
     * @param authClient 是否验证客户端证书
     */
    public static Tomcat buildGmHttpsTomcat(int port, String certPath, String certPasswd, boolean authClient) {
        Tomcat tomcat = new Tomcat();

        tomcat.setBaseDir(null);
        String ctxPath = "";
        StandardContext ctx = new StandardContext();

        ctx.setPath(ctxPath);
        ctx.addLifecycleListener(new Tomcat.FixContextListener());
        tomcat.getHost().addChild(ctx);

        tomcat.setConnector(buildConnector(port, certPath, certPasswd, authClient));

        return tomcat;
    }

    public static void addServlet(Tomcat tomcat, Servlet... servlets) {
        if (servlets == null || servlets.length == 0) {
            return;
        }
        Context ctx = (Context) tomcat.getHost().findChildren()[0];
        for (Servlet servlet : servlets) {
            Class<? extends Servlet> clazz = servlet.getClass();
            WebServlet anno = clazz.getAnnotation(WebServlet.class);
            if (anno == null) {
                continue;
            }
            String name = anno.name();
            if (name == null || name.isEmpty()) {
                name = clazz.getName();
            }
            tomcat.addServlet("", name, servlet);
            String[] urlPatterns = anno.value();
            for (String ptn : urlPatterns) {
                ctx.addServletMappingDecoded(ptn, name);
            }
        }
    }

    public static void start(Tomcat tomcat) {
        new Thread(() -> {
            try {
                tomcat.start();
            } catch (LifecycleException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    private static final String CLASSPATH = "classpath:";


    /**
     * @param port       监听端口
     * @param certPath   证书路径, 例: "classpath:sm2.pfx" 或 "/home/sm2.pfx"
     * @param certPasswd 证书密码
     * @param authClient 是否验证客户端证书
     */
    public static Connector buildConnector(int port, String certPath, String certPasswd, boolean authClient) {
        if (certPath.startsWith(CLASSPATH)) {
            certPath = copyCertToTempDir(certPath);
        }
        Connector conn = new Connector(Http11NioProtocol.class.getName());
        conn.setPort(port);
        conn.setScheme("https");
        conn.setURIEncoding(StandardCharsets.UTF_8.name());

        Http11NioProtocol pro = (Http11NioProtocol) conn.getProtocolHandler();
        if (authClient) {
            pro.setClientAuth("true");
        }
        pro.setMinSpareThreads(10);
        pro.setMaxThreads(200);
        pro.setMaxConnections(10000);
        pro.setAcceptCount(100);

        pro.setSSLEnabled(true);
        pro.setSslImplementationName(GMSSLImplementation.class.getName());
        pro.setKeystoreType("PKCS12");
        pro.setKeystoreFile(certPath);
        pro.setKeystorePass(certPasswd);
        return conn;
    }


    private static String copyCertToTempDir(String certPath) {
        certPath = certPath.replace(CLASSPATH, "");
        InputStream certIn = Tomcats.class.getClassLoader().getResourceAsStream(certPath);
        try {
            Path tmp = Files.createTempFile("sm2.pfx", ".tmp");
            Files.copy(certIn, tmp, StandardCopyOption.REPLACE_EXISTING);
            return tmp.toFile().getAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
