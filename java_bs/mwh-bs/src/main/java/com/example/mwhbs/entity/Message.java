package com.example.mwhbs.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Message implements Serializable {
    private Long id;
    private Long userId;
    private User fromU;
    private Long idleId;
    private IdleItem idle;
    private String content;
    private Date createTime;
    private Long toUser;
    private User toU;
    private Long toMessage;
    private Message toM;
    private static final long serialVersionUID = 1L;

}