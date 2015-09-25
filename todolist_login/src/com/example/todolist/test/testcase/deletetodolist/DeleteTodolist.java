package com.example.todolist.test.testcase.deletetodolist;

import com.example.todolist.test.BasicTestCase;

public class DeleteTodolist extends BasicTestCase{

	public void testDeleteTodolist(){
		uiHelper.getElementsLoginActivity().login(new String[]{"1","1"});
		if(solo.searchText("Sunday is so happy!" )==false){
			uiHelper.getElementsMainActivity().clickActionNew();
			solo.waitForText("输入待办事项。。。");
			uiHelper.geElementsNewToDoItemActivity().enterToDoItemDetailET("Sunday is so happy!");
			uiHelper.geElementsNewToDoItemActivity().clickSaveButton();
		}
		while(solo.searchText("Sunday is so happy!" )){
			solo.clickLongOnText("Sunday is so happy!");
			solo.clickOnText("删除");
			solo.clickOnButton("确认");
		}
		assertFalse(solo.searchText("Sunday is so happy!"));
	}
}
