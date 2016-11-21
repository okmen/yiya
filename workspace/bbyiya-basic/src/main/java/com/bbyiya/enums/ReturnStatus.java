package com.bbyiya.enums;

public enum ReturnStatus {
	/**
	 * �ɹ�
	 */
	Success(1),
	/**
	 * ��������¼��֤�ɹ�
	 */
	OthLogSuccess(2),
	/**
	 * ��½���ڻ�δ��¼
	 */
	LoginError(-1),
	/**
	 * ϵͳ����try catch����
	 */
	SystemError(-3),
	/**
	 * ��������
	 */
	ParamError(-2);

	private final int step;

	private ReturnStatus(int step) {

		this.step = step;
	}

	public int getValue() {
		return step;
	}

	@Override
	public String toString() {
		return String.valueOf(this.step);
	}
}
