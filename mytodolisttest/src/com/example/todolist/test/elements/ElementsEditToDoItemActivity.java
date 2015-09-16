package com.example.todolist.test.elements;

import com.example.todolist.R;
import com.example.todolist.test.BasicTestCase;
import com.robotium.solo.Solo;

import android.widget.*;

public class ElementsEditToDoItemActivity extends BasicTestCase {
	private Solo solo;
	private EditText toDoItemDetailET;
	private Button saveToDoItemBtn;
	
	public ElementsEditToDoItemActivity(Solo solo){
		this.solo=solo;
		initView();
	}
	
	/**
	 * 初始化的时候，找到页面的所有控件 
	 */
	public void initView(){
		solo.sleep(1500);
		toDoItemDetailET=(EditText)solo.getCurrentActivity().findViewById(R.id.toDoItemDetailET);
		saveToDoItemBtn=(Button)solo.getCurrentActivity().findViewById(R.id.saveToDoItemBtn);
	}
	
	
	public EditText getToDoItemDetailET(){
		return toDoItemDetailET;
	}
	
	public void enterToDoItemDetailET(String string){
		solo.enterText(toDoItemDetailET, string);
	}
	
	public void clearToDoItemDetailET(){
		solo.clearEditText(toDoItemDetailET);
	}
	
	public void clickSaveToDoItemBtn(){
		solo.clickOnView(saveToDoItemBtn);
	}
	
}
