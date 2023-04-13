package com.example.mwhbs.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderAddress implements Serializable {
    private Long id;
    private Long orderId;
    private String consigneeName;
    private String consigneePhone;
    private String detailAddress;
    private static final long serialVersionUID = 1L;

}