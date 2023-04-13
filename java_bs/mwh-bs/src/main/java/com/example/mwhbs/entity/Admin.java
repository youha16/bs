package com.example.mwhbs.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Admin implements Serializable {
    private Long id;
    private String accountNumber;
    private String adminPassword;
    private String adminName;
    private static final long serialVersionUID = 1L;

}