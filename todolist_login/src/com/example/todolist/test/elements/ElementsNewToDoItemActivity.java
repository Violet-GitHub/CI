package com.example.todolist.test.elements;

import com.example.todolist.R;
import com.robotium.solo.Solo;

import android.widget.Button;
import android.widget.EditText;


public class ElementsNewToDoItemActivity {
	private Solo solo = null;
	private EditText toDoItemDetailET;
	private Button clickSaveButton;
	
	public ElementsNewToDoItemActivity(Solo solo) {
		this.solo=solo;
		init();
	}
	
	public void init(){
		toDoItemDetailET=(EditText)solo.getCurrentActivity().findViewById(R.id.NewToDoItemDetailET);
		clickSaveButton=(Button)solo.getCurrentActivity().findViewById(R.id.saveBtn);
	}
	
	public void enterToDoItemDetailET(String detailStr){
		solo.enterText(toDoItemDetailET, detailStr);
	}
	
	public void clickSaveButton(){
		solo.clickOnView(clickSaveButton);
	}
	
}
