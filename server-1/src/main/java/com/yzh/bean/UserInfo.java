package com.yzh.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yuanzhihao
 * @since 2022/5/8
 */
@Data
public class UserInfo implements Serializable {
    private String username;
    private String password;
}