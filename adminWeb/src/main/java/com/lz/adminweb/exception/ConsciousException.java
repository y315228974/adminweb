package com.lz.adminweb.exception;


import com.lz.adminweb.enums.ResponseCodeEnum;

public class ConsciousException extends RuntimeException {

    private int code;

    public ConsciousException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public ConsciousException(String msg) {
        super(msg);
        this.code = ResponseCodeEnum.INTERNAL_SERVER_ERROR.getCode();
    }

    public ConsciousException(ResponseCodeEnum responseCodeEnum, String msg) {
        super(msg);
        this.code = responseCodeEnum.getCode();
    }

    public ConsciousException(ResponseCodeEnum responseCodeEnum) {
        super(responseCodeEnum.getMessage());
        this.code = responseCodeEnum.getCode();
    }

    public int getCode() {
        return code;
    }
}
