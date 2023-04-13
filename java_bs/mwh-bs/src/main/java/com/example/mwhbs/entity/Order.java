package com.example.mwhbs.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Order implements Serializable {

    private Long id;
    private String orderNumber;
    private Long userId;
    private User user;
    private Long idleId;
    private IdleItem idleItem;
    private BigDecimal orderPrice;
    private Byte paymentStatus;
    private String paymentWay;
    private Date createTime;
    private Date paymentTime;
    private Byte orderStatus;
    private Byte isDeleted;
    private static final long serialVersionUID = 1L;

}