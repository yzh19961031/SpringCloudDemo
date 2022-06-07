package com.yzh.server1.bean;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author yuanzhihao
 * @since 2022/1/27
 */
@Data
@Accessors(chain = true)
public class Account {
    private int id;
    private String name;
    private double balance;
}
