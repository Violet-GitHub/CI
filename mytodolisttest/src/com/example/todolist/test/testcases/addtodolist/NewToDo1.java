package com.example.todolist.test.testcases.addtodolist;

import com.example.todolist.test.BasicTestWithLogin;

public class NewToDo1 extends BasicTestWithLogin{
	
	public void testNewToDo1(){
		//��������б�ҳ��Ŀؼ��࣬���øÿؼ������Ѿ���װ�õĵ����Ӱ�ť
		uiHelper.getElementsMainActivity().clickOnActionNewView();
		//��֤�ַ�������
		//robotium waitForText�����ĵ�һ��������ָ�ȴ����ֵ��ַ���
		//�ڶ�����������֤�ڼ����ַ�������Ϊҳ���Ͽ��ܻ���ֶ��ͬ�����ַ���������Ҫָ���ڼ���
		//������������ָ��ȴ���ʱ�䣬�������ʱ�䣬����ַ������ǲ����־Ͳ������ȴ���
		//���ĸ�������ָ�Ƿ�Ҫ����
		//���һ�������������true�����ֻ�����ɼ����ַ�����false��ʱ�����ص��ַ���Ҳ��
		assertTrue(uiHelper.getSolo().waitForText("��������������", 1, 5000, false, true));
				
		//����½�����ҳ��Ŀؼ��࣬���øÿؼ������Ѿ���װ�õĵ�����水ť
		uiHelper.getElementsNewToDoActivity().clickSaveButton();
		//searchText��������ָ�����ַ����Ƿ���ڣ��ڶ��������������ᵽ��waitForText�����һ�������÷�һ��
		assertTrue(uiHelper.getSolo().searchText("���������Ϊ�գ�", true));
	}
}
