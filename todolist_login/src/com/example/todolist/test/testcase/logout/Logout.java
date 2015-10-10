package com.example.todolist.test.testcase.logout;

import com.example.todolist.test.BasicTestCase;

public class Logout extends BasicTestCase{
	
	public void testLogout(){
		uiHelper.getElementsLoginActivity().login(new String[]{"1","1"});
		solo.sleep(1000);
		solo.sendKey(solo.MENU);
		solo.sleep(1000);
		solo.clickOnText("退出");
		solo.clickOnButton("确认");
		solo.sleep(1000);
		assertEquals("", uiHelper.getElementsLoginActivity().getUsernameEditText().getText().toString());
		assertEquals("", uiHelper.getElementsLoginActivity().getPasswordEditText().getText().toString());
	}
	
}
