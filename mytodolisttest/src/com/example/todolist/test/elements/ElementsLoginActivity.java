package com.example.todolist.test.elements;

import android.widget.Button;
import android.widget.EditText;

import com.example.todolist.MainActivity;
import com.example.todolist.R;
import com.robotium.solo.Solo;

public class ElementsLoginActivity {
	
	private Solo solo;
	private EditText nameEditText,passwordEditText;
	private Button loginButton;
	
	public ElementsLoginActivity(Solo solo){
		this.solo = solo;
		initViews();
	}
	
	/**
	 * ����¼���ܷ�װ����
	 * @param account
	 */
	public void doLoginWithAccount(String[] account){
		enterName(account[0]);
		enterPassword(account[1]);
		clickLoginButton();
		solo.waitForActivity(MainActivity.class,3000);
		solo.sleep(1500);
	}
	
	/**
	 * ��ʼ����ʱ���ҵ����е�¼ҳ��Ŀؼ�
	 */
	private void initViews(){
		nameEditText = (EditText) solo.getCurrentActivity().findViewById(R.id.nameET);
		passwordEditText = (EditText) solo.getCurrentActivity().findViewById(R.id.passwordET);
		loginButton = (Button) solo.getCurrentActivity().findViewById(R.id.loginBtn);
	}
	
	/**
	 * ����û��������
	 * @return
	 */
	public EditText getNameEditText(){
		return nameEditText;
	}
	
	/**
	 * ������������
	 * @return
	 */
	public EditText getPasswordEditText(){
		return passwordEditText;
	}
	
	/**
	 * ��õ�¼��ť
	 * @return
	 */
	public Button getLoginButton(){
		return loginButton;
	}
	
	/**
	 * �����û���
	 * @param text
	 */
	public void enterName(String text){
		solo.enterText(nameEditText, text);
	}
	
	/**
	 * ��������
	 * @param text
	 */
	public void enterPassword(String text){
		solo.enterText(passwordEditText, text);
	}
	
	/**
	 * �����¼��ť
	 */
	public void clickLoginButton(){
		solo.clickOnView(loginButton);
	}

}
