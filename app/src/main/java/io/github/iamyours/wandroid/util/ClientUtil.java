package io.github.iamyours.wandroid.util;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.github.iamyours.wandroid.App;
import io.github.iamyours.wandroid.net.CacheInterceptor;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ClientUtil {
    public static OkHttpClient getUnsafeOkHttpClient() {
        File file = new File(App.instance.getCacheDir(), "http-cache");
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        try {
            setUnsafe(builder);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        OkHttpClient okHttpClient = builder
                .cache(new Cache(file, 1024 * 1024 * 20))
                .addNetworkInterceptor(new CacheInterceptor()).build();
        return okHttpClient;
    }

    public static void setUnsafe(OkHttpClient.Builder builder) throws NoSuchAlgorithmException, KeyManagementException {
        // Create a trust manager that does not validate certificate chains
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }
        };
        final TrustManager[] trustAllCerts = new TrustManager[]{
                trustManager
        };

        // Install the all-trusting trust manager
        final SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts,
                new java.security.SecureRandom());
        // Create an ssl socket factory with our all-trusting manager
        final SSLSocketFactory sslSocketFactory =
                new SSLSocketFactoryCompat(trustManager);

        builder.sslSocketFactory(sslSocketFactory, trustManager);
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
    }
}
