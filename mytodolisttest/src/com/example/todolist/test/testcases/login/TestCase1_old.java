package com.example.todolist.test.testcases.login;

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
		// ���캯��,ʹ��Ĭ������
		solo=new Solo(getInstrumentation(),getActivity());
	}
	
	public void testTestCase1(){
		solo.enterText(0, "abcdefj");
		solo.enterText(1, "123456");
		solo.clickOnButton(0);
		assertTrue("������Ϣû�г��֣����ܳ���bug", solo.searchText("�û��������������", true));
	}
	
	//�ر����д򿪵�activity
	@Override
	protected void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}
}
