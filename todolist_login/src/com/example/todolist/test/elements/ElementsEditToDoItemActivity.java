package com.example.todolist.test.elements;

import com.example.todolist.R;
import com.robotium.solo.Solo;

import android.widget.Button;
import android.widget.EditText;

public class ElementsEditToDoItemActivity {
	private Solo solo = null;
	private EditText editToDoItemDetailET;
	private Button saveToDoItemBtn;
	
	public ElementsEditToDoItemActivity(Solo solo) {
		this.solo=solo;
		init();
	}
	
	public void init(){
		editToDoItemDetailET=(EditText)solo.getCurrentActivity().findViewById(R.id.editToDoItemDetailET);
		saveToDoItemBtn=(Button)solo.getCurrentActivity().findViewById(R.id.saveToDoItemBtn);
	}
	
	public void enterEditToDoItemDetailET(String detailStr){
		solo.enterText(editToDoItemDetailET, detailStr);
	}
	
	public void clearToDoItemDetailET(){
		solo.clearEditText(editToDoItemDetailET);
	}
	
	
	public void clickSaveToDoItemBtn(){
		solo.clickOnView(saveToDoItemBtn);
	}
	
	
}
