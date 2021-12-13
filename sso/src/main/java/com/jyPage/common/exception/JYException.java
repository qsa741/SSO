package com.jyPage.common.exception;

public class JYException extends Exception {

	private final long ERR_CODE;

	
	JYException(String msg, int errCode) {
		super(msg);
		ERR_CODE = errCode;
	}

	JYException(String msg) {
		this(msg, 100);
	}

	public long getErrCode() {
		return ERR_CODE;
	}
}
