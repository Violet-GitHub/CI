package com.example.todolist.test.testcases.todolist;

import com.example.todolist.MainActivity;
import com.example.todolist.test.BasicTestWithLogin;

public class ToDoList_1 extends BasicTestWithLogin {
	
	public void testToDoList_1() {
		//获得任务列表页面的控件类，调用该控件类里已经封装好的点击添加按钮
		uiHelper.getElementsMainActivity().clickOnActionNewView();
		//获得任务列表页面的控件类，调用该控件类里已经封装好输入任务项方法，以及点击保存按钮方法
		uiHelper.getElementsNewToDoActivity().enterTextToToDoItemDetailET("addtest");
		uiHelper.getElementsNewToDoActivity().clickSaveButton();
		//waitForActivity,等待具体的Activity出现，如果等到了返回true，否则返回false
		assertTrue(uiHelper.getSolo().waitForActivity(MainActivity.class,3000));
		assertTrue(uiHelper.getSolo().searchText("addtest", true));
		
		/**
		 * 删除测试数据
		 */
		//clickLongOnText 长按文本
		uiHelper.getSolo().clickLongOnText("addtest");
		//clickOnText 点击文本
		uiHelper.getSolo().clickOnText("删除");
		//clickOnButton 点击指定文本按钮
		uiHelper.getSolo().clickOnButton("确认");
	}
}
