package com.example.todolist.test;

import com.example.todolist.test.elements.ElementsEditToDoItemActivity;
import com.example.todolist.test.elements.ElementsLoginActivity;
import com.example.todolist.test.elements.ElementsMainActivity;
import com.example.todolist.test.elements.ElementsNewToDoItemActivity;
import com.robotium.solo.Solo;

public class UIHelper {
	private Solo solo=null;
	private ElementsLoginActivity loginActivity;
	
	public UIHelper(Solo solo) {
		this.solo=solo;
	}
	
	public ElementsLoginActivity getElementsLoginActivity(){
		loginActivity=new ElementsLoginActivity(solo);
		return loginActivity;
	}
	
	public ElementsMainActivity getElementsMainActivity(){
		ElementsMainActivity mainActivity=new ElementsMainActivity(solo);
		return mainActivity;
	}
	
	public ElementsNewToDoItemActivity geElementsNewToDoItemActivity(){
		ElementsNewToDoItemActivity elementsNewToDoItemActivity=new ElementsNewToDoItemActivity(solo);
		return elementsNewToDoItemActivity;
	}
	
	public ElementsEditToDoItemActivity geElementsEditToDoItemActivity(){
		ElementsEditToDoItemActivity elementsEditToDoItemActivity=new ElementsEditToDoItemActivity(solo);
		return elementsEditToDoItemActivity;
	}
	
}
