package com.example.todolist.test;

import com.example.todolist.test.elements.ElementsEditToDoItemActivity;
import com.example.todolist.test.elements.ElementsLoginActivity;
import com.example.todolist.test.elements.ElementsMainActivity;
import com.example.todolist.test.elements.ElementsNewToDoActivity;
import com.robotium.solo.Solo;

public class UIHelper {
	private Solo solo = null;
	private ElementsLoginActivity elementsLoginActivity;
	private ElementsMainActivity elementsMainActivity;
	private ElementsNewToDoActivity elementsNewToDoActivity;
	private ElementsEditToDoItemActivity elementsEditToDoItemActivity;
	
	public UIHelper(Solo solo){
		this.solo = solo;
	}
	
	public Solo getSolo(){
		return solo;
	}
	
	
	/**
	 * 单例模式
	 * @return
	
	public  ElementsLoginActivity getElementsLoginActivity(){
		if(elementsLoginActivity == null){
			elementsLoginActivity = new ElementsLoginActivity(solo);
			return elementsLoginActivity;
		}
		return elementsLoginActivity;
	} */
	public ElementsLoginActivity getElementsLoginActivity() {
		elementsLoginActivity = new ElementsLoginActivity(solo);
		return elementsLoginActivity;
	}
	
	
	/**
	 * 单例模式
	 * @return
	 */
	public  ElementsMainActivity getElementsMainActivity(){
		if(elementsMainActivity == null){
			elementsMainActivity = new ElementsMainActivity(solo);
			return elementsMainActivity;
		}
		return elementsMainActivity;
	}
	
	/**
	 * 单例模式
	 * @return
	 */
	public  ElementsNewToDoActivity getElementsNewToDoActivity(){
		if(elementsNewToDoActivity == null){
			elementsNewToDoActivity = new ElementsNewToDoActivity(solo);
			return elementsNewToDoActivity;
		}
		return elementsNewToDoActivity;
	}
	
	/**
	 * 单例模式
	 * @return
	 */
	public  ElementsEditToDoItemActivity getElementsEditToDoItemActivity(){
		if(elementsEditToDoItemActivity == null){
			elementsEditToDoItemActivity = new ElementsEditToDoItemActivity(solo);
			return elementsEditToDoItemActivity;
		}
		return elementsEditToDoItemActivity;
	}
	
}
