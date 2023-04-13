package com.example.mwhbs.entity;

import lombok.Data;
import java.io.Serializable;

@Data
public class Address implements Serializable {
    private Long id;
    private String consigneeName;
    private String consigneePhone;
    private String provinceName;
    private String cityName;
    private String regionName;
    private String detailAddress;
    private Boolean defaultFlag;
    private Long userId;
    private static final long serialVersionUID = 1L;

}