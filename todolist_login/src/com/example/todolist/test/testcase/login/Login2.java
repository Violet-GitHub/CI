package com.example.todolist.test.testcase.login;

import com.example.todolist.test.BasicTestCase;
import com.example.todolist.test.elements.ElementsLoginActivity;

public class Login2 extends BasicTestCase{
	
	public void testLogin1(){
		uiHelper.getElementsLoginActivity().clickLoginButton();
		assertTrue("错误信息没有出现，可能出现bug",solo.searchText("用户名或者密码不能为空！", true));
	}
}
