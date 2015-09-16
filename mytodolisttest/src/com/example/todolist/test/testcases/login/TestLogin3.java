package com.example.todolist.test.testcases.login;

import com.example.todolist.test.BasicTestWithLogin;


public class TestLogin3 extends BasicTestWithLogin{
	
	public void testLogin(){
		assertTrue(uiHelper.getElementsMainActivity().getActionNewView().isShown());
	}
	
}
