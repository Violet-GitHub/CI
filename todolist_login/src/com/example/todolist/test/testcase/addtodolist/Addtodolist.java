package com.example.todolist.test.testcase.addtodolist;

import com.example.todolist.test.BasicTestCase;

public class Addtodolist extends BasicTestCase{
	public void testAddtodolist(){
		uiHelper.getElementsLoginActivity().login(new String[]{"1","1"});
		solo.waitForActivity("ElementsMainActivity",1000);
		uiHelper.getElementsMainActivity().clickActionNew();
		solo.waitForText("输入待办事项。。。");
		uiHelper.geElementsNewToDoItemActivity().enterToDoItemDetailET("Sunday is so happy!");
		uiHelper.geElementsNewToDoItemActivity().clickSaveButton();
		assertTrue(solo.searchText("Sunday is so happy!"));
	}
}
