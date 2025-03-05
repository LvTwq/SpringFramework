package com.lvmc.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author 吕茂陈
 * @description
 * @date 2024/10/14 15:21
 */
@XmlRootElement(name = "dnsCommand")
@Getter(onMethod_ = {@XmlElement})
@Setter
@ToString
public class DnsCommandVo {

    private String dnsId;
    private String randVal;
}
