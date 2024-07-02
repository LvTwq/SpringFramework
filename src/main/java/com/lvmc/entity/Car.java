package com.lvmc.entity;

import lombok.Data;

/**
 * @author 吕茂陈
 * @description
 * @date 2023/12/14 11:13
 */
@Data
public class Car {

    private long price;
    private String color;
    private String make;
    private String sold;

    public Car(long price, String color, String make, String sold) {
        this.price = price;
        this.color = color;
        this.make = make;
        this.sold = sold;
    }
}
