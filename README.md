# tomcat-embed-gmssl
支持国密 HTTPS 的嵌入式 Tomcat 容器，基于原版 embed tomcat 修改。

版本号说明：x 之前的版本号即为原始 embed tomcat 的版本号

修改内容: 
1. 增加了 `cn.gmssl.tomcat` 包下的五个类。
2. 增加了基于 http client 库的、支持国密 https 的请求工具类(可选)。


#### maven 地址：
```xml
<!-- 如果使用 spring boot -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
        <!-- 排除自带的 tomcat -->
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
        </exclusion>
    </exclusions>
</dependency>

<dependency>
    <groupId>cc.kkon</groupId>
    <artifactId>tomcat-embed-gmssl</artifactId>
    <version>9.0.63.x.6</version>
</dependency>

<!-- 可选 -->
<dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpclient</artifactId>
    <version>4.5.13</version>
</dependency>
```
#### 如何使用:
1. 方式一: 基于 spring boot
   1. yml
    ```yml
    server:
        # spring 限制必须设置该值, 设置为 0, 使用随机端口
        port: 0
    ```
   
   1. config
   ```java
    @Configuration
    public class WebConfig {

        @Bean
        public TomcatServletWebServerFactory getFactory() {
            TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
            Connector conn1 = Tomcats.buildConnector(8001, "classpath:sm2.auth.both.pfx", "12345678", false);
            tomcat.addAdditionalTomcatConnectors(conn1);
            return tomcat;
        }
    }
    ```
2. 方式二: 纯 servlet
```java
    public void listen() throws Exception {
        int port = 0;
        String certPassword = "12345678";
        String cert = "classpath:sm2.auth.both.pfx";

        Tomcat tomcat = Tomcats.buildGmHttpsTomcat(port, cert, certPassword, false);
        Tomcats.addServlet(tomcat, new EchoServlet());
        // add...
        tomcat.start();
        // 阻塞
        tomcat.getServer().await();
    }
```
