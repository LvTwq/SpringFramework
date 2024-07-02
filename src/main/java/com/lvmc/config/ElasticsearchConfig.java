

package com.lvmc.config;

import java.io.IOException;

import lombok.Getter;
import lombok.Setter;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 吕茂陈
 * @description
 * @date 2023/6/29 9:25
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.elasticsearch")
public class ElasticsearchConfig {

    private String host;
    private int port;
    private String username;
    private String password;
    private String schema;
    private int connectTimeOut;
    private int socketTimeOut;
    private int connectionRequestTimeOut;
    private int maxConnectNum;
    private int maxConnectPerRoute;
    private RestClientBuilder builder;
    private RestHighLevelClient restHighLevelClient;
    private RestClient restClient;


    /**
     * 低级操作client
     *
     * @return {@link RestClient}
     */
    @Bean
    public RestClient restLowLevelClient() {
        if (getClient(restClient != null)) {
            return restClient;
        }
        restClient = builder.build();
        return restClient;
    }

    /**
     * Bean name default  函数名字
     *
     * @return
     */
    @Bean
    public RestHighLevelClient restHighLevelClient() {
        if (getClient(restHighLevelClient != null)) {
            return restHighLevelClient;
        }
        restHighLevelClient = new RestHighLevelClient(builder);
        return restHighLevelClient;
    }



    private boolean getClient(boolean b) {
        if (b) {
            return true;
        }
        String[] a1 = host.split(",");
        HttpHost[] hosts = new HttpHost[a1.length];
        for (int i = 0; i < a1.length; i++) {
            hosts[i] = new HttpHost(a1[i], port, "http");
        }
        builder = RestClient.builder(hosts);
        final CredentialsProvider credentialsProvider =
            new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
            new UsernamePasswordCredentials(username, password));
        builder.setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
            .setDefaultCredentialsProvider(credentialsProvider)
            .setMaxConnTotal(maxConnectNum)
            .setMaxConnPerRoute(maxConnectPerRoute));
        builder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(connectTimeOut);
            requestConfigBuilder.setSocketTimeout(socketTimeOut);
            requestConfigBuilder.setConnectionRequestTimeout(connectionRequestTimeOut);
            return requestConfigBuilder;
        });
        Header[] defaultHeaders = new Header[]{new BasicHeader("Content-Type", "application/json")};
        builder.setDefaultHeaders(defaultHeaders);
        return false;
    }

    /**
     * 关闭连接
     */
    public void close() {
        if (restHighLevelClient != null) {
            try {
                restClient.close();
                restHighLevelClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
