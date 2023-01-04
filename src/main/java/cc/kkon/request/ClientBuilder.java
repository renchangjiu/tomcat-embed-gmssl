package cc.kkon.request;

import cn.gmssl.jsse.provider.GMJSSE;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * 使用HttpClient访问国密https
 *
 * @author gmssl.cn
 */
public class ClientBuilder {


    // 创建SSL上下文---忽略服务端证书信任
    static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException, NoSuchProviderException {
        SSLContext sc = SSLContext.getInstance(GMJSSE.GMSSLv11, GMJSSE.NAME);

        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString) throws CertificateException {
                for (int i = 0; i < paramArrayOfX509Certificate.length; i++) {
                    System.out.println(paramArrayOfX509Certificate[i].getSubjectDN().getName());
                }
                System.out.println();
            }

            @Override
            public void checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString) throws CertificateException {
                for (int i = 0; i < paramArrayOfX509Certificate.length; i++) {
                    System.out.println(paramArrayOfX509Certificate[i].getSubjectDN().getName());
                }
                System.out.println();
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        sc.init(null, new TrustManager[]{trustManager}, null);
        return sc;
    }

    static HttpClient initGMSSL() {
        try {
            Security.insertProviderAt(new cn.gmssl.jce.provider.GMJCE(), 1);
            Security.insertProviderAt((Provider) Class.forName("cn.gmssl.jsse.provider.GMJSSE").newInstance(), 2);

            SSLContext sslContext = createIgnoreVerifySSL();

            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,
                    new String[]{"GMSSLv1.1"}, new String[]{"ECC_SM4_CBC_SM3"},
                    SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            Registry<ConnectionSocketFactory> socketFactoryRegistry =
                    RegistryBuilder.<ConnectionSocketFactory>create()
                            .register("https", sslsf).build();

            int timeout = 30;
            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(timeout * 1000)
                    .setConnectionRequestTimeout(timeout * 1000)
                    .setSocketTimeout(timeout * 1000).build();

            HttpClientBuilder b = HttpClientBuilder.create()
                    .setConnectionManager(new BasicHttpClientConnectionManager(socketFactoryRegistry))
                    .setMaxConnPerRoute(20)
                    .setMaxConnTotal(400)
                    .setDefaultRequestConfig(config);
            return b.build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


}

