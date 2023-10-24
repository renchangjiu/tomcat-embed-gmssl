package cc.kkon;

import cc.kkon.utils.Tomcats;
import org.apache.catalina.startup.Tomcat;
import org.junit.Test;

/**
 * @author yui
 */
public class Tests {


    @Test
    public void listen() throws Exception {
        int port = 0;
        String certPassword = "12345678";
        String cert = "classpath:sm2.auth.both.pfx";

        Tomcat tomcat = Tomcats.buildGmHttpsTomcat(port, cert, certPassword, false);
        Tomcats.addServlet(tomcat, new EchoServlet());
        tomcat.start();
        // 阻塞
        tomcat.getServer().await();
    }
}
