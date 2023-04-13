package com.example.mwhbs.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class User implements Serializable {

    private Long id;
    private String accountNumber;
    private String userPassword;
    private String nickname;
    private String avatar;
    private Date signInTime;
    private Byte userStatus;
    private static final long serialVersionUID = 1L;


}