package com.example.todolist.test;

import com.example.todolist.LoginActivity;
import com.robotium.solo.Solo;

import android.test.ActivityInstrumentationTestCase2;

public class BasicTestCase extends ActivityInstrumentationTestCase2<LoginActivity> {
	public Solo solo=null;
	public UIHelper uiHelper=null;
	
	public BasicTestCase() {
		super(LoginActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		init();
	}
	
	public void init(){
		solo=new Solo(getInstrumentation(),getActivity());
		uiHelper=new UIHelper(solo);
	}

	@Override
	protected void tearDown() throws Exception {
		solo.finishOpenedActivities();
		super.tearDown();
	}
	
}
