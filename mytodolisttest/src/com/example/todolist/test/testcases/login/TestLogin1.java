package com.example.todolist.test.testcases.login;

import com.example.todolist.test.BasicTestCase;


public class TestLogin1 extends BasicTestCase{
	
	public void testLogin(){
		uiHelper.getElementsLoginActivity().enterName("abcdefg");
		uiHelper.getElementsLoginActivity().enterPassword("123456");
		uiHelper.getElementsLoginActivity().clickLoginButton();
		assertTrue("������Ϣû�г��֣����ܳ���bug", uiHelper.getSolo().searchText("�û��������������", true));
	}
	
}
