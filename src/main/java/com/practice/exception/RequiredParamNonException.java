package com.practice.exception;

import lombok.NoArgsConstructor;

/**
 * 필수 파라메터 없음
 */
@NoArgsConstructor
public class RequiredParamNonException extends Exception {
    public RequiredParamNonException(String message) {
        super(message);
    }
}
