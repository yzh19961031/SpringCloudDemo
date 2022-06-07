package com.yzh.client1.bean;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author yuanzhihao
 * @since 2022/1/28
 */
@Data
@Accessors(chain = true)
public class Account {
    private int id;
    private String name;
    private double balance;
}
