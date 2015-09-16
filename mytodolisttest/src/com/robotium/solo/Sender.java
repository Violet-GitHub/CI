package com.robotium.solo;


import junit.framework.Assert;
import android.app.Instrumentation;
import android.view.KeyEvent;

/**
 * ���ڷ��͸��ఴ���¼�
 * Contains send key event methods. Examples are:
 * sendKeyCode(), goBack()
 * 
 * @author Renas Reda, renas.reda@robotium.com
 * 
 */

class Sender {
	// Instrument,���ڷ��͸����¼�
	private final Instrumentation inst;
	// �ȴ�������
	private final Sleeper sleeper;

	/**
	 * ���캯��
	 * Constructs this object.
	 * 
	 * @param inst the {@code Instrumentation} instance
	 * @param sleeper the {@code Sleeper} instance
	 */

	Sender(Instrumentation inst, Sleeper sleeper) {
		this.inst = inst;
		this.sleeper = sleeper;
	}

	/**
	 * ���͸��ఴ���¼�.
	 * Tells Robotium to send a key code: Right, Left, Up, Down, Enter or other.
	 * 
	 * @param keycode the key code to be sent. Use {@link KeyEvent#KEYCODE_ENTER}, {@link KeyEvent#KEYCODE_MENU}, {@link KeyEvent#KEYCODE_DEL}, {@link KeyEvent#KEYCODE_DPAD_RIGHT} and so on
	 */

	public void sendKeyCode(int keycode)
	{
		sleeper.sleep();
		try{
			inst.sendCharacterSync(keycode);
			// �������������Ȩ������
		}catch(SecurityException e){
			// ��־���ѣ��ò�����Ȩ����ش�����־
			Assert.fail("Can not complete action! ("+(e != null ? e.getClass().getName()+": "+e.getMessage() : "null")+")");
		}
	}

	/**
	 * ���ͷ����¼��������һ�·��ذ�ť
	 * Simulates pressing the hardware back key.
	 */

	public void goBack() {
		// �ȴ�500ms
		sleeper.sleep();
		try {
			// ���ͷ����¼�
			inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
			// �ȴ�500ms
			sleeper.sleep();
		} catch (Throwable ignored) {}
	}
}