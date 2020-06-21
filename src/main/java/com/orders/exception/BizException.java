package com.orders.exception;

/**
 * 自定义业务异常
 * 
 * @author zoumingyu
 * @date 2019年8月18日
 *
 */
public class BizException extends RootRuntimeException {
	private static final long serialVersionUID = 1L;

	public BizException(String message) {
		super(message);
	}

	public BizException(Integer exceptionCode, String message) {
		super(exceptionCode, message);
	}

	public BizException(Integer exceptionCode, String message, Throwable cause) {
		super(exceptionCode, message, cause);
	}
}
