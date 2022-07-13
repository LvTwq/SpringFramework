package com.southwind.task;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.southwind.vo.FyajVo;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author 吕茂陈
 * @date 2021/11/22 10:51
 */
@Slf4j
@Component
public class FyAjTask {

    @Value("${custom.fyaj.url}")
    private String fyajUrl;

    /**
     * 每天0点调用第三方接口，获取法院案件的增量数据
     */
//    @Scheduled(fixedRate = 10000)
    public void gainFyAjTask() {
        // 形如 2021-10-10
        String today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        String requestBody = String.format(ResourceUtil.readUtf8Str("fyajReq.txt"), "2021-12-27");

        String data = excurl(requestBody, fyajUrl);
        if (StringUtils.isEmpty(data)) {
            log.error("调用对方{}接口无响应，可能对方服务异常", fyajUrl);
            return;
        }
        List<FyajVo> fyajVoList = xml2Bean(data);
        log.info("转换过的实体{}", fyajVoList);
    }


    /**
     * 解析拿到的响应(xml)，并转成 List<FyajVo>
     *
     * @param data
     * @return
     */
    private List<FyajVo> xml2Bean(String data) {
        JSONObject jsonObject = JSONUtil.parseFromXml(data);
        JSONObject enveElement = (JSONObject) jsonObject.get("soap:Envelope");
        JSONObject bodyElement = (JSONObject) enveElement.get("soap:Body");
        JSONObject resElement = (JSONObject) bodyElement.get("qryMPSqlByKeyResponse");
        // 如果没有案件数据，data 里面是 []
        JSONArray jsonArray = JSONUtil.parseArray(JSONUtil.parse(resElement.get("String")).getByPath("data"));
        return JSONUtil.toList(jsonArray, FyajVo.class);
    }


    /**
     * 执行请求
     *
     * @return
     */
    private String excurl(String requestBody, String url) {
        log.info("=============================开始调用第三方接口获取法院数据=============================");

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("text/xml; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, requestBody);

        StopWatch sw = new StopWatch();
        sw.start("调用法院案件接口");
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Content-Type", "text/xml; charset=utf-8")
                .build();
        String responseBody = "";
        try (Response response = client.newCall(request).execute()) {
            responseBody = response.body().string();
        } catch (IOException e) {
            log.error("使用HttpClient调用{}失败", url, e);
        }
        sw.stop();
        log.info("调用接口花费了{}ms", sw.getTotalTimeMillis());
        log.info("通过curl调用到的响应体为：{}", responseBody);

        return responseBody;
    }

    //    @Scheduled(fixedRate = 10000)
    public void gainFyAjTask02() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("text/xml; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://service.fgw.bm.com/\"><soapenv:Header /><soapenv:Body><ser:qryMPSqlByKey><ser:key>f964a5718b3b400aaf54d0baecbca609</ser:key><ser:inParams>updateTime=2021-10-27</ser:inParams></ser:qryMPSqlByKey></soapenv:Body></soapenv:Envelope>");
        Request request = new Request.Builder()
                .url("http://2.208.64.254:8090/channel/gG2foEk5lz/services/FgwServices?wsdl")
                .method("POST", body)
                .addHeader("Content-Type", "text/xml; charset=utf-8")
                .build();
        Response response = client.newCall(request).execute();
        String string = response.body().string();
        response.close();
        log.info("response:{}", response);
        log.info("string:{}", string);
    }



//    @Scheduled(fixedRate = 10000)
    public void httpClient() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file","/C:/Users/Administrator/Desktop/tmp.txt",
                        RequestBody.create(MediaType.parse("multipart/form-data"),
                                new File("/C:/Users/Administrator/Desktop/tmp.txt")))
                .build();
        Request request = new Request.Builder()
                .url("http://192.168.101.182:8099/enapi/upload/systeminfo?SystemOS=Windows")
                .method("POST", body)
                .build();
        Response response = client.newCall(request).execute();
    }

}




