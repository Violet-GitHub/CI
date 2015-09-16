package com.example.todolist.test.elements;

import com.example.todolist.R;
import com.example.todolist.test.BasicTestCase;
import com.robotium.solo.Solo;

import android.widget.*;

public class ElementsNewToDoActivity extends BasicTestCase {
	public Solo solo;
	private EditText toDoItemDetailET;
	private Button saveBtn;

	public ElementsNewToDoActivity(Solo solo) {
		this.solo=solo;
		initViews();
	}
	
	/**
	 * ��ʼ����ʱ���ҵ����е�¼ҳ��Ŀؼ�
	 */
	private void initViews(){
		solo.sleep(1500);
		toDoItemDetailET = (EditText) solo.getCurrentActivity().findViewById(R.id.toDoItemDetailET);
		saveBtn = (Button) solo.getCurrentActivity().findViewById(R.id.saveBtn);
	}

	/**
	 * ��������
	 * @return
	 */
	public EditText getToDoItemDetailET() {
		return toDoItemDetailET;
	}
	
	/**
	 * �������������������ֵ
	 * @param text
	 */
	public void enterTextToToDoItemDetailET(String text) {
		solo.enterText(toDoItemDetailET,text);
	}

	/**
	 * ������水ť
	 */
	public void clickSaveButton() {
		solo.clickOnView(saveBtn);
	}
	
}
