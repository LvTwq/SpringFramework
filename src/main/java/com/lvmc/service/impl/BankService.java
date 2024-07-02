package com.lvmc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

/**
 * 把参数拼接在一起，构成一个大字符串
 *
 * @author 吕茂陈
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BankService {

    private final RestTemplate restTemplate;

//    /**
//     * 创建用户方法
//     *
//     * @param name
//     * @param identity
//     * @param mobile
//     * @param age
//     * @return
//     * @throws IOException
//     */
//    public static String createUser(String name, String identity, String mobile, int age) throws IOException {
//        StringBuilder stringBuilder = new StringBuilder();
//        //字符串靠左，多余的地方填充_
//        stringBuilder.append(String.format("%-10s", name).replace(' ', '_'));
//        //字符串靠左，多余的地方填充_
//        stringBuilder.append(String.format("%-18s", identity).replace(' ', '_'));
//        //数字靠右，多余的地方用0填充
//        stringBuilder.append(String.format("%05d", age));
//        //字符串靠左，多余的地方用_填充
//        stringBuilder.append(String.format("%-11s", mobile).replace(' ', '_'));
//        //最后加上MD5作为签名
//        stringBuilder.append(DigestUtils.md2Hex(stringBuilder.toString()));
//        return Request.Post("http://localhost:45678/reflection/bank/createUser")
//                .bodyString(stringBuilder.toString(), ContentType.APPLICATION_JSON)
//                .execute().returnContent().asString();
//    }

//    /**
//     * 支付方法
//     *
//     * @param userId
//     * @param amount
//     * @return
//     * @throws IOException
//     */
//    public static String pay(long userId, BigDecimal amount) throws IOException {
//        StringBuilder stringBuilder = new StringBuilder();
//        //数字靠右，多余的地方用0填充
//        stringBuilder.append(String.format("%020d", userId));
//        //金额向下舍入2位到分，以分为单位，作为数字靠右，多余的地方用0填充
//        stringBuilder.append(String.format("%010d", amount.setScale(2, RoundingMode.DOWN).multiply(new BigDecimal("100")).longValue()));
//        //最后加上MD5作为签名
//        stringBuilder.append(DigestUtils.md2Hex(stringBuilder.toString()));
//        return Request.Post("http://localhost:45678/reflection/bank/pay")
//                .bodyString(stringBuilder.toString(), ContentType.APPLICATION_JSON)
//                .execute().returnContent().asString();
//    }
}
