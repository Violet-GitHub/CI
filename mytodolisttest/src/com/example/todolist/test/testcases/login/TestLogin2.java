package com.example.todolist.test.testcases.login;

import com.example.todolist.test.BasicTestCase;


public class TestLogin2 extends BasicTestCase{
	
	public void testLogin(){
		//uiHelper.getElementsLoginActivity().enterName("");
		//uiHelper.getElementsLoginActivity().enterPassword("");
		uiHelper.getElementsLoginActivity().clickLoginButton();
		assertTrue("������Ϣû�г��֣����ܳ���bug", uiHelper.getSolo().searchText("�û����������벻��Ϊ�գ�", true));
	}
	
}
