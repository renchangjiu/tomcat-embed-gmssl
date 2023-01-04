# tomcat-embed-gmssl
支持国密 HTTPS 的嵌入式 Tomcat 容器，基于原版 embed tomcat 修改。

版本号说明：x 之前的版本号即为原始 embed tomcat 的版本号

修改内容: 
1. 增加了 `cn.gmssl.tomcat` 包下的五个类。
2. 增加了基于 http client 库的、支持国密 https 的请求工具类(可选)。


#### 使用方法(spring boot)：
```xml

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
    <version>${tomcat-embed-gmssl.version}</version>
</dependency>
```

#### maven 地址
```xml
<dependency>
    <groupId>cc.kkon</groupId>
    <artifactId>tomcat-embed-gmssl</artifactId>
    <version>9.0.63.x.2</version>
</dependency>
<!-- 可选 -->
<dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpclient</artifactId>
    <version>4.5.13</version>
</dependency>
```
