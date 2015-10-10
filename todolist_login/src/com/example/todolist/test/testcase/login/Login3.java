package com.example.todolist.test.testcase.login;

import com.example.todolist.test.BasicTestCase;

public class Login3 extends BasicTestCase{
	
	public void testLogin1(){
		uiHelper.getElementsLoginActivity().login(new String[]{"1","1"});
		solo.sleep(1000);
		assertTrue(uiHelper.getElementsMainActivity().getActionNew().isShown());
	}
}