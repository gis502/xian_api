package com.ruoyi.common.exception.base;

/**
 * @author: xiaodemos
 * @date: 2025-08-06 11:08
 * @description: 参数空异常
 */


public class ParamsException extends RuntimeException{

    public ParamsException(String message) {
        super(message);
    }

}
