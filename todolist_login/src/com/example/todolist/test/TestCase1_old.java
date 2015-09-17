package com.example.todolist.test;

import com.example.todolist.LoginActivity;
import com.robotium.solo.Solo;

import junit.framework.TestCase;
import android.test.ActivityInstrumentationTestCase2;

public class TestCase1_old extends ActivityInstrumentationTestCase2<LoginActivity> {
	
	private Solo solo;
	
	public TestCase1_old() {
		super(LoginActivity.class);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		// 构造函数,使用默认配置
		solo=new Solo(getInstrumentation(),getActivity());
	}
	
	public void testTestCase1(){
		solo.enterText(0, "abcdefj");
		solo.enterText(1, "123456");
		solo.clickOnButton(0);
		assertTrue("错误信息没有出现，可能出现bug", solo.searchText("用户名或者密码错误！", true));
	}
	
	//关闭所有打开的activity
	@Override
	protected void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}
}
