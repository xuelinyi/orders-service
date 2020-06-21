package com.orders.exception;


/**
 * @prouect_name: framework
 * @class_name: RootRuntimeException
 * @description: 根RuntimeException类
 * @create_date: 2017年08月01日
 * @modify_date: 2017年08月01日
 **/
public class RootRuntimeException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public RootRuntimeException(String message) {
		this(null, message);
	}

	public RootRuntimeException(Integer exceptionCode, String message) {
		super(message);
		this.exceptionCode = exceptionCode;
	}

	public RootRuntimeException(Integer exceptionCode, String message, Throwable cause) {
		super(message, cause);
		this.exceptionCode = exceptionCode;
	}

	private Integer exceptionCode;

	/**
	 * 异常代码
	 *
	 * @return 异常代码
	 */
	public Integer getExceptionCode() {
		return exceptionCode;
	}

	/**
	 * 异常代码
	 *
	 * @param exceptionCode
	 *            异常代码
	 */
	public void setExceptionCode(Integer exceptionCode) {
		this.exceptionCode = exceptionCode;
	}

	/**
	 * 重写fillInStackTrace，不在返回堆栈异常
	 *
	 * @return Throwable
	 */
	@Override
	public synchronized Throwable fillInStackTrace() {
		return this;
	}

	/**
	 * 返回ip和错误信息
	 *
	 * @return ip和错误信息
	 *//*
	public String getMessageMore() {
		String ipName = "local";
		String ip = "127.0.0.1";
		try {
			if (isConsumerSide()) {
				ipName = "Provider";
				ip = getProviderHost();
			} else if (isProviderSide()) {
				ipName = "Consumer";
				ip = getConsumerHost();
			} else {
				ip = System..getLocalAddress().getHostAddress();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return String.format("{%s:\"%s\",信息:\"%s\",异常代码:\"%s\"}", ipName, ip, super.getMessage(), exceptionCode);
	}

	*//**
	 * 重写tostring，返回ip和错误信息
	 *
	 * @return ip和错误信息
	 *//*
	@Override
	public String toString() {
		return getMessageMore();
	}

	*//**
	 * 是否为消费端
	 *//*
	private boolean isConsumerSide() {
		RpcContext rpcContext = RpcContext.getContext();
		if (rpcContext != null) {
			return rpcContext.isConsumerSide();
		}
		return false;
	}

	*//**
	 * 判断是否为提供端
	 *//*
	private boolean isProviderSide() {
		RpcContext rpcContext = RpcContext.getContext();
		if (rpcContext != null) {
			boolean isProviderSide = rpcContext.isProviderSide();
			return isProviderSide;
		}
		return false;

	}

	*//**
	 * 获取最后一次调用的提供端IP地址
	 *
	 * @return
	 *//*
	private String getProviderHost() {
		if (isConsumerSide()) {
			return RpcContext.getContext().getRemoteHost();
		} else {
			return NetUtils.getLocalAddress().getHostAddress();
		}
	}

	*//**
	 * 获取调用方IP
	 *
	 * @return 消费端IP地址
	 *//*
	private String getConsumerHost() {
		if (isProviderSide()) {
			return RpcContext.getContext().getRemoteHost();
		}
		return NetUtils.getLocalAddress().getHostAddress();
	}*/

}
