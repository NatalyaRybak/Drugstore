package com.kmadrugstore.controller;

import com.kmadrugstore.exception.*;
import com.kmadrugstore.utils.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(GoodNotFoundException.class)
    public ResponseEntity<Object> goodNotFoundException() {
        return new ResponseEntity<>(
                Constants.GOOD_NOT_FOUND_MESSAGE, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GoodNotInStockException.class)
    public ResponseEntity<Object> goodNotInStockException() {
        return new ResponseEntity<>(
                Constants.GOOD_NOT_IN_STOCK_MESSAGE, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(StatusNotFoundException.class)
    public ResponseEntity<Object> statusNotFoundException() {
        return new ResponseEntity<>(
                Constants.STATUS_NOT_FOUND_MESSAGE, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Object> orderNotFoundException() {
        return new ResponseEntity<>(
                Constants.ORDER_NOT_FOUND_MESSAGE, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> UserNotFoundException() {
        return new ResponseEntity<>(
                Constants.USER_NOT_FOUND_MESSAGE, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailIsUsedException.class)
    public ResponseEntity<Object> EmailIsUsedException() {
        return new ResponseEntity<>(
                Constants.EMAIL_IS_USED_MESSAGE, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NotEnoughBonusesException.class)
    public ResponseEntity<Object> NotEnoughBonusesException() {
        return new ResponseEntity<>(
                Constants.NOT_ENOUGH_BONUSES_MESSAGE, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(PasswordResetTokenInvalidException.class)
    public ResponseEntity<Object> PasswordTokenInvalidException() {
        return new ResponseEntity<>(
                Constants.PASSWORD_RESET_TOKEN_INVALID_EXCEPTION ,
                HttpStatus.FORBIDDEN);
    }
}
