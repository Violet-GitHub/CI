package com.example.todolist.test.testcases.todolist;

import com.example.todolist.MainActivity;
import com.example.todolist.test.BasicTestWithLogin;

public class ToDoList_1 extends BasicTestWithLogin {
	
	public void testToDoList_1() {
		//��������б�ҳ��Ŀؼ��࣬���øÿؼ������Ѿ���װ�õĵ����Ӱ�ť
		uiHelper.getElementsMainActivity().clickOnActionNewView();
		//��������б�ҳ��Ŀؼ��࣬���øÿؼ������Ѿ���װ����������������Լ�������水ť����
		uiHelper.getElementsNewToDoActivity().enterTextToToDoItemDetailET("addtest");
		uiHelper.getElementsNewToDoActivity().clickSaveButton();
		//waitForActivity,�ȴ������Activity���֣�����ȵ��˷���true�����򷵻�false
		assertTrue(uiHelper.getSolo().waitForActivity(MainActivity.class,3000));
		assertTrue(uiHelper.getSolo().searchText("addtest", true));
		
		/**
		 * ɾ����������
		 */
		//clickLongOnText �����ı�
		uiHelper.getSolo().clickLongOnText("addtest");
		//clickOnText ����ı�
		uiHelper.getSolo().clickOnText("ɾ��");
		//clickOnButton ���ָ���ı���ť
		uiHelper.getSolo().clickOnButton("ȷ��");
	}
}
