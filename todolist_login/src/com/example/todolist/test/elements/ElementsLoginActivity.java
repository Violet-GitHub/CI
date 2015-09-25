/**
 * 封装界面模型
 */
package com.example.todolist.test.elements;

import com.example.todolist.R;
import com.robotium.solo.Solo;
import android.widget.Button;
import android.widget.EditText;

public class ElementsLoginActivity{
	private Solo solo = null;
	private EditText usernameEditText;
	private EditText passwordEditText;
	private Button loginButton;
	
	public ElementsLoginActivity(Solo solo) {
		this.solo=solo;
		init();
	}
	
	public void init(){
		usernameEditText = (EditText)solo.getCurrentActivity().findViewById(R.id.nameET);
		passwordEditText = (EditText)solo.getCurrentActivity().findViewById(R.id.passwordET);
		loginButton = (Button)solo.getCurrentActivity().findViewById(R.id.loginBtn);
	}
	
	public void login(String[] str){
		enterNameEditText(str[0]);
		enterPasswordEditText(str[1]);
		clickLoginButton();
	}
	
	public void enterNameEditText(String nameStr){
		solo.enterText(usernameEditText, nameStr);
	}
	
	public void enterPasswordEditText(String passwordStr){
		solo.enterText(passwordEditText, passwordStr);
	}
	
	public void clickLoginButton(){
		solo.clickOnView(loginButton);
	}
	
	public EditText getUsernameEditText() {
		return usernameEditText;
	}
	
	public EditText getPasswordEditText() {
		return passwordEditText;
	}
	
}
