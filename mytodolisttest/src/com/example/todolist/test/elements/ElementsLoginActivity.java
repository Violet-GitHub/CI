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
	 * 将登录功能封装起来
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
	 * 初始化的时候，找到所有登录页面的控件
	 */
	private void initViews(){
		nameEditText = (EditText) solo.getCurrentActivity().findViewById(R.id.nameET);
		passwordEditText = (EditText) solo.getCurrentActivity().findViewById(R.id.passwordET);
		loginButton = (Button) solo.getCurrentActivity().findViewById(R.id.loginBtn);
	}
	
	/**
	 * 获得用户名输入框
	 * @return
	 */
	public EditText getNameEditText(){
		return nameEditText;
	}
	
	/**
	 * 获得密码输入框
	 * @return
	 */
	public EditText getPasswordEditText(){
		return passwordEditText;
	}
	
	/**
	 * 获得登录按钮
	 * @return
	 */
	public Button getLoginButton(){
		return loginButton;
	}
	
	/**
	 * 输入用户名
	 * @param text
	 */
	public void enterName(String text){
		solo.enterText(nameEditText, text);
	}
	
	/**
	 * 输入密码
	 * @param text
	 */
	public void enterPassword(String text){
		solo.enterText(passwordEditText, text);
	}
	
	/**
	 * 点击登录按钮
	 */
	public void clickLoginButton(){
		solo.clickOnView(loginButton);
	}

}
