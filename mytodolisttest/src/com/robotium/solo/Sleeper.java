package com.robotium.solo;
/**
 * ��ʱ�ȴ�������
 *
 */
class Sleeper {
	// ����500ms
	private final int PAUSE = 500;
	// ����300ms
	private final int MINIPAUSE = 300;

	/**
	 * ��ʱ500ms
	 * Sleeps the current thread for a default pause length.
	 */

	public void sleep() {
        sleep(PAUSE);
	}


	/**
	 * ��ʱ300ms
	 * Sleeps the current thread for a default mini pause length.
	 */

	public void sleepMini() {
        sleep(MINIPAUSE);
	}


	/**
	 * ��ʱָ����ֵ��ms
	 * Sleeps the current thread for <code>time</code> milliseconds.
	 *
	 * @param time the length of the sleep in milliseconds
	 */

	public void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException ignored) {}
	}

}