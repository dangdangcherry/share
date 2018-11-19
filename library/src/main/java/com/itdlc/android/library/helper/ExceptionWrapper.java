package com.itdlc.android.library.helper;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import retrofit2.HttpException;

public class ExceptionWrapper extends Exception {
    private Throwable exception;

    public ExceptionWrapper(Throwable exception) {
        this.exception = exception;
    }

    @Override
    public String getMessage() {
        if (exception instanceof SocketTimeoutException
                || exception instanceof ConnectException
                || exception instanceof SocketException
                || exception instanceof HttpException) {
            return "网络异常";
        }
        return super.getMessage();
    }
}
