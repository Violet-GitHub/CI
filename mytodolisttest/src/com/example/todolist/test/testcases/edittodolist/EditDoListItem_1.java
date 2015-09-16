package com.example.todolist.test.testcases.edittodolist;

import com.example.todolist.EditToDoItemActivity;
import com.example.todolist.MainActivity;
import com.example.todolist.test.BasicTestWithLogin;

public class EditDoListItem_1 extends BasicTestWithLogin {
	
	public void testEditDoListItem1(){
		uiHelper.getElementsMainActivity().clickOnActionNewView();
		uiHelper.getElementsNewToDoActivity().enterTextToToDoItemDetailET("addTested");
		uiHelper.getElementsNewToDoActivity().clickSaveButton();
		assertFalse(uiHelper.getSolo().waitForText("addTestde", 1, 2000,false,true));
		//
		uiHelper.getSolo().clickLongOnText("addTested");
		//
		uiHelper.getSolo().clickOnText("±à¼­");
		assertTrue(uiHelper.getSolo().waitForActivity(EditToDoItemActivity.class,3000));
		assertEquals("addTested",uiHelper.getElementsEditToDoItemActivity().getToDoItemDetailET().getText().toString());
		uiHelper.getElementsEditToDoItemActivity().clearToDoItemDetailET();
		uiHelper.getElementsEditToDoItemActivity().enterToDoItemDetailET("addTestde");
		uiHelper.getElementsEditToDoItemActivity().clickSaveToDoItemBtn();
		assertTrue(uiHelper.getSolo().waitForActivity(MainActivity.class,3000));
		assertTrue(uiHelper.getSolo().searchText("addTestde",true));
		
	}
	
}
