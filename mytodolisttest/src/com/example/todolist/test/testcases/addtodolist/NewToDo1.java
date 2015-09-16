package com.example.todolist.test.testcases.addtodolist;

import com.example.todolist.test.BasicTestWithLogin;

public class NewToDo1 extends BasicTestWithLogin{
	
	public void testNewToDo1(){
		//获得任务列表页面的控件类，调用该控件类里已经封装好的点击添加按钮
		uiHelper.getElementsMainActivity().clickOnActionNewView();
		//验证字符串出现
		//robotium waitForText方法的第一个参数是指等待出现的字符串
		//第二个参数是验证第几个字符串，因为页面上可能会出现多个同样的字符串，所以要指定第几个
		//第三个参数是指最长等待的时间，超过这个时间，如果字符串还是不出现就不继续等待了
		//带四个参数是指是否要滚动
		//最后一个参数，如果是true则代表只搜索可见的字符串，false则及时是隐藏的字符串也算
		assertTrue(uiHelper.getSolo().waitForText("输入待办事项。。。", 1, 5000, false, true));
				
		//获得新建任务页面的控件类，调用该控件类里已经封装好的点击保存按钮
		uiHelper.getElementsNewToDoActivity().clickSaveButton();
		//searchText用来搜索指定的字符串是否存在，第二个参数跟上面提到的waitForText的最后一个参数用法一致
		assertTrue(uiHelper.getSolo().searchText("待办事项不能为空！", true));
	}
}
