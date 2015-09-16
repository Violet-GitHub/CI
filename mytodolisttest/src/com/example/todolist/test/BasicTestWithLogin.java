package com.example.todolist.test;

public class BasicTestWithLogin extends BasicTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		uiHelper.getElementsLoginActivity().doLoginWithAccount(new String[]{"1","1"});
	}

}
