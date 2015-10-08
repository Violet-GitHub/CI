package com.example.todolist.test.elements;

import com.example.todolist.R;
import com.robotium.solo.Solo;

import android.view.View;
import android.widget.ListView;

public class ElementsMainActivity {
	private Solo solo = null;
	private ListView todoListView= null;
	private View actionNew=null;
	
	public ElementsMainActivity(Solo solo) {
		this.solo=solo;
		init();
	}
	
	public void init(){
		todoListView =(ListView)solo.getCurrentActivity().findViewById(R.id.todoListView);
		actionNew =(View)solo.getCurrentActivity().findViewById(R.id.action_new);
	}
	
	public ListView getTodoListView() {
		return todoListView;
	}
	
	public View getActionNew(){
		return actionNew;
	}
	
	public void clickActionNew(){
		solo.clickOnView(actionNew);
	}
	
}
