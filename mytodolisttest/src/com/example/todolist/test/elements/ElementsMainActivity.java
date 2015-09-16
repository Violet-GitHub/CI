package com.example.todolist.test.elements;

import com.example.todolist.R;
import com.example.todolist.test.BasicTestCase;
import com.robotium.solo.Solo;

import android.view.View;
import android.widget.*;

public class ElementsMainActivity extends BasicTestCase {
	public Solo solo;
	private ListView toDoListView;
	private LinearLayout emptyToDoView;
	private View actionNewView;
	
	public ElementsMainActivity(Solo solo) {
		this.solo=solo;
		initViews();
	}
	
	/**
	 * 初始化的时候，找到所有登录页面的控件
	 */
	private void initViews(){
		toDoListView = (ListView) solo.getCurrentActivity().findViewById(R.id.todoListView);
		emptyToDoView = (LinearLayout) solo.getCurrentActivity().findViewById(R.id.emptyToDoView);
		actionNewView = (View)solo.getCurrentActivity().findViewById(R.id.action_new);
	}

	public ListView getToDoListView() {
		return toDoListView;
	}

	public View getActionNewView(){
		return actionNewView;
	}
	
	public void clickOnActionNewView() {
		solo.clickOnView(actionNewView);
	}
}
