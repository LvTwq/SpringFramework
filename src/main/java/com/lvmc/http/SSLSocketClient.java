package com.lvmc.http;


import lombok.experimental.UtilityClass;
import okhttp3.OkHttpClient;

import javax.net.ssl.*;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

/**
 * @author 吕茂陈
 * @date 2022-07-14 08:47
 */
@UtilityClass
public class SSLSocketClient {

    public OkHttpClient getUnsafeOkHttpClient() {

        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[]{};
                        }
                    }
            };

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
