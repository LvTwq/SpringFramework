package com.southwind.service;

import cn.hutool.core.io.resource.ResourceUtil;
import okhttp3.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@SpringBootTest
class AccountServiceTest {

    @Test
    void httpClient() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        InputStream inputStream = ResourceUtil.getStream("fyajReq.txt");
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, new File("sql/account.sql"));
        Request request = new Request.Builder()
                .url("http://192.168.101.182:8099/enapi/upload/systeminfo?SystemOS=Windows")
                .method("POST", body)
                .build();
        Response response = client.newCall(request).execute();
    }

}