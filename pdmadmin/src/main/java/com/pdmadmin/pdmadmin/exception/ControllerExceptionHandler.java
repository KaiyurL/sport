package com.pdmadmin.pdmadmin.exception;

import cn.dev33.satoken.exception.NotLoginException;
import com.pdmadmin.pdmadmin.response.R;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public R<Void> handleBusinessException(BusinessException e){
        return R.error(e.getCode(),e.getMessage());
    }
    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception e){
        return R.error(e.getMessage());
    }

    @ExceptionHandler(NotLoginException.class)
    public R<Void> handleNotLoginException(NotLoginException e){
        return R.error(e.getCode(),e.getMessage());
    }


}
