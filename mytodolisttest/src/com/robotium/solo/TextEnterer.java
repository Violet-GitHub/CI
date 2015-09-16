package com.robotium.solo;

import junit.framework.Assert;
import android.app.Instrumentation;
import android.text.InputType;
import android.widget.EditText;


/**
 * �ı��������������
 * Contains setEditText() to enter text into text fields.
 * 
 * @author Renas Reda, renas.reda@robotium.com
 *
 */

class TextEnterer{
	// Instrument �����¼�����
	private final Instrumentation inst;
	// �������������
	private final Clicker clicker;
	// �������������
	private final DialogUtils dialogUtils;

	/**
	 * ���캯��
	 * Constructs this object.
	 * 
	 * @param inst the {@code Instrumentation} instance
	 * @param clicker the {@code Clicker} instance
	 * @param dialogUtils the {@code DialogUtils} instance
	 * 
	 */

	public TextEnterer(Instrumentation inst, Clicker clicker, DialogUtils dialogUtils) {
		this.inst = inst;
		this.clicker = clicker;
		this.dialogUtils = dialogUtils;
	}

	/**
	 * ����EditText����,�������ı���Ϊ�գ�����ԭ��������׷�ӣ��������ԭ������
	 * editText  ��Ҫ�������ݵ�editText
	 * text      ���õ��ı�����
	 * Sets an {@code EditText} text
	 * 
	 * @param index the index of the {@code EditText} 
	 * @param text the text that should be set
	 */

	public void setEditText(final EditText editText, final String text) {
		// �ǿ��ж�
		if(editText != null){
			// ��ȡԭ�е��ı�����
			final String previousText = editText.getText().toString();
			// �����߳���ִ�У�������̱߳���
			inst.runOnMainSync(new Runnable()
			{
				public void run()
				{
					// ���ԭ������
					editText.setInputType(InputType.TYPE_NULL); 
					// �ѽ����л���editText
					editText.performClick();
					// ���������
					dialogUtils.hideSoftKeyboard(editText, false, false);
					// ����ı�����Ϊ�գ�������Ϊ��
					if(text.equals(""))
						editText.setText(text);
					// ����ǿգ�����ԭ��������׷��
					else{
						editText.setText(previousText + text);
						// �Ƴ�����
						editText.setCursorVisible(false);
					}
				}
			});
		}
	}
	
	/**
	 * ¼���ı����ݵ�EditText
	 * editText  ��Ҫ�������ݵ�editText
	 * text      ¼����ı�����
	 * Types text in an {@code EditText} 
	 * 
	 * @param index the index of the {@code EditText} 
	 * @param text the text that should be typed
	 */

	public void typeText(final EditText editText, final String text){
		// �ǿ��ж�
		if(editText != null){
			// ���ԭ������
			inst.runOnMainSync(new Runnable()
			{
				public void run()
				{
					editText.setInputType(InputType.TYPE_NULL);
				}
			});
			// editText��Ϊ��ǰ����
			clicker.clickOnScreen(editText, false, 0);
			// ���������
			dialogUtils.hideSoftKeyboard(editText, true, true);

			boolean successfull = false;
			int retry = 0;
			// ¼���ı����ݣ���¼��ʧ�ܻ����ԣ����10��
			while(!successfull && retry < 10) {

				try{
					inst.sendStringSync(text);
					successfull = true;
					// ����������̵����쳣
				}catch(SecurityException e){
					// ���������
					dialogUtils.hideSoftKeyboard(editText, true, true);
					// �������Դ���
					retry++;
				}
			}
			// ¼��ʧ�ܣ��״�
			if(!successfull) {
				Assert.fail("Text can not be typed!");
			}
		}
	}
}