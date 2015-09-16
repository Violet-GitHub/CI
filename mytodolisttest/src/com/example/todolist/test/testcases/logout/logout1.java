package com.example.todolist.test.testcases.logout;

import com.example.todolist.test.BasicTestWithLogin;
import com.robotium.solo.Solo;

public class logout1 extends BasicTestWithLogin {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public void testLogOut(){
		uiHelper.getSolo().sendKey(Solo.MENU);
		uiHelper.getSolo().sleep(3000);
		uiHelper.getSolo().clickOnText("ÍË³ö");
		uiHelper.getSolo().clickOnButton("È·ÈÏ");
		uiHelper.getSolo().sleep(3000);
		assertEquals("", uiHelper.getElementsLoginActivity().getNameEditText().getText().toString());
		assertEquals("",uiHelper.getElementsLoginActivity().getPasswordEditText().getText().toString());
		
	}
	
}
