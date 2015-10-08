package com.example.todolist.test.testcase.login;

import com.example.todolist.test.BasicTestCase;
import com.example.todolist.test.elements.ElementsLoginActivity;

public class Login1 extends BasicTestCase{
	
	public void testLogin1(){
		uiHelper.getElementsLoginActivity().login(new String[]{"abcdefg","111111"});
		assertTrue("错误信息没有出现，可能出现bug",solo.searchText("用户名或者密码错误！", true));
	}
}
