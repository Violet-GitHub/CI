package com.example.todolist.test.testcases.login;

import com.example.todolist.test.BasicTestCase;


public class TestLogin2 extends BasicTestCase{
	
	public void testLogin(){
		//uiHelper.getElementsLoginActivity().enterName("");
		//uiHelper.getElementsLoginActivity().enterPassword("");
		uiHelper.getElementsLoginActivity().clickLoginButton();
		assertTrue("错误信息没有出现，可能出现bug", uiHelper.getSolo().searchText("用户名或者密码不能为空！", true));
	}
	
}
