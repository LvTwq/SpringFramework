package com.southwind.service;

import com.southwind.http.SSLSocketClient;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;

@Slf4j
@SpringBootTest
class HttpClientTest {

    @Test
    void httpClient() throws IOException {

        OkHttpClient client = SSLSocketClient.getUnsafeOkHttpClient();

        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file",
                        "/2018-02-04_123955.png",
                        RequestBody.create(MediaType.parse("application/octet-stream"), new File("/2018-02-04_123955.png"))
                )
                .build();
        Request request = new Request.Builder()
                .url("https://47.114.164.148/device-report/clientlogs/upload/systeminfo?SystemOS=Windows")
                .method("POST", body)
                .build();
        Response response = client.newCall(request).execute();
        log.info("调用结果 {}", response.body().string());
    }


}