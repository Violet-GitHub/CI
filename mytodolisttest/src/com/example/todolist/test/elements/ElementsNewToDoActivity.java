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
	 * 初始化的时候，找到所有登录页面的控件
	 */
	private void initViews(){
		solo.sleep(1500);
		toDoItemDetailET = (EditText) solo.getCurrentActivity().findViewById(R.id.toDoItemDetailET);
		saveBtn = (Button) solo.getCurrentActivity().findViewById(R.id.saveBtn);
	}

	/**
	 * 获得输入框
	 * @return
	 */
	public EditText getToDoItemDetailET() {
		return toDoItemDetailET;
	}
	
	/**
	 * 往任务项输入框中输入值
	 * @param text
	 */
	public void enterTextToToDoItemDetailET(String text) {
		solo.enterText(toDoItemDetailET,text);
	}

	/**
	 * 点击保存按钮
	 */
	public void clickSaveButton() {
		solo.clickOnView(saveBtn);
	}
	
}
