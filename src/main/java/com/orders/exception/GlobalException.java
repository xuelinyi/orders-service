package com.orders.exception;

import com.orders.entity.response.ResponseCodeEnum;

public class GlobalException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private ResponseCodeEnum codeEnum;

	public GlobalException(ResponseCodeEnum codeEnum) {
		super(codeEnum.getMessage());
		this.codeEnum = codeEnum;
	}

	public ResponseCodeEnum getCodeEnum() {
		return codeEnum;
	}
}
