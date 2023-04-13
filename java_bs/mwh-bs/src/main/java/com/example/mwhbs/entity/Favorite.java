package com.example.mwhbs.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class Favorite implements Serializable {
    private Long id;
    private Date createTime;
    private Long userId;
    private Long idleId;
    private IdleItem idleItem;
    private static final long serialVersionUID = 1L;

}