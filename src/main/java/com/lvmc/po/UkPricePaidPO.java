package com.lvmc.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (UkPricePaid)表实体类
 *
 * @author 吕茂陈
 * @since 2024-11-18 14:22:45
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("uk_price_paid")
public class UkPricePaidPO {

    private String price;

    private String date;

    private String postcode1;

    private String postcode2;

    private String type;

    private boolean isNew;

    private String duration;

    private String addr1;

    private String addr2;

    private String street;

    private String locality;

    private String town;

    private String district;

    private String county;

}
