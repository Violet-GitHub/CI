package com.example.todolist.test.testcase.edittodolist;

import com.example.todolist.test.BasicTestCase;

public class Edittodolist extends BasicTestCase{
	
	public void testEdittodolist(){
		uiHelper.getElementsLoginActivity().login(new String[]{"1","1"});
		solo.sleep(1000);
		if(solo.searchText("Sunday is so happy!" )==false){
			uiHelper.getElementsMainActivity().clickActionNew();
			solo.waitForText("输入待办事项。。。");
			uiHelper.geElementsNewToDoItemActivity().enterToDoItemDetailET("Sunday is so happy!");
			uiHelper.geElementsNewToDoItemActivity().clickSaveButton();
		}
		solo.clickLongOnText("Sunday is so happy!");
		solo.clickOnText("编辑");
		solo.sleep(1000);
		uiHelper.geElementsEditToDoItemActivity().clearToDoItemDetailET();
		uiHelper.geElementsEditToDoItemActivity().enterEditToDoItemDetailET("Monday is not happy!");
		uiHelper.geElementsEditToDoItemActivity().clickSaveToDoItemBtn();
		assertTrue(solo.searchText("Monday is not happy!"));
	}
	
}
